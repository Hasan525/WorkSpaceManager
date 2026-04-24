package com.workspace.manager.data.sync;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.workspace.manager.domain.repository.WorkspaceRepository;
import dagger.internal.DaggerGenerated;
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
public final class SyncWorker_Factory {
  private final Provider<WorkspaceRepository> repositoryProvider;

  public SyncWorker_Factory(Provider<WorkspaceRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  public SyncWorker get(Context context, WorkerParameters workerParams) {
    return newInstance(context, workerParams, repositoryProvider.get());
  }

  public static SyncWorker_Factory create(Provider<WorkspaceRepository> repositoryProvider) {
    return new SyncWorker_Factory(repositoryProvider);
  }

  public static SyncWorker newInstance(Context context, WorkerParameters workerParams,
      WorkspaceRepository repository) {
    return new SyncWorker(context, workerParams, repository);
  }
}
