package com.workspace.manager.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assets")
data class AssetEntity(
    @PrimaryKey val id: String,
    val downloadUrl: String,
    val localUri: String? = null,
    /** Base64-encoded JPEG bytes — cached locally so we don't re-fetch from Firestore on cold start. */
    val imageData: String? = null,
    val rotationAngle: Float = 0f,
    val sortOrder: Long,
    val createdAt: Long,
    val updatedAt: Long,
    val isPendingSync: Boolean = false
)
