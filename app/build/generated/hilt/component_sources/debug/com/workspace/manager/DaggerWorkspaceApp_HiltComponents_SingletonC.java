package com.workspace.manager;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.hilt.work.HiltWorkerFactory;
import androidx.hilt.work.WorkerAssistedFactory;
import androidx.hilt.work.WorkerFactoryModule_ProvideFactoryFactory;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.workspace.manager.data.local.AssetDao;
import com.workspace.manager.data.local.NetworkConnectivityObserver;
import com.workspace.manager.data.local.NoteDao;
import com.workspace.manager.data.local.WorkspaceDatabase;
import com.workspace.manager.data.remote.FirebaseStorageDataSource;
import com.workspace.manager.data.remote.FirestoreDataSource;
import com.workspace.manager.data.repository.WorkspaceRepositoryImpl;
import com.workspace.manager.data.sync.SyncWorker;
import com.workspace.manager.data.sync.SyncWorker_AssistedFactory;
import com.workspace.manager.di.DatabaseModule_ProvideAssetDaoFactory;
import com.workspace.manager.di.DatabaseModule_ProvideDatabaseFactory;
import com.workspace.manager.di.DatabaseModule_ProvideNoteDaoFactory;
import com.workspace.manager.di.FirebaseModule_ProvideFirebaseAuthFactory;
import com.workspace.manager.di.FirebaseModule_ProvideFirebaseStorageFactory;
import com.workspace.manager.di.FirebaseModule_ProvideFirestoreFactory;
import com.workspace.manager.di.NetworkModule_ProvideNetworkObserverFactory;
import com.workspace.manager.domain.usecase.DeleteNoteUseCase;
import com.workspace.manager.domain.usecase.GetWorkspaceItemsUseCase;
import com.workspace.manager.domain.usecase.ReorderItemsUseCase;
import com.workspace.manager.domain.usecase.ResolveConflictUseCase;
import com.workspace.manager.domain.usecase.SaveNoteUseCase;
import com.workspace.manager.domain.usecase.UploadAssetUseCase;
import com.workspace.manager.ui.note.NoteEditorViewModel;
import com.workspace.manager.ui.note.NoteEditorViewModel_HiltModules;
import com.workspace.manager.ui.workspace.WorkspaceViewModel;
import com.workspace.manager.ui.workspace.WorkspaceViewModel_HiltModules;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.IdentifierNameString;
import dagger.internal.KeepFieldType;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.SingleCheck;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

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
public final class DaggerWorkspaceApp_HiltComponents_SingletonC {
  private DaggerWorkspaceApp_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public WorkspaceApp_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements WorkspaceApp_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public WorkspaceApp_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements WorkspaceApp_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public WorkspaceApp_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements WorkspaceApp_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public WorkspaceApp_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements WorkspaceApp_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public WorkspaceApp_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements WorkspaceApp_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public WorkspaceApp_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements WorkspaceApp_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public WorkspaceApp_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements WorkspaceApp_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public WorkspaceApp_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends WorkspaceApp_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends WorkspaceApp_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends WorkspaceApp_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends WorkspaceApp_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(ImmutableMap.<String, Boolean>of(LazyClassKeyProvider.com_workspace_manager_ui_note_NoteEditorViewModel, NoteEditorViewModel_HiltModules.KeyModule.provide(), LazyClassKeyProvider.com_workspace_manager_ui_workspace_WorkspaceViewModel, WorkspaceViewModel_HiltModules.KeyModule.provide()));
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_workspace_manager_ui_note_NoteEditorViewModel = "com.workspace.manager.ui.note.NoteEditorViewModel";

      static String com_workspace_manager_ui_workspace_WorkspaceViewModel = "com.workspace.manager.ui.workspace.WorkspaceViewModel";

      @KeepFieldType
      NoteEditorViewModel com_workspace_manager_ui_note_NoteEditorViewModel2;

      @KeepFieldType
      WorkspaceViewModel com_workspace_manager_ui_workspace_WorkspaceViewModel2;
    }
  }

  private static final class ViewModelCImpl extends WorkspaceApp_HiltComponents.ViewModelC {
    private final SavedStateHandle savedStateHandle;

    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<NoteEditorViewModel> noteEditorViewModelProvider;

    private Provider<WorkspaceViewModel> workspaceViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.savedStateHandle = savedStateHandleParam;
      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    private SaveNoteUseCase saveNoteUseCase() {
      return new SaveNoteUseCase(singletonCImpl.workspaceRepositoryImplProvider.get());
    }

    private DeleteNoteUseCase deleteNoteUseCase() {
      return new DeleteNoteUseCase(singletonCImpl.workspaceRepositoryImplProvider.get());
    }

    private GetWorkspaceItemsUseCase getWorkspaceItemsUseCase() {
      return new GetWorkspaceItemsUseCase(singletonCImpl.workspaceRepositoryImplProvider.get());
    }

    private UploadAssetUseCase uploadAssetUseCase() {
      return new UploadAssetUseCase(singletonCImpl.workspaceRepositoryImplProvider.get());
    }

    private ReorderItemsUseCase reorderItemsUseCase() {
      return new ReorderItemsUseCase(singletonCImpl.workspaceRepositoryImplProvider.get());
    }

    private ResolveConflictUseCase resolveConflictUseCase() {
      return new ResolveConflictUseCase(singletonCImpl.workspaceRepositoryImplProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.noteEditorViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.workspaceViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(ImmutableMap.<String, javax.inject.Provider<ViewModel>>of(LazyClassKeyProvider.com_workspace_manager_ui_note_NoteEditorViewModel, ((Provider) noteEditorViewModelProvider), LazyClassKeyProvider.com_workspace_manager_ui_workspace_WorkspaceViewModel, ((Provider) workspaceViewModelProvider)));
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return ImmutableMap.<Class<?>, Object>of();
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_workspace_manager_ui_workspace_WorkspaceViewModel = "com.workspace.manager.ui.workspace.WorkspaceViewModel";

      static String com_workspace_manager_ui_note_NoteEditorViewModel = "com.workspace.manager.ui.note.NoteEditorViewModel";

      @KeepFieldType
      WorkspaceViewModel com_workspace_manager_ui_workspace_WorkspaceViewModel2;

      @KeepFieldType
      NoteEditorViewModel com_workspace_manager_ui_note_NoteEditorViewModel2;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.workspace.manager.ui.note.NoteEditorViewModel 
          return (T) new NoteEditorViewModel(viewModelCImpl.savedStateHandle, singletonCImpl.workspaceRepositoryImplProvider.get(), viewModelCImpl.saveNoteUseCase(), viewModelCImpl.deleteNoteUseCase());

          case 1: // com.workspace.manager.ui.workspace.WorkspaceViewModel 
          return (T) new WorkspaceViewModel(viewModelCImpl.getWorkspaceItemsUseCase(), viewModelCImpl.saveNoteUseCase(), viewModelCImpl.deleteNoteUseCase(), viewModelCImpl.uploadAssetUseCase(), viewModelCImpl.reorderItemsUseCase(), viewModelCImpl.resolveConflictUseCase(), singletonCImpl.workspaceRepositoryImplProvider.get(), singletonCImpl.provideNetworkObserverProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends WorkspaceApp_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends WorkspaceApp_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends WorkspaceApp_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<WorkspaceDatabase> provideDatabaseProvider;

    private Provider<FirebaseFirestore> provideFirestoreProvider;

    private Provider<FirebaseAuth> provideFirebaseAuthProvider;

    private Provider<FirestoreDataSource> firestoreDataSourceProvider;

    private Provider<FirebaseStorage> provideFirebaseStorageProvider;

    private Provider<FirebaseStorageDataSource> firebaseStorageDataSourceProvider;

    private Provider<NetworkConnectivityObserver> provideNetworkObserverProvider;

    private Provider<WorkspaceRepositoryImpl> workspaceRepositoryImplProvider;

    private Provider<SyncWorker_AssistedFactory> syncWorker_AssistedFactoryProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private NoteDao noteDao() {
      return DatabaseModule_ProvideNoteDaoFactory.provideNoteDao(provideDatabaseProvider.get());
    }

    private AssetDao assetDao() {
      return DatabaseModule_ProvideAssetDaoFactory.provideAssetDao(provideDatabaseProvider.get());
    }

    private Map<String, javax.inject.Provider<WorkerAssistedFactory<? extends ListenableWorker>>> mapOfStringAndProviderOfWorkerAssistedFactoryOf(
        ) {
      return ImmutableMap.<String, javax.inject.Provider<WorkerAssistedFactory<? extends ListenableWorker>>>of("com.workspace.manager.data.sync.SyncWorker", ((Provider) syncWorker_AssistedFactoryProvider));
    }

    private HiltWorkerFactory hiltWorkerFactory() {
      return WorkerFactoryModule_ProvideFactoryFactory.provideFactory(mapOfStringAndProviderOfWorkerAssistedFactoryOf());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<WorkspaceDatabase>(singletonCImpl, 2));
      this.provideFirestoreProvider = DoubleCheck.provider(new SwitchingProvider<FirebaseFirestore>(singletonCImpl, 4));
      this.provideFirebaseAuthProvider = DoubleCheck.provider(new SwitchingProvider<FirebaseAuth>(singletonCImpl, 5));
      this.firestoreDataSourceProvider = DoubleCheck.provider(new SwitchingProvider<FirestoreDataSource>(singletonCImpl, 3));
      this.provideFirebaseStorageProvider = DoubleCheck.provider(new SwitchingProvider<FirebaseStorage>(singletonCImpl, 7));
      this.firebaseStorageDataSourceProvider = DoubleCheck.provider(new SwitchingProvider<FirebaseStorageDataSource>(singletonCImpl, 6));
      this.provideNetworkObserverProvider = DoubleCheck.provider(new SwitchingProvider<NetworkConnectivityObserver>(singletonCImpl, 8));
      this.workspaceRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<WorkspaceRepositoryImpl>(singletonCImpl, 1));
      this.syncWorker_AssistedFactoryProvider = SingleCheck.provider(new SwitchingProvider<SyncWorker_AssistedFactory>(singletonCImpl, 0));
    }

    @Override
    public void injectWorkspaceApp(WorkspaceApp workspaceApp) {
      injectWorkspaceApp2(workspaceApp);
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return ImmutableSet.<Boolean>of();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    @CanIgnoreReturnValue
    private WorkspaceApp injectWorkspaceApp2(WorkspaceApp instance) {
      WorkspaceApp_MembersInjector.injectWorkerFactory(instance, hiltWorkerFactory());
      return instance;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.workspace.manager.data.sync.SyncWorker_AssistedFactory 
          return (T) new SyncWorker_AssistedFactory() {
            @Override
            public SyncWorker create(Context context, WorkerParameters workerParams) {
              return new SyncWorker(context, workerParams, singletonCImpl.workspaceRepositoryImplProvider.get());
            }
          };

          case 1: // com.workspace.manager.data.repository.WorkspaceRepositoryImpl 
          return (T) new WorkspaceRepositoryImpl(singletonCImpl.noteDao(), singletonCImpl.assetDao(), singletonCImpl.firestoreDataSourceProvider.get(), singletonCImpl.firebaseStorageDataSourceProvider.get(), singletonCImpl.provideNetworkObserverProvider.get());

          case 2: // com.workspace.manager.data.local.WorkspaceDatabase 
          return (T) DatabaseModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 3: // com.workspace.manager.data.remote.FirestoreDataSource 
          return (T) new FirestoreDataSource(singletonCImpl.provideFirestoreProvider.get(), singletonCImpl.provideFirebaseAuthProvider.get());

          case 4: // com.google.firebase.firestore.FirebaseFirestore 
          return (T) FirebaseModule_ProvideFirestoreFactory.provideFirestore();

          case 5: // com.google.firebase.auth.FirebaseAuth 
          return (T) FirebaseModule_ProvideFirebaseAuthFactory.provideFirebaseAuth();

          case 6: // com.workspace.manager.data.remote.FirebaseStorageDataSource 
          return (T) new FirebaseStorageDataSource(singletonCImpl.provideFirebaseStorageProvider.get());

          case 7: // com.google.firebase.storage.FirebaseStorage 
          return (T) FirebaseModule_ProvideFirebaseStorageFactory.provideFirebaseStorage();

          case 8: // com.workspace.manager.data.local.NetworkConnectivityObserver 
          return (T) NetworkModule_ProvideNetworkObserverFactory.provideNetworkObserver(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
