package com.workspace.manager.domain.usecase;

import com.workspace.manager.domain.repository.WorkspaceRepository;
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
public final class DeleteNoteUseCase_Factory implements Factory<DeleteNoteUseCase> {
  private final Provider<WorkspaceRepository> repositoryProvider;

  public DeleteNoteUseCase_Factory(Provider<WorkspaceRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public DeleteNoteUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static DeleteNoteUseCase_Factory create(Provider<WorkspaceRepository> repositoryProvider) {
    return new DeleteNoteUseCase_Factory(repositoryProvider);
  }

  public static DeleteNoteUseCase newInstance(WorkspaceRepository repository) {
    return new DeleteNoteUseCase(repository);
  }
}
