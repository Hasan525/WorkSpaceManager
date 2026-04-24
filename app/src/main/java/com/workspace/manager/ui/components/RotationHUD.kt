package com.workspace.manager.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.RotateRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.workspace.manager.ui.theme.BorderStrong
import com.workspace.manager.ui.theme.Dim
import com.workspace.manager.ui.theme.ForestLight
import com.workspace.manager.ui.theme.TextPrimary
import kotlin.math.roundToInt

@Composable
fun RotationHUD(
    angleDegrees: Float,
    modifier: Modifier = Modifier
) {
    val normalised = ((angleDegrees % 360) + 360) % 360

    Row(
        modifier = modifier
            .padding(Dim.Space8)
            .background(Color(0xE61C1C1C), RoundedCornerShape(Dim.RadiusMd))
            .border(Dim.BorderThin, BorderStrong, RoundedCornerShape(Dim.RadiusMd))
            .padding(horizontal = Dim.Space12, vertical = Dim.Space8),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dim.Space6)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.RotateRight,
            contentDescription = "Rotation",
            tint = ForestLight,
            modifier = Modifier.size(Dim.IconSm)
        )
        Text(
            text = "${normalised.roundToInt()}°",
            style = MaterialTheme.typography.labelMedium,
            color = TextPrimary
        )
    }
}
