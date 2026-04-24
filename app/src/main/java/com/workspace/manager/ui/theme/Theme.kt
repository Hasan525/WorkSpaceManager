package com.workspace.manager.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary              = Forest,
    onPrimary            = TextPrimary,
    primaryContainer     = ForestDeep,
    onPrimaryContainer   = ForestLight,
    secondary            = ForestLight,
    onSecondary          = BgBase,
    secondaryContainer   = ForestDeep,
    onSecondaryContainer = ForestLight,
    tertiary             = TextSecondary,
    onTertiary           = BgBase,
    background           = BgBase,
    onBackground         = TextPrimary,
    surface              = BgSurface,
    onSurface            = TextPrimary,
    surfaceVariant       = BgElevated,
    onSurfaceVariant     = TextSecondary,
    surfaceTint          = Forest,
    inverseSurface       = TextPrimary,
    inverseOnSurface     = BgBase,
    outline              = BorderStrong,
    outlineVariant       = BorderSubtle,
    error                = StatusRed,
    onError              = TextPrimary,
    errorContainer       = Color(0xFF3D0F11),
    onErrorContainer     = StatusRed,
    scrim                = Color(0xCC000000)
)

@Composable
fun WorkspaceTheme(content: @Composable () -> Unit) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = false
                isAppearanceLightNavigationBars = false
            }
        }
    }
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography  = WorkspaceTypography,
        content     = content
    )
}
