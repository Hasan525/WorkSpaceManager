package com.workspace.manager.di

import android.content.Context
import androidx.room.Room
import com.workspace.manager.data.local.AssetDao
import com.workspace.manager.data.local.NoteDao
import com.workspace.manager.data.local.WorkspaceDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): WorkspaceDatabase =
        Room.databaseBuilder(context, WorkspaceDatabase::class.java, WorkspaceDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideNoteDao(db: WorkspaceDatabase): NoteDao = db.noteDao()

    @Provides
    fun provideAssetDao(db: WorkspaceDatabase): AssetDao = db.assetDao()
}
