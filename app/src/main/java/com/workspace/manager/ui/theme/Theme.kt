package com.workspace.manager.ui.theme

import android.app.Activity
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Always-dark colour scheme — marketplace apps don't flip to light
private val DarkColorScheme = darkColorScheme(
    primary             = Violet,
    onPrimary           = NeutralWhite,
    primaryContainer    = VioletDeep,
    onPrimaryContainer  = VioletLight,
    secondary           = Mint,
    onSecondary         = BgBase,
    secondaryContainer  = MintDeep,
    onSecondaryContainer= Mint,
    background          = BgBase,
    onBackground        = NeutralWhite,
    surface             = BgSurface,
    onSurface           = NeutralWhite,
    surfaceVariant      = BgElevated,
    onSurfaceVariant    = NeutralText,
    outline             = NeutralBorder,
    outlineVariant      = NeutralBorder,
    error               = StatusRed,
    onError             = NeutralWhite,
    errorContainer      = Color(0xFF3B0D0D),
    onErrorContainer    = StatusRed,
    scrim               = BgBase
)

@Composable
fun WorkspaceTheme(content: @Composable () -> Unit) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = BgBase.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography  = WorkspaceTypography,
        content     = content
    )
}
