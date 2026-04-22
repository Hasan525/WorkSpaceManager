package com.workspace.manager.data.model

data class NoteDto(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val sortOrder: Long = 0L,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)
