package com.workspace.manager.domain.usecase

import com.workspace.manager.domain.repository.WorkspaceRepository
import javax.inject.Inject

class ReorderItemsUseCase @Inject constructor(
    private val repository: WorkspaceRepository
) {
    suspend fun reorderNote(id: String, newSortOrder: Long) =
        repository.reorderNote(id, newSortOrder)

    suspend fun reorderAsset(id: String, newSortOrder: Long) =
        repository.reorderAsset(id, newSortOrder)
}
