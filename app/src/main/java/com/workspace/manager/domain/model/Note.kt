package com.workspace.manager.domain.model

data class Note(
    val id: String,
    val title: String,
    val content: String,
    val sortOrder: Long,
    val createdAt: Long,
    val updatedAt: Long,
    val isPendingSync: Boolean = false,
    val isConflicted: Boolean = false,
    val remoteContent: String? = null,
    val remoteUpdatedAt: Long = 0L
)
