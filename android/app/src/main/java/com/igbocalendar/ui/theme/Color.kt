package com.igbocalendar.ui.theme

import androidx.compose.ui.graphics.Color

// ── Amber / Gold Theme ───────────────────────────────────────────────────────
object AmberPalette {
    val Background      = Color(0xFF0C0800)
    val Surface         = Color(0xFF181000)
    val SurfaceVariant  = Color(0xFF241A00)
    val SurfaceCard     = Color(0xFF1E1500)
    val Primary         = Color(0xFFD4900A)
    val PrimaryVariant  = Color(0xFFB07808)
    val PrimaryContainer = Color(0xFF3D2800)
    val OnPrimary       = Color(0xFF0C0800)
    val Secondary       = Color(0xFFF0B429)
    val OnBackground    = Color(0xFFFFF8E7)
    val OnSurface       = Color(0xFFF5E6C8)
    val OnSurfaceMuted  = Color(0xFF9E8555)
    val Divider         = Color(0xFF3D2F00)
    val Outline         = Color(0xFF5C4400)
    val Error           = Color(0xFFCF6679)
    val Success         = Color(0xFF4CAF50)
    val NavBar          = Color(0xFF100C00)
}

// ── Forest Green Theme ───────────────────────────────────────────────────────
object GreenPalette {
    val Background      = Color(0xFF050F05)
    val Surface         = Color(0xFF081508)
    val SurfaceVariant  = Color(0xFF0D2410)
    val SurfaceCard     = Color(0xFF0A1E0C)
    val Primary         = Color(0xFF00C853)
    val PrimaryVariant  = Color(0xFF00A040)
    val PrimaryContainer = Color(0xFF003015)
    val OnPrimary       = Color(0xFF050F05)
    val Secondary       = Color(0xFF69F0AE)
    val OnBackground    = Color(0xFFE8F5E9)
    val OnSurface       = Color(0xFFD4EDDA)
    val OnSurfaceMuted  = Color(0xFF5A9468)
    val Divider         = Color(0xFF1B4020)
    val Outline         = Color(0xFF254D2C)
    val Error           = Color(0xFFCF6679)
    val Success         = Color(0xFF00E676)
    val NavBar          = Color(0xFF040C04)
}

// ── Market Day Colors (shared across themes) ─────────────────────────────────
val EkeColor   = Color(0xFFD4900A)   // amber gold
val OrieColor  = Color(0xFF1976D2)   // royal blue
val AfoColor   = Color(0xFF2E7D32)   // deep green
val NkwoColor  = Color(0xFF7B1FA2)   // purple

val EkeColorLight  = Color(0xFFFFF3CD)
val OrieColorLight = Color(0xFFE3F2FD)
val AfoColorLight  = Color(0xFFE8F5E9)
val NkwoColorLight = Color(0xFFF3E5F5)

// ── Week accent colors ────────────────────────────────────────────────────────
val IzuColors = listOf(
    Color(0xFFD4900A), // Izu Mbụ  – amber
    Color(0xFF1976D2), // Izu Abụọ – blue
    Color(0xFF2E7D32), // Izu Atọ  – green
    Color(0xFF7B1FA2), // Izu Anọ  – purple
    Color(0xFFE53935), // Izu Ise  – red
    Color(0xFF00838F), // Izu Isii – teal
    Color(0xFFF57C00)  // Izu Asaa – deep orange
)

fun dayColor(dayIndex: Int): Color = when (dayIndex) {
    0 -> EkeColor
    1 -> OrieColor
    2 -> AfoColor
    3 -> NkwoColor
    else -> Color.Gray
}

fun dayColorLight(dayIndex: Int): Color = when (dayIndex) {
    0 -> EkeColorLight
    1 -> OrieColorLight
    2 -> AfoColorLight
    3 -> NkwoColorLight
    else -> Color.LightGray
}
