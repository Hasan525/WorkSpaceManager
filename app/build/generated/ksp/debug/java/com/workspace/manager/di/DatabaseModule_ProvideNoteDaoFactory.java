package com.workspace.manager.di;

import com.workspace.manager.data.local.NoteDao;
import com.workspace.manager.data.local.WorkspaceDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideNoteDaoFactory implements Factory<NoteDao> {
  private final Provider<WorkspaceDatabase> dbProvider;

  public DatabaseModule_ProvideNoteDaoFactory(Provider<WorkspaceDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public NoteDao get() {
    return provideNoteDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideNoteDaoFactory create(
      Provider<WorkspaceDatabase> dbProvider) {
    return new DatabaseModule_ProvideNoteDaoFactory(dbProvider);
  }

  public static NoteDao provideNoteDao(WorkspaceDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideNoteDao(db));
  }
}
