package com.workspace.manager.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [NoteEntity::class, AssetEntity::class],
    version = 2,
    exportSchema = false
)
abstract class WorkspaceDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun assetDao(): AssetDao

    companion object {
        const val DATABASE_NAME = "workspace_db"
    }
}
