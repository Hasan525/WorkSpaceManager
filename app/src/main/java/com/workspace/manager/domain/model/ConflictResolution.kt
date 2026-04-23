package com.workspace.manager.domain.model

enum class ConflictResolution {
    KEEP_LOCAL,
    KEEP_REMOTE,
    /** Append remote content after local content, separated by a divider */
    MERGE
}
