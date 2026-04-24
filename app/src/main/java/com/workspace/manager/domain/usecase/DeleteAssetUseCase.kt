package com.workspace.manager.domain.usecase

import com.workspace.manager.domain.repository.WorkspaceRepository
import javax.inject.Inject

class DeleteAssetUseCase @Inject constructor(
    private val repository: WorkspaceRepository
) {
    suspend operator fun invoke(id: String) = repository.deleteAsset(id)
}
