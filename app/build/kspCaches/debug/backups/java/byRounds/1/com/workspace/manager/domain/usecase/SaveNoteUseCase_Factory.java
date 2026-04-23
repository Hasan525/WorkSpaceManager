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
public final class SaveNoteUseCase_Factory implements Factory<SaveNoteUseCase> {
  private final Provider<WorkspaceRepository> repositoryProvider;

  public SaveNoteUseCase_Factory(Provider<WorkspaceRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public SaveNoteUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static SaveNoteUseCase_Factory create(Provider<WorkspaceRepository> repositoryProvider) {
    return new SaveNoteUseCase_Factory(repositoryProvider);
  }

  public static SaveNoteUseCase newInstance(WorkspaceRepository repository) {
    return new SaveNoteUseCase(repository);
  }
}
