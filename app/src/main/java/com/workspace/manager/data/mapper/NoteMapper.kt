package com.workspace.manager.data.mapper

import com.workspace.manager.data.local.NoteEntity
import com.workspace.manager.data.model.NoteDto
import com.workspace.manager.domain.model.Note

fun NoteEntity.toDomain(): Note = Note(
    id = id, title = title, content = content,
    sortOrder = sortOrder, createdAt = createdAt, updatedAt = updatedAt,
    isPendingSync = isPendingSync, isConflicted = isConflicted,
    remoteContent = remoteContent, remoteUpdatedAt = remoteUpdatedAt
)

fun Note.toEntity(): NoteEntity = NoteEntity(
    id = id, title = title, content = content,
    sortOrder = sortOrder, createdAt = createdAt, updatedAt = updatedAt,
    isPendingSync = isPendingSync, isConflicted = isConflicted,
    remoteContent = remoteContent, remoteUpdatedAt = remoteUpdatedAt
)

fun Note.toDto(): NoteDto = NoteDto(
    id = id, title = title, content = content,
    sortOrder = sortOrder, createdAt = createdAt, updatedAt = updatedAt
)

/**
 * Converts a Firestore DTO to a Room entity with clean sync state.
 * Conflict fields (isConflicted, remoteContent, etc.) are handled by
 * [WorkspaceRepositoryImpl.startFirestoreSync] — never set here.
 */
fun NoteDto.toEntity(): NoteEntity = NoteEntity(
    id = id, title = title, content = content,
    sortOrder = sortOrder, createdAt = createdAt, updatedAt = updatedAt,
    isPendingSync = false, isConflicted = false,
    remoteContent = null,
    // Track the remote version we last received so conflict detection can compare
    // against it instead of always treating 0L as the baseline.
    remoteUpdatedAt = updatedAt
)
