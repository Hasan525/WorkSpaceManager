package com.workspace.manager.data.model

data class AssetDto(
    val id: String = "",
    val downloadUrl: String = "",
    /** Base64-encoded JPEG bytes — image data lives in this field instead of Storage. */
    val imageData: String = "",
    val rotationAngle: Float = 0f,
    val sortOrder: Long = 0L,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)
