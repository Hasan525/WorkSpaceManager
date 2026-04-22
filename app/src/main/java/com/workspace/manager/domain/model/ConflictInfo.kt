package com.workspace.manager.domain.model

data class ConflictInfo(
    val itemId: String,
    val localContent: String,
    val remoteContent: String,
    val localUpdatedAt: Long,
    val remoteUpdatedAt: Long
)
