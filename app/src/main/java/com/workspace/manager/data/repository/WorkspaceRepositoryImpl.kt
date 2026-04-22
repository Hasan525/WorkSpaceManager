package com.workspace.manager.data.repository

import android.net.Uri
import com.workspace.manager.data.local.AssetDao
import com.workspace.manager.data.local.NetworkConnectivityObserver
import com.workspace.manager.data.local.NoteDao
import com.workspace.manager.data.mapper.*
import com.workspace.manager.data.remote.FirebaseStorageDataSource
import com.workspace.manager.data.remote.FirestoreDataSource
import com.workspace.manager.domain.model.Asset
import com.workspace.manager.domain.model.ConflictInfo
import com.workspace.manager.domain.model.ConflictResolution
import com.workspace.manager.domain.model.Note
import com.workspace.manager.domain.repository.WorkspaceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkspaceRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao,
    private val assetDao: AssetDao,
    private val firestoreDataSource: FirestoreDataSource,
    private val storageDataSource: FirebaseStorageDataSource,
    private val networkObserver: NetworkConnectivityObserver
) : WorkspaceRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    init {
        startFirestoreSync()
        observeConnectivity()
    }

    // ── Observations ──────────────────────────────────────────────────────────

    override fun observeNotes(): Flow<List<Note>> =
        noteDao.observeAllNotes().map { it.map { e -> e.toDomain() } }

    override fun observeAssets(): Flow<List<Asset>> =
        assetDao.observeAllAssets().map { it.map { e -> e.toDomain() } }

    override fun observeConflicts(): Flow<List<ConflictInfo>> =
        noteDao.observeConflictedNotes().map { entities ->
            entities.map { e ->
                ConflictInfo(
                    itemId = e.id,
                    localContent = e.content,
                    remoteContent = e.remoteContent ?: "",
                    localUpdatedAt = e.updatedAt,
                    remoteUpdatedAt = e.remoteUpdatedAt
                )
            }
        }

    // ── Notes ─────────────────────────────────────────────────────────────────

    override suspend fun saveNote(note: Note) {
        // 1. Atomic local write first
        noteDao.upsertNote(note.toEntity().copy(isPendingSync = true))
        // 2. Try remote sync
        trySync { firestoreDataSource.saveNote(note.toDto()); noteDao.markSynced(note.id) }
    }

    override suspend fun deleteNote(id: String) {
        noteDao.deleteNote(id)
        trySync { firestoreDataSource.deleteNote(id) }
    }

    override suspend fun reorderNote(id: String, newSortOrder: Long) {
        noteDao.updateSortOrder(id, newSortOrder)
        val note = noteDao.getNoteById(id)?.toDomain() ?: return
        trySync { firestoreDataSource.saveNote(note.toDto()) }
    }

    // ── Assets ────────────────────────────────────────────────────────────────

    override suspend fun uploadAsset(localUri: Uri): Asset {
        val id = UUID.randomUUID().toString()
        val now = System.currentTimeMillis()
        val asset = Asset(
            id = id, downloadUrl = localUri.toString(), localUri = localUri.toString(),
            sortOrder = now, createdAt = now, updatedAt = now, isPendingSync = true
        )
        // Save locally first
        assetDao.upsertAsset(asset.toEntity())
        // Upload to Storage then update URL
        trySync {
            val url = storageDataSource.uploadImage(localUri, id)
            val synced = asset.copy(downloadUrl = url, isPendingSync = false)
            assetDao.upsertAsset(synced.toEntity())
            firestoreDataSource.saveAsset(synced.toDto())
        }
        return asset
    }

    override suspend fun deleteAsset(id: String) {
        assetDao.deleteAsset(id)
        trySync { storageDataSource.deleteImage(id); firestoreDataSource.deleteAsset(id) }
    }

    override suspend fun updateAssetRotation(id: String, angle: Float) {
        val now = System.currentTimeMillis()
        assetDao.updateRotation(id, angle, now)
        val asset = assetDao.getAssetById(id)?.toDomain() ?: return
        trySync { firestoreDataSource.saveAsset(asset.toDto()) }
    }

    override suspend fun reorderAsset(id: String, newSortOrder: Long) {
        assetDao.updateSortOrder(id, newSortOrder)
        val asset = assetDao.getAssetById(id)?.toDomain() ?: return
        trySync { firestoreDataSource.saveAsset(asset.toDto()) }
    }

    // ── Conflict resolution ───────────────────────────────────────────────────

    override suspend fun resolveConflict(noteId: String, resolution: ConflictResolution) {
        val entity = noteDao.getNoteById(noteId) ?: return
        when (resolution) {
            ConflictResolution.KEEP_LOCAL -> {
                noteDao.clearConflict(noteId)
                trySync { firestoreDataSource.saveNote(entity.toDomain().toDto()); noteDao.markSynced(noteId) }
            }
            ConflictResolution.KEEP_REMOTE -> {
                noteDao.upsertNote(
                    entity.copy(
                        content = entity.remoteContent ?: entity.content,
                        updatedAt = entity.remoteUpdatedAt,
                        isConflicted = false,
                        isPendingSync = false
                    )
                )
            }
        }
    }

    // ── Pending sync ──────────────────────────────────────────────────────────

    override suspend fun syncPendingChanges() {
        noteDao.getPendingSyncNotes().forEach { entity ->
            trySync { firestoreDataSource.saveNote(entity.toDomain().toDto()); noteDao.markSynced(entity.id) }
        }
        assetDao.getPendingSyncAssets().forEach { entity ->
            trySync { firestoreDataSource.saveAsset(entity.toDomain().toDto()); assetDao.markSynced(entity.id) }
        }
    }

    // ── Internal helpers ──────────────────────────────────────────────────────

    private suspend fun trySync(block: suspend () -> Unit) {
        try { block() } catch (_: Exception) { /* will retry on next connection */ }
    }

    /** Mirror Firestore changes into Room, detecting conflicts */
    private fun startFirestoreSync() {
        firestoreDataSource.observeNotes().onEach { remoteDtos ->
            remoteDtos.forEach { dto ->
                val local = noteDao.getNoteById(dto.id)
                when {
                    local == null -> noteDao.upsertNote(dto.toEntity())
                    local.isPendingSync && dto.updatedAt > local.remoteUpdatedAt -> {
                        // CONFLICT
                        noteDao.upsertNote(local.copy(isConflicted = true, remoteContent = dto.content, remoteUpdatedAt = dto.updatedAt))
                    }
                    !local.isPendingSync -> noteDao.upsertNote(dto.toEntity())
                }
            }
        }.launchIn(scope)

        firestoreDataSource.observeAssets().onEach { remoteDtos ->
            remoteDtos.forEach { dto ->
                val local = assetDao.getAssetById(dto.id)
                if (local == null || !local.isPendingSync) {
                    assetDao.upsertAsset(dto.toEntity())
                }
            }
        }.launchIn(scope)
    }

    private fun observeConnectivity() {
        networkObserver.isConnected.onEach { connected ->
            if (connected) scope.launch { syncPendingChanges() }
        }.launchIn(scope)
    }
}
