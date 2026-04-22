package com.workspace.manager.domain.model

sealed class WorkspaceItem {
    abstract val id: String
    abstract val sortOrder: Long
    abstract val createdAt: Long

    data class NoteItem(val note: Note) : WorkspaceItem() {
        override val id = note.id
        override val sortOrder = note.sortOrder
        override val createdAt = note.createdAt
    }

    data class AssetItem(val asset: Asset) : WorkspaceItem() {
        override val id = asset.id
        override val sortOrder = asset.sortOrder
        override val createdAt = asset.createdAt
    }
}
