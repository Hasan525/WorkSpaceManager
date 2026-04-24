package com.workspace.manager.ui.workspace;

import com.workspace.manager.data.local.NetworkConnectivityObserver;
import com.workspace.manager.domain.repository.WorkspaceRepository;
import com.workspace.manager.domain.usecase.DeleteNoteUseCase;
import com.workspace.manager.domain.usecase.GetWorkspaceItemsUseCase;
import com.workspace.manager.domain.usecase.ReorderItemsUseCase;
import com.workspace.manager.domain.usecase.ResolveConflictUseCase;
import com.workspace.manager.domain.usecase.SaveNoteUseCase;
import com.workspace.manager.domain.usecase.UploadAssetUseCase;
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
public final class WorkspaceViewModel_Factory implements Factory<WorkspaceViewModel> {
  private final Provider<GetWorkspaceItemsUseCase> getWorkspaceItemsProvider;

  private final Provider<SaveNoteUseCase> saveNoteProvider;

  private final Provider<DeleteNoteUseCase> deleteNoteProvider;

  private final Provider<UploadAssetUseCase> uploadAssetProvider;

  private final Provider<ReorderItemsUseCase> reorderItemsProvider;

  private final Provider<ResolveConflictUseCase> resolveConflictProvider;

  private final Provider<WorkspaceRepository> repositoryProvider;

  private final Provider<NetworkConnectivityObserver> networkObserverProvider;

  public WorkspaceViewModel_Factory(Provider<GetWorkspaceItemsUseCase> getWorkspaceItemsProvider,
      Provider<SaveNoteUseCase> saveNoteProvider, Provider<DeleteNoteUseCase> deleteNoteProvider,
      Provider<UploadAssetUseCase> uploadAssetProvider,
      Provider<ReorderItemsUseCase> reorderItemsProvider,
      Provider<ResolveConflictUseCase> resolveConflictProvider,
      Provider<WorkspaceRepository> repositoryProvider,
      Provider<NetworkConnectivityObserver> networkObserverProvider) {
    this.getWorkspaceItemsProvider = getWorkspaceItemsProvider;
    this.saveNoteProvider = saveNoteProvider;
    this.deleteNoteProvider = deleteNoteProvider;
    this.uploadAssetProvider = uploadAssetProvider;
    this.reorderItemsProvider = reorderItemsProvider;
    this.resolveConflictProvider = resolveConflictProvider;
    this.repositoryProvider = repositoryProvider;
    this.networkObserverProvider = networkObserverProvider;
  }

  @Override
  public WorkspaceViewModel get() {
    return newInstance(getWorkspaceItemsProvider.get(), saveNoteProvider.get(), deleteNoteProvider.get(), uploadAssetProvider.get(), reorderItemsProvider.get(), resolveConflictProvider.get(), repositoryProvider.get(), networkObserverProvider.get());
  }

  public static WorkspaceViewModel_Factory create(
      Provider<GetWorkspaceItemsUseCase> getWorkspaceItemsProvider,
      Provider<SaveNoteUseCase> saveNoteProvider, Provider<DeleteNoteUseCase> deleteNoteProvider,
      Provider<UploadAssetUseCase> uploadAssetProvider,
      Provider<ReorderItemsUseCase> reorderItemsProvider,
      Provider<ResolveConflictUseCase> resolveConflictProvider,
      Provider<WorkspaceRepository> repositoryProvider,
      Provider<NetworkConnectivityObserver> networkObserverProvider) {
    return new WorkspaceViewModel_Factory(getWorkspaceItemsProvider, saveNoteProvider, deleteNoteProvider, uploadAssetProvider, reorderItemsProvider, resolveConflictProvider, repositoryProvider, networkObserverProvider);
  }

  public static WorkspaceViewModel newInstance(GetWorkspaceItemsUseCase getWorkspaceItems,
      SaveNoteUseCase saveNote, DeleteNoteUseCase deleteNote, UploadAssetUseCase uploadAsset,
      ReorderItemsUseCase reorderItems, ResolveConflictUseCase resolveConflict,
      WorkspaceRepository repository, NetworkConnectivityObserver networkObserver) {
    return new WorkspaceViewModel(getWorkspaceItems, saveNote, deleteNote, uploadAsset, reorderItems, resolveConflict, repository, networkObserver);
  }
}
