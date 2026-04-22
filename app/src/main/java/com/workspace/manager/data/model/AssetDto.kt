package com.workspace.manager.data.model

data class AssetDto(
    val id: String = "",
    val downloadUrl: String = "",
    val rotationAngle: Float = 0f,
    val sortOrder: Long = 0L,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)
