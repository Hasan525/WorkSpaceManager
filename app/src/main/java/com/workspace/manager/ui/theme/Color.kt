package com.workspace.manager.ui.theme

import androidx.compose.ui.graphics.Color

// ── Background layers ──────────────────────────────────────────────────────────
val BgBase      = Color(0xFF0C0C14)   // main screen background (near-black)
val BgSurface   = Color(0xFF13131E)   // card / bottom-sheet surface
val BgElevated  = Color(0xFF1B1B29)   // dialogs / menus
val BgHighlight = Color(0xFF252538)   // hover, selected row, input focus

// ── Primary — electric violet ──────────────────────────────────────────────────
val Violet      = Color(0xFF7C3AED)   // primary actions, FAB
val VioletLight = Color(0xFF9D5CF6)   // lighter variant (icons on dark bg)
val VioletDeep  = Color(0xFF2E1065)   // primaryContainer
val VioletGlow  = Color(0x337C3AED)   // alpha glow for selection rings

// ── Secondary — neon mint ─────────────────────────────────────────────────────
val Mint        = Color(0xFF10B981)   // secondary accent, sync indicator
val MintDeep    = Color(0xFF064E3B)   // secondaryContainer

// ── Neutrals ──────────────────────────────────────────────────────────────────
val NeutralWhite  = Color(0xFFF8F8FF) // primary text on dark
val NeutralText   = Color(0xFFB4B4CC) // secondary / body text
val NeutralMuted  = Color(0xFF565678) // placeholder, timestamps
val NeutralBorder = Color(0xFF1F2039) // card borders, dividers

// ── Status ────────────────────────────────────────────────────────────────────
val StatusRed   = Color(0xFFEF4444)   // error / conflict
val StatusAmber = Color(0xFFF59E0B)   // pending sync / offline
val StatusGreen = Color(0xFF22C55E)   // online / success

// ── Semantic aliases (kept for backward-compat with existing callers) ─────────
val ConflictRed   = StatusRed
val SyncGreen     = StatusGreen
val OfflineOrange = StatusAmber

// ── Per-note accent strip colours (chosen by note.id.hashCode()) ──────────────
val NoteAccentColors = listOf(
    Color(0xFF7C3AED), // violet
    Color(0xFF2563EB), // blue
    Color(0xFF0891B2), // cyan
    Color(0xFF059669), // emerald
    Color(0xFF65A30D), // lime
    Color(0xFFD97706), // amber
    Color(0xFFDB2777), // pink
    Color(0xFFDC2626)  // red
)
