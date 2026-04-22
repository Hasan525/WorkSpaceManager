package com.workspace.manager.ui.note

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workspace.manager.domain.repository.WorkspaceRepository
import com.workspace.manager.domain.usecase.DeleteNoteUseCase
import com.workspace.manager.domain.usecase.SaveNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NoteEditorUiState(
    val noteId: String? = null,
    val title: String = "",
    val content: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val sortOrder: Long = System.currentTimeMillis(),
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class NoteEditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: WorkspaceRepository,
    private val saveNote: SaveNoteUseCase,
    private val deleteNote: DeleteNoteUseCase
) : ViewModel() {

    // Keys used in SavedStateHandle to survive process death
    companion object {
        const val KEY_NOTE_ID = "noteId"
        const val KEY_TITLE = "draftTitle"
        const val KEY_CONTENT = "draftContent"
    }

    private val noteId: String? = savedStateHandle[KEY_NOTE_ID]

    private val _uiState = MutableStateFlow(
        NoteEditorUiState(
            noteId = noteId,
            // Restore draft from saved state (survives process death)
            title = savedStateHandle[KEY_TITLE] ?: "",
            content = savedStateHandle[KEY_CONTENT] ?: ""
        )
    )
    val uiState: StateFlow<NoteEditorUiState> = _uiState.asStateFlow()

    private val _savedState = savedStateHandle

    init {
        noteId?.let { loadNote(it) }
    }

    private fun loadNote(id: String) {
        viewModelScope.launch {
            repository.observeNotes()
                .map { notes -> notes.find { it.id == id } }
                .filterNotNull()
                .firstOrNull()
                ?.let { note ->
                    // Only update if user hasn't made local edits yet
                    if (_uiState.value.title.isBlank() && _uiState.value.content.isBlank()) {
                        _uiState.update {
                            it.copy(
                                title = note.title,
                                content = note.content,
                                sortOrder = note.sortOrder,
                                createdAt = note.createdAt
                            )
                        }
                    }
                }
        }
    }

    fun onTitleChanged(value: String) {
        _uiState.update { it.copy(title = value) }
        _savedState[KEY_TITLE] = value  // persist to saved state immediately
    }

    fun onContentChanged(value: String) {
        _uiState.update { it.copy(content = value) }
        _savedState[KEY_CONTENT] = value  // persist to saved state immediately
    }

    fun save() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            try {
                val state = _uiState.value
                saveNote(
                    id = state.noteId,
                    title = state.title,
                    content = state.content,
                    sortOrder = state.sortOrder,
                    createdAt = state.createdAt
                )
                _uiState.update { it.copy(isSaving = false, isSaved = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isSaving = false, error = e.message) }
            }
        }
    }

    fun delete(onComplete: () -> Unit) {
        val id = _uiState.value.noteId ?: return
        viewModelScope.launch {
            deleteNote(id)
            onComplete()
        }
    }

    fun clearError() = _uiState.update { it.copy(error = null) }
}
