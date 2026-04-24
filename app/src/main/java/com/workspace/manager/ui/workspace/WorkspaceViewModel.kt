package com.workspace.manager.ui.workspace

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workspace.manager.data.local.NetworkConnectivityObserver
import com.workspace.manager.domain.model.ConflictResolution
import com.workspace.manager.domain.model.WorkspaceItem
import com.workspace.manager.domain.repository.WorkspaceRepository
import com.workspace.manager.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class WorkspaceViewModel @Inject constructor(
    private val getWorkspaceItems: GetWorkspaceItemsUseCase,
    private val saveNote: SaveNoteUseCase,
    private val deleteNote: DeleteNoteUseCase,
    private val uploadAsset: UploadAssetUseCase,
    private val reorderItems: ReorderItemsUseCase,
    private val resolveConflict: ResolveConflictUseCase,
    private val repository: WorkspaceRepository,
    private val networkObserver: NetworkConnectivityObserver
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkspaceUiState())
    val uiState: StateFlow<WorkspaceUiState> = _uiState.asStateFlow()

    init {
        observeItems()
        observeConflicts()
        observeNetwork()
    }

    private fun observeItems() {
        getWorkspaceItems()
            .onEach { items -> _uiState.update { it.copy(items = items, isLoading = false) } }
            .catch { e -> _uiState.update { it.copy(error = e.message, isLoading = false) } }
            .launchIn(viewModelScope)
    }

    private fun observeConflicts() {
        repository.observeConflicts()
            .onEach { conflicts -> _uiState.update { it.copy(conflicts = conflicts) } }
            .launchIn(viewModelScope)
    }

    private fun observeNetwork() {
        networkObserver.isConnected
            .onEach { online -> _uiState.update { it.copy(isOnline = online) } }
            .launchIn(viewModelScope)
    }

    /**
     * Creates a new note and invokes [onCreated] with the new note's ID so the
     * caller can navigate directly to the editor — removing the extra tap.
     */
    fun createNote(onCreated: (noteId: String) -> Unit = {}) {
        viewModelScope.launch {
            val id = UUID.randomUUID().toString()
            val now = System.currentTimeMillis()
            saveNote(id = id, title = "", content = "", sortOrder = now)
            onCreated(id)
        }
    }

    fun deleteNote(id: String) {
        viewModelScope.launch { deleteNote.invoke(id) }
    }

    fun pickImage(uri: Uri) {
        viewModelScope.launch {
            try {
                uploadAsset(uri)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Failed to add image: ${e.message}") }
            }
        }
    }

    fun onDragStart(itemId: String) {
        _uiState.update { it.copy(draggedItemId = itemId) }
    }

    fun onDragEnd(itemId: String, targetIndex: Int) {
        viewModelScope.launch {
            val items = _uiState.value.items
            val newSortOrder = computeSortOrderAt(items, targetIndex)
            val item = items.find { it.id == itemId } ?: return@launch
            when (item) {
                is WorkspaceItem.NoteItem -> reorderItems.reorderNote(itemId, newSortOrder)
                is WorkspaceItem.AssetItem -> reorderItems.reorderAsset(itemId, newSortOrder)
            }
            _uiState.update { it.copy(draggedItemId = null) }
        }
    }

    fun onAssetRotated(assetId: String, angle: Float) {
        viewModelScope.launch { repository.updateAssetRotation(assetId, angle) }
    }

    fun showConflict(conflictId: String) {
        val conflict = _uiState.value.conflicts.find { it.itemId == conflictId }
        _uiState.update { it.copy(activeConflict = conflict) }
    }

    fun dismissConflict() {
        _uiState.update { it.copy(activeConflict = null) }
    }

    fun resolveConflict(resolution: ConflictResolution) {
        val conflict = _uiState.value.activeConflict ?: return
        viewModelScope.launch {
            resolveConflict(conflict.itemId, resolution)
            _uiState.update { it.copy(activeConflict = null) }
        }
    }

    fun clearError() = _uiState.update { it.copy(error = null) }

    /**
     * Computes a sort order that places an item at [index] using midpoint
     * arithmetic so no global renumbering is required.
     */
    private fun computeSortOrderAt(items: List<WorkspaceItem>, index: Int): Long {
        return when {
            items.isEmpty() -> System.currentTimeMillis()
            index <= 0 -> items.first().sortOrder - 1_000L
            index >= items.size -> items.last().sortOrder + 1_000L
            else -> (items[index - 1].sortOrder + items[index].sortOrder) / 2
        }
    }
}
