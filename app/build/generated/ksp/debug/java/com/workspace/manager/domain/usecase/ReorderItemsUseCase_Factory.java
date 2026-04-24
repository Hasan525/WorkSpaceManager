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
public final class ReorderItemsUseCase_Factory implements Factory<ReorderItemsUseCase> {
  private final Provider<WorkspaceRepository> repositoryProvider;

  public ReorderItemsUseCase_Factory(Provider<WorkspaceRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public ReorderItemsUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static ReorderItemsUseCase_Factory create(
      Provider<WorkspaceRepository> repositoryProvider) {
    return new ReorderItemsUseCase_Factory(repositoryProvider);
  }

  public static ReorderItemsUseCase newInstance(WorkspaceRepository repository) {
    return new ReorderItemsUseCase(repository);
  }
}
