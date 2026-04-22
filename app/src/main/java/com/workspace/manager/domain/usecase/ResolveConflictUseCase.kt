package com.workspace.manager.domain.usecase

import com.workspace.manager.domain.model.ConflictResolution
import com.workspace.manager.domain.repository.WorkspaceRepository
import javax.inject.Inject

class ResolveConflictUseCase @Inject constructor(
    private val repository: WorkspaceRepository
) {
    suspend operator fun invoke(noteId: String, resolution: ConflictResolution) =
        repository.resolveConflict(noteId, resolution)
}
