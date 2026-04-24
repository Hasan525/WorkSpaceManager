package com.workspace.manager.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.workspace.manager.domain.model.Note
import com.workspace.manager.ui.theme.*

@Composable
fun NoteTile(
    note: Note,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Pick a consistent accent colour for this note using its id hash
    val accentColor = remember(note.id) {
        NoteAccentColors[Math.abs(note.id.hashCode()) % NoteAccentColors.size]
    }

    val borderColor by animateColorAsState(
        targetValue = when {
            note.isConflicted -> StatusRed
            note.isPendingSync -> StatusAmber
            else -> NeutralBorder
        },
        animationSpec = tween(300),
        label = "borderColor"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(BgSurface)
            .clickable { onClick() }
    ) {
        // Left accent strip
        Box(
            modifier = Modifier
                .width(4.dp)
                .fillMaxHeight()
                .background(accentColor, RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
        )

        // Status border glow — drawn as a subtle outline when conflicted / syncing
        if (note.isConflicted || note.isPendingSync) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        borderColor.copy(alpha = 0.06f),
                        RoundedCornerShape(16.dp)
                    )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 14.dp, top = 14.dp, bottom = 12.dp)
        ) {
            // Title row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = note.title.ifBlank { "Untitled Note" },
                    style = MaterialTheme.typography.titleMedium,
                    color = NeutralWhite,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                if (note.isConflicted || note.isPendingSync) {
                    Spacer(Modifier.width(8.dp))
                    StatusChip(isConflicted = note.isConflicted)
                }
            }

            // Content preview
            if (note.content.isNotBlank()) {
                Spacer(Modifier.height(6.dp))
                Text(
                    text = note.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = NeutralText,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(Modifier.height(10.dp))

            // Footer — timestamp
            val sdf = remember { java.text.SimpleDateFormat("MMM d, HH:mm", java.util.Locale.getDefault()) }
            val formattedTime = remember(note.updatedAt) { sdf.format(java.util.Date(note.updatedAt)) }
            Text(
                text = formattedTime,
                style = MaterialTheme.typography.labelSmall,
                color = NeutralMuted
            )
        }
    }
}

@Composable
private fun StatusChip(isConflicted: Boolean) {
    val chipColor = if (isConflicted) StatusRed else StatusAmber
    val chipIcon  = if (isConflicted) Icons.Default.Warning else Icons.Default.Sync
    val chipLabel = if (isConflicted) "Conflict" else "Sync"

    Row(
        modifier = Modifier
            .background(chipColor.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Icon(
            imageVector = chipIcon,
            contentDescription = chipLabel,
            tint = chipColor,
            modifier = Modifier.size(12.dp)
        )
        Text(
            text = chipLabel,
            style = MaterialTheme.typography.labelSmall,
            color = chipColor
        )
    }
}
