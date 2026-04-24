package com.workspace.manager.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.workspace.manager.domain.model.Asset
import com.workspace.manager.ui.theme.*
import kotlin.math.atan2

@Composable
fun ImageAssetTile(
    asset: Asset,
    onRotationChanged: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentAngle by remember(asset.id) { mutableFloatStateOf(asset.rotationAngle) }
    var showHUD by remember { mutableStateOf(false) }
    var hudAngle by remember { mutableFloatStateOf(0f) }
    var isSelected by remember { mutableStateOf(false) }

    val animatedRotation by animateFloatAsState(
        targetValue = currentAngle,
        animationSpec = spring(dampingRatio = 0.8f),
        label = "rotation"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(BgSurface)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) Violet else NeutralBorder,
                shape = RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        // Full-bleed image with rotation
        AsyncImage(
            model = asset.localUri ?: asset.downloadUrl,
            contentDescription = "Asset Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .rotate(animatedRotation)
                .pointerInput(asset.id) {
                    awaitEachGesture {
                        awaitFirstDown(requireUnconsumed = false)

                        var prevAngle = 0f
                        var fingerCount = 0
                        var prevFingerCount = 0  // tracks transitions to reset prevAngle

                        do {
                            val event = awaitPointerEvent()
                            val pressed = event.changes.filter { it.pressed }
                            fingerCount = pressed.size

                            // Reset prevAngle when entering rotation mode from
                            // single-finger (or zero-finger) state.  Without this
                            // the first delta would be computed against a stale
                            // angle causing an unwanted rotation jump.
                            if (fingerCount >= 2 && prevFingerCount < 2) {
                                prevAngle = 0f
                            }
                            prevFingerCount = fingerCount

                            when {
                                fingerCount == 1 -> {
                                    // Finger 1: select / focus
                                    isSelected = true
                                    showHUD = false
                                    pressed.forEach { it.consume() }
                                }

                                fingerCount == 2 -> {
                                    // Finger 2: rotation transformation (no HUD yet)
                                    isSelected = true
                                    val angle = angleBetween(
                                        pressed[0].position,
                                        pressed[1].position
                                    )
                                    if (prevAngle != 0f) {
                                        currentAngle += angle - prevAngle
                                    }
                                    prevAngle = angle
                                    hudAngle = currentAngle
                                    showHUD = false
                                    pressed.forEach { it.consume() }
                                }

                                fingerCount >= 3 -> {
                                    // Finger 3: continue rotation AND display HUD
                                    isSelected = true
                                    val angle = angleBetween(
                                        pressed[0].position,
                                        pressed[1].position
                                    )
                                    if (prevAngle != 0f) {
                                        currentAngle += angle - prevAngle
                                    }
                                    prevAngle = angle
                                    hudAngle = currentAngle
                                    showHUD = true
                                    pressed.forEach { it.consume() }
                                }
                            }
                        } while (pressed.any { it.pressed })

                        // Gesture ended — persist final angle and hide HUD
                        showHUD = false
                        isSelected = false
                        if (fingerCount >= 2) {
                            onRotationChanged(currentAngle)
                        }
                    }
                }
        )

        // Gradient scrim at bottom — ensures badges / HUD are always readable
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color(0xCC0C0C14))
                    )
                )
        )

        // Selection glow ring (violet when selected)
        if (isSelected) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .border(
                        width = 2.dp,
                        color = VioletGlow,
                        shape = RoundedCornerShape(16.dp)
                    )
            )
        }

        // Rotation HUD — top-end corner
        if (showHUD) {
            RotationHUD(
                angleDegrees = hudAngle,
                modifier = Modifier.align(Alignment.TopEnd)
            )
        }

        // Pending-sync badge — bottom-start corner
        if (asset.isPendingSync) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
                    .background(
                        StatusAmber.copy(alpha = 0.18f),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 7.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "⏳",
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = "Syncing",
                    style = MaterialTheme.typography.labelSmall,
                    color = StatusAmber
                )
            }
        }
    }
}

private fun angleBetween(p1: Offset, p2: Offset): Float =
    Math.toDegrees(atan2((p2.y - p1.y).toDouble(), (p2.x - p1.x).toDouble())).toFloat()
