package com.workspace.manager.ui.workspace

import com.workspace.manager.domain.model.ConflictInfo
import com.workspace.manager.domain.model.WorkspaceItem

data class WorkspaceUiState(
    val items: List<WorkspaceItem> = emptyList(),
    val conflicts: List<ConflictInfo> = emptyList(),
    val isOnline: Boolean = true,
    val isLoading: Boolean = true,
    val activeConflict: ConflictInfo? = null,
    val draggedItemId: String? = null,
    val selectedAssetId: String? = null,
    val assetPendingDelete: String? = null,
    val error: String? = null
)
