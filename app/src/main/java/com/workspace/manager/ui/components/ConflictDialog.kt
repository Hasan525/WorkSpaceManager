package com.workspace.manager.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.workspace.manager.domain.model.ConflictInfo
import com.workspace.manager.domain.model.ConflictResolution
import com.workspace.manager.ui.theme.ConflictRed
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ConflictDialog(
    conflict: ConflictInfo,
    onResolve: (ConflictResolution) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                // Header
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = null,
                        tint = ConflictRed,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = "Conflict Detected",
                        style = MaterialTheme.typography.titleLarge,
                        color = ConflictRed
                    )
                }

                Spacer(Modifier.height(16.dp))
                Text(
                    text = "This note was modified both locally and remotely. Choose which version to keep.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                Spacer(Modifier.height(20.dp))

                // Local version
                VersionCard(
                    label = "📱 Local Version",
                    content = conflict.localContent,
                    timestamp = conflict.localUpdatedAt,
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )

                Spacer(Modifier.height(12.dp))

                // Remote version
                VersionCard(
                    label = "☁️ Remote Version",
                    content = conflict.remoteContent,
                    timestamp = conflict.remoteUpdatedAt,
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )

                Spacer(Modifier.height(24.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { onResolve(ConflictResolution.KEEP_REMOTE) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Keep Remote")
                    }
                    Button(
                        onClick = { onResolve(ConflictResolution.KEEP_LOCAL) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Keep Local")
                    }
                }
            }
        }
    }
}

@Composable
private fun VersionCard(
    label: String,
    content: String,
    timestamp: Long,
    containerColor: androidx.compose.ui.graphics.Color
) {
    val formatted = SimpleDateFormat("MMM d, HH:mm:ss", Locale.getDefault()).format(Date(timestamp))
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(containerColor, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = content.ifBlank { "(empty)" },
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = formatted,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
        )
    }
}
