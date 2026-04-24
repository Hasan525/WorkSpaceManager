package com.workspace.manager.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object Dim {
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

    val RadiusXs = 6.dp
    val RadiusSm = 8.dp
    val RadiusMd = 12.dp
    val RadiusLg = 16.dp
    val RadiusXl = 20.dp

    val IconXs = 12.dp
    val IconSm = 16.dp
    val IconMd = 20.dp
    val IconLg = 24.dp

    val BorderHair  = 0.5.dp
    val BorderThin  = 1.dp
    val BorderThick = 2.dp

    val FabSize        = 56.dp
    val MiniFabSize    = 40.dp
    val IconButtonSize = 40.dp
    val SaveBtnSize    = 44.dp

    val ContentMaxWidth      = 720.dp
    val DialogMaxWidth       = 480.dp
    val TabletWidthBreakpoint = 600.dp
    val GridMinTileWidth     = 170.dp

    val ScreenEdge = Space20
}

@Composable
@ReadOnlyComposable
fun isExpandedWidth(): Boolean {
    val widthDp: Dp = LocalConfiguration.current.screenWidthDp.dp
    return widthDp >= Dim.TabletWidthBreakpoint
}
