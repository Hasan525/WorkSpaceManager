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
public final class UploadAssetUseCase_Factory implements Factory<UploadAssetUseCase> {
  private final Provider<WorkspaceRepository> repositoryProvider;

  public UploadAssetUseCase_Factory(Provider<WorkspaceRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public UploadAssetUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static UploadAssetUseCase_Factory create(
      Provider<WorkspaceRepository> repositoryProvider) {
    return new UploadAssetUseCase_Factory(repositoryProvider);
  }

  public static UploadAssetUseCase newInstance(WorkspaceRepository repository) {
    return new UploadAssetUseCase(repository);
  }
}
