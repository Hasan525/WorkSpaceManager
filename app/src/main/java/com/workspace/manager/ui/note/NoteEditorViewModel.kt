package com.workspace.manager.ui.note

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workspace.manager.domain.repository.WorkspaceRepository
import com.workspace.manager.domain.usecase.DeleteNoteUseCase
import com.workspace.manager.domain.usecase.SaveNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
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

    companion object {
        const val KEY_NOTE_ID = "noteId"
        const val KEY_TITLE = "draftTitle"
        const val KEY_CONTENT = "draftContent"
        private const val MIN_SAVE_FEEDBACK_MS = 600L
    }

    private val noteId: String? = savedStateHandle[KEY_NOTE_ID]

    private val _uiState = MutableStateFlow(
        NoteEditorUiState(
            noteId = noteId,
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
            withTimeoutOrNull(3_000L) {
                repository.observeNotes()
                    .mapNotNull { notes -> notes.find { it.id == id } }
                    .first()
            }?.let { note ->
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
        _savedState[KEY_TITLE] = value
    }

    fun onContentChanged(value: String) {
        _uiState.update { it.copy(content = value) }
        _savedState[KEY_CONTENT] = value
    }

    fun save() {
        if (_uiState.value.isSaving) return
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, isSaved = false) }
            val started = System.currentTimeMillis()
            try {
                val state = _uiState.value
                saveNote(
                    id = state.noteId,
                    title = state.title,
                    content = state.content,
                    sortOrder = state.sortOrder,
                    createdAt = state.createdAt
                )
                val elapsed = System.currentTimeMillis() - started
                if (elapsed < MIN_SAVE_FEEDBACK_MS) delay(MIN_SAVE_FEEDBACK_MS - elapsed)
                _uiState.update { it.copy(isSaving = false, isSaved = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isSaving = false, error = e.message) }
            }
        }
    }

    fun onSavedHandled() = _uiState.update { it.copy(isSaved = false) }

    fun delete(onComplete: () -> Unit) {
        val id = _uiState.value.noteId ?: return
        viewModelScope.launch {
            deleteNote(id)
            onComplete()
        }
    }

    fun clearError() = _uiState.update { it.copy(error = null) }
}
