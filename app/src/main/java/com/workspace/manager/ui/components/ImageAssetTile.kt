package com.workspace.manager.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import android.util.Base64
import coil.compose.AsyncImage
import com.workspace.manager.domain.model.Asset
import com.workspace.manager.ui.theme.*
import kotlin.math.atan2

@Composable
fun ImageAssetTile(
    asset: Asset,
    isSelected: Boolean,
    onClick: () -> Unit,
    onRotationChanged: (Float) -> Unit,
    onDeleteRequested: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentAngle by rememberSaveable(asset.id) { mutableFloatStateOf(asset.rotationAngle) }
    // Sentinel: NaN = no in-flight rotation; any other value = a partial gesture that
    // was interrupted before onRotationChanged could fire. Survives process death.
    var pendingCommit by rememberSaveable(asset.id) { mutableFloatStateOf(Float.NaN) }
    var showHUD by remember { mutableStateOf(false) }
    var hudAngle by remember { mutableFloatStateOf(0f) }
    var isRotating by remember { mutableStateOf(false) }

    LaunchedEffect(asset.id) {
        if (!pendingCommit.isNaN()) {
            currentAngle = pendingCommit
            onRotationChanged(pendingCommit)
            pendingCommit = Float.NaN
        }
    }

    LaunchedEffect(asset.rotationAngle) {
        if (!isRotating && pendingCommit.isNaN()) {
            currentAngle = asset.rotationAngle
        }
    }

    val animatedRotation by animateFloatAsState(
        targetValue   = currentAngle,
        animationSpec = spring(dampingRatio = 0.8f),
        label         = "rotation"
    )

    val isActive = isSelected || isRotating

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(Dim.RadiusLg))
            .background(BgSurface)
            .border(
                width = if (isActive) Dim.BorderThick else Dim.BorderThin,
                color = if (isActive) Forest else BorderSubtle,
                shape = RoundedCornerShape(Dim.RadiusLg)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = remember(asset.id, asset.imageData, asset.localUri, asset.downloadUrl) {
                imageModelFor(asset)
            },
            contentDescription = "Asset Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .rotate(animatedRotation)
                .pointerInput(asset.id) {
                    awaitEachGesture {
                        awaitFirstDown(requireUnconsumed = false)

                        var prevAngle = 0f
                        var fingerCount: Int
                        var prevFingerCount = 0
                        var didRotate = false

                        do {
                            val event = awaitPointerEvent()
                            val pressed = event.changes.filter { it.pressed }
                            fingerCount = pressed.size

                            if (fingerCount >= 2 && prevFingerCount < 2) {
                                prevAngle = 0f
                            }
                            prevFingerCount = fingerCount

                            when {
                                fingerCount == 2 -> {
                                    isRotating = true
                                    didRotate = true
                                    val angle = angleBetween(pressed[0].position, pressed[1].position)
                                    if (prevAngle != 0f) currentAngle += angle - prevAngle
                                    prevAngle = angle
                                    hudAngle = currentAngle
                                    pendingCommit = currentAngle
                                    showHUD = false
                                    pressed.forEach { it.consume() }
                                }
                                fingerCount >= 3 -> {
                                    isRotating = true
                                    didRotate = true
                                    val angle = angleBetween(pressed[0].position, pressed[1].position)
                                    if (prevAngle != 0f) currentAngle += angle - prevAngle
                                    prevAngle = angle
                                    hudAngle = currentAngle
                                    pendingCommit = currentAngle
                                    showHUD = true
                                    pressed.forEach { it.consume() }
                                }
                            }
                        } while (pressed.any { it.pressed })

                        showHUD = false
                        isRotating = false
                        if (didRotate) {
                            onRotationChanged(currentAngle)
                            pendingCommit = Float.NaN
                        }
                    }
                }
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color(0xCC0A0A0A))
                    )
                )
        )

        if (isActive) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .border(
                        width = Dim.BorderThick,
                        color = ForestGlow,
                        shape = RoundedCornerShape(Dim.RadiusLg)
                    )
            )
        }

        if (showHUD) {
            RotationHUD(
                angleDegrees = hudAngle,
                modifier = Modifier.align(Alignment.TopEnd)
            )
        } else {
            AnimatedVisibility(
                visible = isSelected,
                enter = fadeIn() + scaleIn(initialScale = 0.7f),
                exit  = fadeOut() + scaleOut(targetScale = 0.7f),
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Box(
                    modifier = Modifier
                        .padding(Dim.Space8)
                        .size(Dim.MiniFabSize)
                        .clip(CircleShape)
                        .background(StatusRed)
                        .clickable(onClick = onDeleteRequested),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Delete image",
                        tint = TextPrimary,
                        modifier = Modifier.size(Dim.IconMd)
                    )
                }
            }
        }

        if (asset.isPendingSync) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(Dim.Space8)
                    .background(StatusAmber.copy(alpha = 0.15f), RoundedCornerShape(Dim.RadiusSm))
                    .padding(horizontal = Dim.Space8, vertical = Dim.Space4),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dim.Space6)
            ) {
                Box(
                    modifier = Modifier
                        .size(Dim.Space6)
                        .background(StatusAmber, RoundedCornerShape(Dim.Space2))
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

private fun imageModelFor(asset: Asset): Any? = when {
    !asset.imageData.isNullOrBlank() -> runCatching {
        Base64.decode(asset.imageData, Base64.NO_WRAP)
    }.getOrNull() ?: asset.localUri ?: asset.downloadUrl.takeIf { it.isNotBlank() }
    !asset.localUri.isNullOrBlank() -> asset.localUri
    asset.downloadUrl.isNotBlank() -> asset.downloadUrl
    else -> null
}
