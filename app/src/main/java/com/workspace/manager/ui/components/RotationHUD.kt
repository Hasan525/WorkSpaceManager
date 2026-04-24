package com.workspace.manager.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.workspace.manager.ui.theme.NeutralWhite
import com.workspace.manager.ui.theme.VioletLight
import kotlin.math.roundToInt

/**
 * HUD overlay shown when a 3rd finger is detected during image rotation.
 * Glassmorphism dark pill displaying the current rotation angle.
 */
@Composable
fun RotationHUD(
    angleDegrees: Float,
    modifier: Modifier = Modifier
) {
    val normalised = ((angleDegrees % 360) + 360) % 360

    Row(
        modifier = modifier
            .padding(8.dp)
            .background(
                color = Color(0xCC13131E),   // ~80 % opacity dark surface = glassmorphism feel
                shape = RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Icon(
            imageVector = Icons.Default.RotateRight,
            contentDescription = "Rotation",
            tint = VioletLight,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = "${normalised.roundToInt()}°",
            style = MaterialTheme.typography.labelMedium,
            color = NeutralWhite
        )
    }
}
