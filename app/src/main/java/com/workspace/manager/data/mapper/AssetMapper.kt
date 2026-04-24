package com.workspace.manager.data.mapper

import com.workspace.manager.data.local.AssetEntity
import com.workspace.manager.data.model.AssetDto
import com.workspace.manager.domain.model.Asset

fun AssetEntity.toDomain(): Asset = Asset(
    id = id,
    downloadUrl = downloadUrl,
    localUri = localUri,
    imageData = imageData,
    rotationAngle = rotationAngle,
    sortOrder = sortOrder,
    createdAt = createdAt,
    updatedAt = updatedAt,
    isPendingSync = isPendingSync
)

fun Asset.toEntity(): AssetEntity = AssetEntity(
    id = id,
    downloadUrl = downloadUrl,
    localUri = localUri,
    imageData = imageData,
    rotationAngle = rotationAngle,
    sortOrder = sortOrder,
    createdAt = createdAt,
    updatedAt = updatedAt,
    isPendingSync = isPendingSync
)

fun Asset.toDto(): AssetDto = AssetDto(
    id = id,
    downloadUrl = downloadUrl,
    imageData = imageData ?: "",
    rotationAngle = rotationAngle,
    sortOrder = sortOrder,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun AssetDto.toEntity(): AssetEntity = AssetEntity(
    id = id,
    downloadUrl = downloadUrl,
    imageData = imageData.ifBlank { null },
    rotationAngle = rotationAngle,
    sortOrder = sortOrder,
    createdAt = createdAt,
    updatedAt = updatedAt
)
