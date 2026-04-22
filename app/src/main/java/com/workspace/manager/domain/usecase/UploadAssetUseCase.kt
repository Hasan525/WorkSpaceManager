package com.workspace.manager.domain.usecase

import android.net.Uri
import com.workspace.manager.domain.model.Asset
import com.workspace.manager.domain.repository.WorkspaceRepository
import javax.inject.Inject

class UploadAssetUseCase @Inject constructor(
    private val repository: WorkspaceRepository
) {
    suspend operator fun invoke(uri: Uri): Asset = repository.uploadAsset(uri)
}
