package com.workspace.manager.data.repository;

import com.workspace.manager.data.local.AssetDao;
import com.workspace.manager.data.local.NetworkConnectivityObserver;
import com.workspace.manager.data.local.NoteDao;
import com.workspace.manager.data.remote.FirebaseStorageDataSource;
import com.workspace.manager.data.remote.FirestoreDataSource;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class WorkspaceRepositoryImpl_Factory implements Factory<WorkspaceRepositoryImpl> {
  private final Provider<NoteDao> noteDaoProvider;

  private final Provider<AssetDao> assetDaoProvider;

  private final Provider<FirestoreDataSource> firestoreDataSourceProvider;

  private final Provider<FirebaseStorageDataSource> storageDataSourceProvider;

  private final Provider<NetworkConnectivityObserver> networkObserverProvider;

  public WorkspaceRepositoryImpl_Factory(Provider<NoteDao> noteDaoProvider,
      Provider<AssetDao> assetDaoProvider,
      Provider<FirestoreDataSource> firestoreDataSourceProvider,
      Provider<FirebaseStorageDataSource> storageDataSourceProvider,
      Provider<NetworkConnectivityObserver> networkObserverProvider) {
    this.noteDaoProvider = noteDaoProvider;
    this.assetDaoProvider = assetDaoProvider;
    this.firestoreDataSourceProvider = firestoreDataSourceProvider;
    this.storageDataSourceProvider = storageDataSourceProvider;
    this.networkObserverProvider = networkObserverProvider;
  }

  @Override
  public WorkspaceRepositoryImpl get() {
    return newInstance(noteDaoProvider.get(), assetDaoProvider.get(), firestoreDataSourceProvider.get(), storageDataSourceProvider.get(), networkObserverProvider.get());
  }

  public static WorkspaceRepositoryImpl_Factory create(Provider<NoteDao> noteDaoProvider,
      Provider<AssetDao> assetDaoProvider,
      Provider<FirestoreDataSource> firestoreDataSourceProvider,
      Provider<FirebaseStorageDataSource> storageDataSourceProvider,
      Provider<NetworkConnectivityObserver> networkObserverProvider) {
    return new WorkspaceRepositoryImpl_Factory(noteDaoProvider, assetDaoProvider, firestoreDataSourceProvider, storageDataSourceProvider, networkObserverProvider);
  }

  public static WorkspaceRepositoryImpl newInstance(NoteDao noteDao, AssetDao assetDao,
      FirestoreDataSource firestoreDataSource, FirebaseStorageDataSource storageDataSource,
      NetworkConnectivityObserver networkObserver) {
    return new WorkspaceRepositoryImpl(noteDao, assetDao, firestoreDataSource, storageDataSource, networkObserver);
  }
}
