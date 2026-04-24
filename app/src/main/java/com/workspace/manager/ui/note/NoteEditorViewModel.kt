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

    // Keys used in SavedStateHandle to survive process death
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
            // withTimeoutOrNull prevents hanging forever if Room is slow on first open
            withTimeoutOrNull(3_000L) {
                repository.observeNotes()
                    .mapNotNull { notes -> notes.find { it.id == id } }
                    .first()   // collect exactly one item then cancel — safe with mapNotNull
            }?.let { note ->
                // Only populate fields if the user hasn't already started typing
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
        // Guard against rapid double-taps — second tap is a no-op while the first is in flight
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
                // Ensure the spinner is visible for a perceivable beat so users get
                // clear feedback that their save was accepted (the local write itself
                // is sub-frame so without this the spinner only "blinks").
                val elapsed = System.currentTimeMillis() - started
                if (elapsed < MIN_SAVE_FEEDBACK_MS) delay(MIN_SAVE_FEEDBACK_MS - elapsed)
                _uiState.update { it.copy(isSaving = false, isSaved = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isSaving = false, error = e.message) }
            }
        }
    }

    /** Called by the screen after it has acted on isSaved=true (e.g. navigated back) */
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
