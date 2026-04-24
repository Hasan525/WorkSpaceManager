package com.workspace.manager.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Single source of truth for spacing, radius, icon, border, and component sizes.
 * Every screen / component pulls dimensions from here so the design stays in step.
 */
object Dim {
    // Spacing scale — 4dp grid
    val Space2  = 2.dp
    val Space4  = 4.dp
    val Space6  = 6.dp
    val Space8  = 8.dp
    val Space12 = 12.dp
    val Space16 = 16.dp
    val Space20 = 20.dp
    val Space24 = 24.dp
    val Space32 = 32.dp
    val Space40 = 40.dp

    // Corner radius scale
    val RadiusXs = 6.dp     // chips, micro pills
    val RadiusSm = 8.dp     // small surfaces, mini-fab labels
    val RadiusMd = 12.dp    // buttons, snackbars, HUD
    val RadiusLg = 16.dp    // cards, dialogs (small)
    val RadiusXl = 20.dp    // dialogs (large), prominent surfaces

    // Icon sizes
    val IconXs = 12.dp
    val IconSm = 16.dp
    val IconMd = 20.dp
    val IconLg = 24.dp

    // Border thickness
    val BorderHair  = 0.5.dp
    val BorderThin  = 1.dp
    val BorderThick = 2.dp

    // Component sizes
    val FabSize        = 56.dp
    val MiniFabSize    = 40.dp
    val IconButtonSize = 40.dp
    val SaveBtnSize    = 44.dp

    // Layout
    val ContentMaxWidth      = 720.dp   // cap reading column on tablets
    val DialogMaxWidth       = 480.dp   // cap dialog width on tablets
    val TabletWidthBreakpoint = 600.dp  // window width threshold
    val GridMinTileWidth     = 170.dp   // staggered-grid adaptive cell width

    // Screen-edge horizontal padding — same everywhere
    val ScreenEdge = Space20
}

/** True when the current window width is at least the tablet breakpoint. */
@Composable
@ReadOnlyComposable
fun isExpandedWidth(): Boolean {
    val widthDp: Dp = LocalConfiguration.current.screenWidthDp.dp
    return widthDp >= Dim.TabletWidthBreakpoint
}
