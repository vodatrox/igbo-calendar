package com.igbocalendar.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.igbocalendar.data.AppTheme

@Immutable
data class IgboColors(
    val background: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val surfaceCard: Color,
    val primary: Color,
    val primaryVariant: Color,
    val primaryContainer: Color,
    val onPrimary: Color,
    val secondary: Color,
    val onBackground: Color,
    val onSurface: Color,
    val onSurfaceMuted: Color,
    val divider: Color,
    val outline: Color,
    val error: Color,
    val success: Color,
    val navBar: Color,
    val appTheme: AppTheme
)

val LocalIgboColors = staticCompositionLocalOf<IgboColors> {
    error("No IgboColors provided")
}

fun amberIgboColors() = IgboColors(
    background      = AmberPalette.Background,
    surface         = AmberPalette.Surface,
    surfaceVariant  = AmberPalette.SurfaceVariant,
    surfaceCard     = AmberPalette.SurfaceCard,
    primary         = AmberPalette.Primary,
    primaryVariant  = AmberPalette.PrimaryVariant,
    primaryContainer = AmberPalette.PrimaryContainer,
    onPrimary       = AmberPalette.OnPrimary,
    secondary       = AmberPalette.Secondary,
    onBackground    = AmberPalette.OnBackground,
    onSurface       = AmberPalette.OnSurface,
    onSurfaceMuted  = AmberPalette.OnSurfaceMuted,
    divider         = AmberPalette.Divider,
    outline         = AmberPalette.Outline,
    error           = AmberPalette.Error,
    success         = AmberPalette.Success,
    navBar          = AmberPalette.NavBar,
    appTheme        = AppTheme.AMBER
)

fun greenIgboColors() = IgboColors(
    background      = GreenPalette.Background,
    surface         = GreenPalette.Surface,
    surfaceVariant  = GreenPalette.SurfaceVariant,
    surfaceCard     = GreenPalette.SurfaceCard,
    primary         = GreenPalette.Primary,
    primaryVariant  = GreenPalette.PrimaryVariant,
    primaryContainer = GreenPalette.PrimaryContainer,
    onPrimary       = GreenPalette.OnPrimary,
    secondary       = GreenPalette.Secondary,
    onBackground    = GreenPalette.OnBackground,
    onSurface       = GreenPalette.OnSurface,
    onSurfaceMuted  = GreenPalette.OnSurfaceMuted,
    divider         = GreenPalette.Divider,
    outline         = GreenPalette.Outline,
    error           = GreenPalette.Error,
    success         = GreenPalette.Success,
    navBar          = GreenPalette.NavBar,
    appTheme        = AppTheme.GREEN
)

@Composable
fun IgboCalendarTheme(
    appTheme: AppTheme = AppTheme.AMBER,
    content: @Composable () -> Unit
) {
    val igboColors = if (appTheme == AppTheme.AMBER) amberIgboColors() else greenIgboColors()

    val materialColorScheme = darkColorScheme(
        primary            = igboColors.primary,
        onPrimary          = igboColors.onPrimary,
        primaryContainer   = igboColors.primaryContainer,
        secondary          = igboColors.secondary,
        background         = igboColors.background,
        surface            = igboColors.surface,
        surfaceVariant     = igboColors.surfaceVariant,
        onBackground       = igboColors.onBackground,
        onSurface          = igboColors.onSurface,
        error              = igboColors.error,
        outline            = igboColors.outline
    )

    CompositionLocalProvider(LocalIgboColors provides igboColors) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            typography  = IgboTypography,
            content     = content
        )
    }
}

// Convenience accessor
val igboColors: IgboColors
    @Composable get() = LocalIgboColors.current
