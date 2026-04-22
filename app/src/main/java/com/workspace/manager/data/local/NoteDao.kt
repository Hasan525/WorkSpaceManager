package com.workspace.manager.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes ORDER BY sortOrder ASC")
    fun observeAllNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE isConflicted = 1")
    fun observeConflictedNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE isPendingSync = 1")
    suspend fun getPendingSyncNotes(): List<NoteEntity>

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteById(id: String): NoteEntity?

    @Upsert
    suspend fun upsertNote(note: NoteEntity)

    @Transaction
    suspend fun upsertNotes(notes: List<NoteEntity>) {
        notes.forEach { upsertNote(it) }
    }

    @Query("UPDATE notes SET isPendingSync = 0, isConflicted = 0 WHERE id = :id")
    suspend fun markSynced(id: String)

    @Query("UPDATE notes SET isConflicted = 0 WHERE id = :id")
    suspend fun clearConflict(id: String)

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteNote(id: String)

    @Query("UPDATE notes SET sortOrder = :sortOrder WHERE id = :id")
    suspend fun updateSortOrder(id: String, sortOrder: Long)
}
