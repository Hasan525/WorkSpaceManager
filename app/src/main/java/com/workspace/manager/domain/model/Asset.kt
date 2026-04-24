package com.workspace.manager.domain.model

data class Asset(
    val id: String,
    val downloadUrl: String,
    val localUri: String? = null,
    val imageData: String? = null,
    val rotationAngle: Float = 0f,
    val sortOrder: Long,
    val createdAt: Long,
    val updatedAt: Long,
    val isPendingSync: Boolean = false
)
