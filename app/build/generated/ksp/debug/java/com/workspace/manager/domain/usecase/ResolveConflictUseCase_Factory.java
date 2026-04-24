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
public final class ResolveConflictUseCase_Factory implements Factory<ResolveConflictUseCase> {
  private final Provider<WorkspaceRepository> repositoryProvider;

  public ResolveConflictUseCase_Factory(Provider<WorkspaceRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public ResolveConflictUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static ResolveConflictUseCase_Factory create(
      Provider<WorkspaceRepository> repositoryProvider) {
    return new ResolveConflictUseCase_Factory(repositoryProvider);
  }

  public static ResolveConflictUseCase newInstance(WorkspaceRepository repository) {
    return new ResolveConflictUseCase(repository);
  }
}
