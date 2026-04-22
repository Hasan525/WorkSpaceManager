package com.workspace.manager.domain.usecase

import com.workspace.manager.domain.model.Note
import com.workspace.manager.domain.repository.WorkspaceRepository
import java.util.UUID
import javax.inject.Inject

class SaveNoteUseCase @Inject constructor(
    private val repository: WorkspaceRepository
) {
    suspend operator fun invoke(
        id: String? = null,
        title: String,
        content: String,
        sortOrder: Long,
        createdAt: Long = System.currentTimeMillis()
    ) {
        val now = System.currentTimeMillis()
        val note = Note(
            id = id ?: UUID.randomUUID().toString(),
            title = title.ifBlank { "Untitled Note" },
            content = content,
            sortOrder = sortOrder,
            createdAt = createdAt,
            updatedAt = now,
            isPendingSync = true
        )
        repository.saveNote(note)
    }
}
