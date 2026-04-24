package com.workspace.manager.ui.theme

import androidx.compose.ui.graphics.Color

// Backgrounds — true neutral charcoal stack
val BgBase      = Color(0xFF0A0A0A)
val BgSurface   = Color(0xFF141414)
val BgElevated  = Color(0xFF1C1C1C)
val BgHighlight = Color(0xFF242424)

// Accent — deep forest teal (#285A48) system
val Forest      = Color(0xFF285A48)   // primary accent — the requested deep forest teal
val ForestLight = Color(0xFF3D7E63)   // lighter forest-teal, same hue family — for small icons/text where #285A48 lacks contrast on dark bg
val ForestDeep  = Color(0xFF12302A)   // primaryContainer / pressed states — darker, slightly more teal
val ForestGlow  = Color(0x40285A48)   // selection rings, focus halos (alpha of primary)

// Borders — alpha-based for depth without color shifts
val BorderSubtle = Color(0x12FFFFFF)
val BorderStrong = Color(0x1FFFFFFF)

// Text
val TextPrimary   = Color(0xFFF5F5F4)
val TextSecondary = Color(0xFFA1A1AA)
val TextMuted     = Color(0xFF52525B)

// Status — calibrated to sit alongside forest teal without clashing
val StatusRed   = Color(0xFFE5484D)
val StatusAmber = Color(0xFFE0A12C)
val StatusGreen = Color(0xFF3D7E63)   // forest-leaning success — same family as accent

// Backward-compat aliases — old code still references these
val NeutralWhite  = TextPrimary
val NeutralText   = TextSecondary
val NeutralMuted  = TextMuted
val NeutralBorder = BorderSubtle
val Olive         = Forest
val OliveBright   = ForestLight
val OliveDeep     = ForestDeep
val OliveGlow     = ForestGlow
val Violet        = Forest
val VioletLight   = ForestLight
val VioletDeep    = ForestDeep
val VioletGlow    = ForestGlow
val Mint          = ForestLight
val MintDeep      = ForestDeep
val ConflictRed   = StatusRed
val SyncGreen     = StatusGreen
val OfflineOrange = StatusAmber

// Per-note accent strip — cool forest/teal family only
val NoteAccentColors = listOf(
    Color(0xFF285A48), // forest teal (primary)
    Color(0xFF3D7E63), // light forest
    Color(0xFF1F4A3A), // deeper forest
    Color(0xFF2D6660), // forest-teal blend
    Color(0xFF1F5050), // deep teal
    Color(0xFF45726B), // muted teal
    Color(0xFF3F6B8C), // slate blue (cool complement)
    Color(0xFF5A6B7A)  // cool gray-blue (near-neutral)
)
