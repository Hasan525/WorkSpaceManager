package com.workspace.manager.domain.usecase

import com.workspace.manager.domain.model.WorkspaceItem
import com.workspace.manager.domain.repository.WorkspaceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetWorkspaceItemsUseCase @Inject constructor(
    private val repository: WorkspaceRepository
) {
    operator fun invoke(): Flow<List<WorkspaceItem>> =
        combine(repository.observeNotes(), repository.observeAssets()) { notes, assets ->
            val items = mutableListOf<WorkspaceItem>()
            items.addAll(notes.map { WorkspaceItem.NoteItem(it) })
            items.addAll(assets.map { WorkspaceItem.AssetItem(it) })
            items.sortBy { it.sortOrder }
            items
        }
}
