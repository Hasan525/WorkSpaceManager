package com.workspace.manager;

import androidx.hilt.work.HiltWorkerFactory;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class WorkspaceApp_MembersInjector implements MembersInjector<WorkspaceApp> {
  private final Provider<HiltWorkerFactory> workerFactoryProvider;

  public WorkspaceApp_MembersInjector(Provider<HiltWorkerFactory> workerFactoryProvider) {
    this.workerFactoryProvider = workerFactoryProvider;
  }

  public static MembersInjector<WorkspaceApp> create(
      Provider<HiltWorkerFactory> workerFactoryProvider) {
    return new WorkspaceApp_MembersInjector(workerFactoryProvider);
  }

  @Override
  public void injectMembers(WorkspaceApp instance) {
    injectWorkerFactory(instance, workerFactoryProvider.get());
  }

  @InjectedFieldSignature("com.workspace.manager.WorkspaceApp.workerFactory")
  public static void injectWorkerFactory(WorkspaceApp instance, HiltWorkerFactory workerFactory) {
    instance.workerFactory = workerFactory;
  }
}
