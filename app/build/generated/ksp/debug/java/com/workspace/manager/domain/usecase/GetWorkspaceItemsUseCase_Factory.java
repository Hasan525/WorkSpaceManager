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
public final class GetWorkspaceItemsUseCase_Factory implements Factory<GetWorkspaceItemsUseCase> {
  private final Provider<WorkspaceRepository> repositoryProvider;

  public GetWorkspaceItemsUseCase_Factory(Provider<WorkspaceRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetWorkspaceItemsUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetWorkspaceItemsUseCase_Factory create(
      Provider<WorkspaceRepository> repositoryProvider) {
    return new GetWorkspaceItemsUseCase_Factory(repositoryProvider);
  }

  public static GetWorkspaceItemsUseCase newInstance(WorkspaceRepository repository) {
    return new GetWorkspaceItemsUseCase(repository);
  }
}
