package com.igbocalendar.data

import androidx.compose.ui.graphics.Color

data class IgboDate(
    val day: String,
    val dayIndex: Int,
    val week: String,
    val weekIndex: Int,
    val month: String,
    val monthIndex: Int,
    val year: Int,
    val dayWithinMonth: Int,
    val isUboshiOmumu: Boolean
)

data class MarketDayInfo(
    val name: String,
    val index: Int,
    val tagline: String,
    val significance: String,
    val direction: String,
    val deity: String,
    val activities: List<String>,
    val color: Color,
    val icon: String
)

data class IzuInfo(
    val name: String,
    val index: Int,
    val tagline: String,
    val description: String,
    val icon: String
)

data class OnwaInfo(
    val name: String,
    val index: Int,
    val season: String,
    val description: String,
    val activities: List<String>
)

data class Festival(
    val name: String,
    val subtitle: String,
    val description: String,
    val marketDay: String,
    val igboMonth: String,
    val location: String,
    val featured: Boolean = false
)

enum class AppScreen(val label: String) {
    CALENDAR("Calendar"),
    MARKET("Market"),
    INFO("Info"),
    EVENTS("Events")
}

enum class AppTheme { AMBER, GREEN }

enum class CalendarView { GREGORIAN, IGBO_ERA }
