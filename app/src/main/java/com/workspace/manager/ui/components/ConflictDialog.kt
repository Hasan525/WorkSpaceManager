package com.workspace.manager.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.workspace.manager.domain.model.ConflictInfo
import com.workspace.manager.domain.model.ConflictResolution
import com.workspace.manager.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ConflictDialog(
    conflict: ConflictInfo,
    onResolve: (ConflictResolution) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .widthIn(max = Dim.DialogMaxWidth)
                .background(BgElevated, RoundedCornerShape(Dim.RadiusXl))
                .border(Dim.BorderThin, BorderSubtle, RoundedCornerShape(Dim.RadiusXl))
                .padding(Dim.Space24)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(Dim.IconButtonSize)
                        .background(StatusRed.copy(alpha = 0.12f), RoundedCornerShape(Dim.RadiusMd)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = null,
                        tint = StatusRed,
                        modifier = Modifier.size(Dim.IconMd)
                    )
                }
                Spacer(Modifier.width(Dim.Space12))
                Column {
                    Text(
                        text = "Conflict detected",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary
                    )
                    Text(
                        text = "Choose which version to keep",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }

            Spacer(Modifier.height(Dim.Space20))
            HorizontalDivider(color = BorderSubtle)
            Spacer(Modifier.height(Dim.Space20))

            VersionCard(
                icon = Icons.Default.Smartphone,
                label = "On this device",
                content = conflict.localContent,
                timestamp = conflict.localUpdatedAt
            )

            Spacer(Modifier.height(Dim.Space8))

            VersionCard(
                icon = Icons.Default.CloudQueue,
                label = "From cloud",
                content = conflict.remoteContent,
                timestamp = conflict.remoteUpdatedAt
            )

            Spacer(Modifier.height(Dim.Space24))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dim.Space8)
            ) {
                OutlinedButton(
                    onClick = { onResolve(ConflictResolution.KEEP_REMOTE) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
                    border = BorderStroke(Dim.BorderThin, BorderStrong),
                    shape = RoundedCornerShape(Dim.RadiusMd)
                ) {
                    Text("Keep cloud", style = MaterialTheme.typography.labelLarge)
                }

                Button(
                    onClick = { onResolve(ConflictResolution.KEEP_LOCAL) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Forest,
                        contentColor   = TextPrimary
                    ),
                    shape = RoundedCornerShape(Dim.RadiusMd)
                ) {
                    Text("Keep local", style = MaterialTheme.typography.labelLarge)
                }
            }

            Spacer(Modifier.height(Dim.Space8))

            FilledTonalButton(
                onClick = { onResolve(ConflictResolution.MERGE) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = BgHighlight,
                    contentColor   = TextPrimary
                ),
                shape = RoundedCornerShape(Dim.RadiusMd)
            ) {
                Text("Merge both versions", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
private fun VersionCard(
    icon: ImageVector,
    label: String,
    content: String,
    timestamp: Long
) {
    val formatted = remember(timestamp) {
        SimpleDateFormat("MMM d · HH:mm", Locale.getDefault()).format(Date(timestamp))
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BgSurface, RoundedCornerShape(Dim.RadiusMd))
            .border(Dim.BorderThin, BorderSubtle, RoundedCornerShape(Dim.RadiusMd))
            .padding(Dim.Space12)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dim.Space6)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = ForestLight,
                modifier = Modifier.size(Dim.IconXs)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = ForestLight
            )
        }
        Spacer(Modifier.height(Dim.Space8))
        Text(
            text = content.ifBlank { "(empty)" },
            style = MaterialTheme.typography.bodyMedium,
            color = if (content.isBlank()) TextMuted else TextPrimary,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.height(Dim.Space8))
        Text(
            text = formatted,
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted
        )
    }
}
