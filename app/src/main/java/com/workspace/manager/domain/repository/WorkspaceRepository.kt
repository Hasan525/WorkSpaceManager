package com.workspace.manager.domain.repository

import android.net.Uri
import com.workspace.manager.domain.model.Asset
import com.workspace.manager.domain.model.ConflictInfo
import com.workspace.manager.domain.model.ConflictResolution
import com.workspace.manager.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface WorkspaceRepository {
    fun observeNotes(): Flow<List<Note>>
    fun observeAssets(): Flow<List<Asset>>
    fun observeConflicts(): Flow<List<ConflictInfo>>

    suspend fun saveNote(note: Note)
    suspend fun deleteNote(id: String)
    suspend fun reorderNote(id: String, newSortOrder: Long)

    suspend fun uploadAsset(localUri: Uri): Asset
    suspend fun deleteAsset(id: String)
    suspend fun updateAssetRotation(id: String, angle: Float)
    suspend fun reorderAsset(id: String, newSortOrder: Long)

    suspend fun resolveConflict(noteId: String, resolution: ConflictResolution)
    suspend fun syncPendingChanges()
}
