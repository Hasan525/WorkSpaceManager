package com.workspace.manager.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.style.TextOverflow
import com.workspace.manager.domain.model.Note
import com.workspace.manager.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs

@Composable
fun NoteTile(
    note: Note,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accentColor = remember(note.id) {
        NoteAccentColors[abs(note.id.hashCode()) % NoteAccentColors.size]
    }

    val borderColor by animateColorAsState(
        targetValue = when {
            note.isConflicted -> StatusRed.copy(alpha = 0.55f)
            note.isPendingSync -> StatusAmber.copy(alpha = 0.45f)
            else              -> BorderSubtle
        },
        animationSpec = tween(300),
        label = "borderColor"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Dim.RadiusLg))
            .background(BgSurface)
            .border(Dim.BorderThin, borderColor, RoundedCornerShape(Dim.RadiusLg))
            .clickable { onClick() }
            .padding(Dim.Space16)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(Dim.Space8)
                    .background(accentColor, RoundedCornerShape(Dim.Space2))
            )
            if (note.isConflicted || note.isPendingSync) {
                StatusChip(isConflicted = note.isConflicted)
            }
        }

        Spacer(Modifier.height(Dim.Space12))

        Text(
            text = note.title.ifBlank { "Untitled" },
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        if (note.content.isNotBlank()) {
            Spacer(Modifier.height(Dim.Space6))
            Text(
                text = note.content,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(Modifier.height(Dim.Space12))

        val formattedTime = remember(note.updatedAt) {
            SimpleDateFormat("MMM d · HH:mm", Locale.getDefault()).format(Date(note.updatedAt))
        }
        Text(
            text = formattedTime,
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted
        )
    }
}

@Composable
private fun StatusChip(isConflicted: Boolean) {
    val chipColor = if (isConflicted) StatusRed else StatusAmber
    val chipIcon  = if (isConflicted) Icons.Default.Warning else Icons.Default.Sync
    val chipLabel = if (isConflicted) "Conflict" else "Syncing"

    Row(
        modifier = Modifier
            .background(chipColor.copy(alpha = 0.12f), RoundedCornerShape(Dim.RadiusXs))
            .padding(horizontal = Dim.Space6, vertical = Dim.Space4),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dim.Space4)
    ) {
        Icon(
            imageVector = chipIcon,
            contentDescription = chipLabel,
            tint = chipColor,
            modifier = Modifier.size(Dim.IconXs)
        )
        Text(
            text = chipLabel,
            style = MaterialTheme.typography.labelSmall,
            color = chipColor
        )
    }
}
