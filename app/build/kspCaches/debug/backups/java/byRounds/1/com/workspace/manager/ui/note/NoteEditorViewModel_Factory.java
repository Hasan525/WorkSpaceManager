package com.workspace.manager.ui.note;

import androidx.lifecycle.SavedStateHandle;
import com.workspace.manager.domain.repository.WorkspaceRepository;
import com.workspace.manager.domain.usecase.DeleteNoteUseCase;
import com.workspace.manager.domain.usecase.SaveNoteUseCase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class NoteEditorViewModel_Factory implements Factory<NoteEditorViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<WorkspaceRepository> repositoryProvider;

  private final Provider<SaveNoteUseCase> saveNoteProvider;

  private final Provider<DeleteNoteUseCase> deleteNoteProvider;

  public NoteEditorViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<WorkspaceRepository> repositoryProvider, Provider<SaveNoteUseCase> saveNoteProvider,
      Provider<DeleteNoteUseCase> deleteNoteProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.repositoryProvider = repositoryProvider;
    this.saveNoteProvider = saveNoteProvider;
    this.deleteNoteProvider = deleteNoteProvider;
  }

  @Override
  public NoteEditorViewModel get() {
    return newInstance(savedStateHandleProvider.get(), repositoryProvider.get(), saveNoteProvider.get(), deleteNoteProvider.get());
  }

  public static NoteEditorViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<WorkspaceRepository> repositoryProvider, Provider<SaveNoteUseCase> saveNoteProvider,
      Provider<DeleteNoteUseCase> deleteNoteProvider) {
    return new NoteEditorViewModel_Factory(savedStateHandleProvider, repositoryProvider, saveNoteProvider, deleteNoteProvider);
  }

  public static NoteEditorViewModel newInstance(SavedStateHandle savedStateHandle,
      WorkspaceRepository repository, SaveNoteUseCase saveNote, DeleteNoteUseCase deleteNote) {
    return new NoteEditorViewModel(savedStateHandle, repository, saveNote, deleteNote);
  }
}
