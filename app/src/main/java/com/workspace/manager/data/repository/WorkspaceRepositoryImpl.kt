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
        scope.launch {
            try { firestoreDataSource.ensureAuth() } catch (_: Exception) {}
            startFirestoreSync()
        }
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
        noteDao.upsertNote(note.toEntity().copy(isPendingSync = true))
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
        assetDao.upsertAsset(asset.toEntity())
        trySync {
            val url = storageDataSource.uploadImage(localUri, id)
            val synced = asset.copy(downloadUrl = url, isPendingSync = false)
            assetDao.upsertAsset(synced.toEntity())
            firestoreDataSource.saveAsset(synced.toDto())
            assetDao.markSynced(id)
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
        trySync { firestoreDataSource.saveAsset(asset.toDto()); assetDao.markSynced(id) }
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
                trySync {
                    firestoreDataSource.saveNote(entity.toDomain().toDto())
                    noteDao.markSynced(noteId)
                }
            }
            ConflictResolution.KEEP_REMOTE -> {
                noteDao.upsertNote(
                    entity.copy(
                        content = entity.remoteContent ?: entity.content,
                        updatedAt = entity.remoteUpdatedAt,
                        isConflicted = false,
                        isPendingSync = false,
                        remoteUpdatedAt = entity.remoteUpdatedAt
                    )
                )
            }
            ConflictResolution.MERGE -> {
                val merged = buildString {
                    append(entity.content)
                    append("\n\n--- Remote version ---\n\n")
                    append(entity.remoteContent ?: "")
                }
                val now = System.currentTimeMillis()
                val mergedEntity = entity.copy(
                    content = merged,
                    updatedAt = now,
                    isConflicted = false,
                    isPendingSync = true
                )
                noteDao.upsertNote(mergedEntity)
                trySync {
                    firestoreDataSource.saveNote(mergedEntity.toDomain().toDto())
                    noteDao.markSynced(noteId)
                }
            }
        }
    }

    // ── Pending sync ──────────────────────────────────────────────────────────

    override suspend fun syncPendingChanges() {
        noteDao.getPendingSyncNotes().forEach { entity ->
            trySync {
                firestoreDataSource.saveNote(entity.toDomain().toDto())
                noteDao.markSynced(entity.id)
            }
        }
        assetDao.getPendingSyncAssets().forEach { entity ->
            trySync {
                // If the downloadUrl is still a local URI, the Storage upload failed while
                // offline — re-upload now before pushing the document to Firestore.
                val dto = if (entity.downloadUrl.startsWith("content://") ||
                    entity.downloadUrl.startsWith("file://")
                ) {
                    val uploadUri = Uri.parse(entity.localUri ?: entity.downloadUrl)
                    val url = storageDataSource.uploadImage(uploadUri, entity.id)
                    val updated = entity.copy(downloadUrl = url)
                    assetDao.upsertAsset(updated)
                    updated.toDomain().toDto()
                } else {
                    entity.toDomain().toDto()
                }
                firestoreDataSource.saveAsset(dto)
                assetDao.markSynced(entity.id)
            }
        }
    }

    // ── Internal helpers ──────────────────────────────────────────────────────

    /**
     * Fire-and-forget remote sync. Local-first: callers should never wait for
     * the network — they update Room synchronously and let Firestore catch up
     * in the background. The repo's [scope] is process-lifetime (SupervisorJob)
     * so the launched block survives ViewModel teardown. Failures leave the
     * row marked isPendingSync; SyncWorker / connectivity observer retries.
     */
    private fun trySync(block: suspend () -> Unit) {
        scope.launch {
            try { block() } catch (_: Exception) { /* queued — will retry on next connection */ }
        }
    }

    /**
     * Mirrors Firestore into Room.
     *
     * Conflict detection: a conflict exists only when the local note has
     * unsynchronised changes (isPendingSync) AND the remote version is newer
     * than the last version we successfully synced (remoteUpdatedAt), which
     * means another device wrote to the same note while we were offline.
     *
     * Remote deletions: notes/assets absent from the snapshot that have no
     * local pending changes are removed from Room as well.
     */
    private fun startFirestoreSync() {
        firestoreDataSource.observeNotes().onEach { remoteDtos ->
            val remoteIds = remoteDtos.map { it.id }.toSet()

            // Propagate remote deletions — skip notes with unsync'd local edits
            noteDao.getAllNoteIds().forEach { localId ->
                if (localId !in remoteIds) {
                    val local = noteDao.getNoteById(localId)
                    if (local != null && !local.isPendingSync) {
                        noteDao.deleteNote(localId)
                    }
                }
            }

            // Upsert / conflict-detect for every remote note
            remoteDtos.forEach { dto ->
                val local = noteDao.getNoteById(dto.id)
                when {
                    local == null ->
                        noteDao.upsertNote(dto.toEntity())

                    // Another device modified the same note while we were offline
                    local.isPendingSync && dto.updatedAt > local.remoteUpdatedAt ->
                        noteDao.upsertNote(
                            local.copy(
                                isConflicted = true,
                                remoteContent = dto.content,
                                remoteUpdatedAt = dto.updatedAt
                            )
                        )

                    // No local pending changes — accept the remote version
                    !local.isPendingSync ->
                        noteDao.upsertNote(dto.toEntity())
                }
            }
        }.launchIn(scope)

        firestoreDataSource.observeAssets().onEach { remoteDtos ->
            val remoteIds = remoteDtos.map { it.id }.toSet()

            // Propagate remote deletions
            assetDao.getAllAssetIds().forEach { localId ->
                if (localId !in remoteIds) {
                    val local = assetDao.getAssetById(localId)
                    if (local != null && !local.isPendingSync) {
                        assetDao.deleteAsset(localId)
                    }
                }
            }

            // Upsert remote assets (local pending rotation/reorder takes priority)
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
