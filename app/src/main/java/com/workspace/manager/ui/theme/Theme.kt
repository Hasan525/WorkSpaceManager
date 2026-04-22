package com.workspace.manager.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary          = Purple80,
    onPrimary        = Purple10,
    primaryContainer = Purple20,
    secondary        = Teal40,
    onSecondary      = Grey10,
    background       = Grey10,
    surface          = Grey15,
    onBackground     = Grey90,
    onSurface        = Grey90,
    error            = ConflictRed
)

private val LightColorScheme = lightColorScheme(
    primary          = Purple40,
    onPrimary        = White,
    primaryContainer = Purple90,
    secondary        = Teal40,
    onSecondary      = Grey10,
    background       = Grey95,
    surface          = SurfaceCardLight,
    onBackground     = Grey10,
    onSurface        = Grey10,
    error            = ConflictRed
)

@Composable
fun WorkspaceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography  = WorkspaceTypography,
        content     = content
    )
}
