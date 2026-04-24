package com.workspace.manager.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.workspace.manager.domain.model.ConflictInfo
import com.workspace.manager.domain.model.ConflictResolution
import com.workspace.manager.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

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
        Box(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .background(BgElevated, RoundedCornerShape(20.dp))
                .padding(24.dp)
        ) {
            Column {
                // ── Header ────────────────────────────────────────────────────
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(StatusRed.copy(alpha = 0.15f), RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = null,
                            tint = StatusRed,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "Conflict Detected",
                            style = MaterialTheme.typography.titleLarge,
                            color = NeutralWhite
                        )
                        Text(
                            text = "Choose which version to keep",
                            style = MaterialTheme.typography.bodySmall,
                            color = NeutralText
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))
                HorizontalDivider(color = NeutralBorder)
                Spacer(Modifier.height(20.dp))

                // ── Version cards ─────────────────────────────────────────────
                VersionCard(
                    label = "📱  Local Version",
                    labelColor = Violet,
                    content = conflict.localContent,
                    timestamp = conflict.localUpdatedAt,
                    bgColor = VioletDeep.copy(alpha = 0.5f)
                )

                Spacer(Modifier.height(10.dp))

                VersionCard(
                    label = "☁️  Remote Version",
                    labelColor = Mint,
                    content = conflict.remoteContent,
                    timestamp = conflict.remoteUpdatedAt,
                    bgColor = MintDeep.copy(alpha = 0.5f)
                )

                Spacer(Modifier.height(24.dp))

                // ── Action buttons ────────────────────────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Keep Remote
                    OutlinedButton(
                        onClick = { onResolve(ConflictResolution.KEEP_REMOTE) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Mint),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Mint.copy(alpha = 0.5f))
                    ) {
                        Text("Keep Remote", style = MaterialTheme.typography.labelLarge)
                    }

                    // Keep Local
                    Button(
                        onClick = { onResolve(ConflictResolution.KEEP_LOCAL) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Violet,
                            contentColor   = NeutralWhite
                        )
                    ) {
                        Text("Keep Local", style = MaterialTheme.typography.labelLarge)
                    }
                }

                Spacer(Modifier.height(8.dp))

                // Merge — full-width
                FilledTonalButton(
                    onClick = { onResolve(ConflictResolution.MERGE) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = BgHighlight,
                        contentColor   = NeutralText
                    )
                ) {
                    Text("🔀  Merge Both Versions", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

@Composable
private fun VersionCard(
    label: String,
    labelColor: Color,
    content: String,
    timestamp: Long,
    bgColor: Color
) {
    val sdf = remember { SimpleDateFormat("MMM d, HH:mm:ss", Locale.getDefault()) }
    val formatted = remember(timestamp) { sdf.format(Date(timestamp)) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor, RoundedCornerShape(12.dp))
            .padding(14.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = labelColor
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = content.ifBlank { "(empty)" },
            style = MaterialTheme.typography.bodyMedium,
            color = NeutralWhite,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = formatted,
            style = MaterialTheme.typography.labelSmall,
            color = NeutralMuted
        )
    }
}
