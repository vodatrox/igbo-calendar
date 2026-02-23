package com.igbocalendar.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.igbocalendar.data.CalendarView
import com.igbocalendar.data.IgboCalendarEngine
import com.igbocalendar.ui.components.DateSearchBottomSheet
import com.igbocalendar.ui.components.GregorianCalendarGrid
import com.igbocalendar.ui.components.IgboDateCard
import com.igbocalendar.ui.components.IgboMonthGrid
import com.igbocalendar.ui.theme.igboColors
import com.igbocalendar.viewmodel.CalendarUiState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun CalendarScreen(
    state: CalendarUiState,
    onDateSelected: (LocalDate) -> Unit,
    onGregorianPrevMonth: () -> Unit,
    onGregorianNextMonth: () -> Unit,
    onIgboPrevMonth: () -> Unit,
    onIgboNextMonth: () -> Unit,
    onViewChange: (CalendarView) -> Unit,
    onToggleTheme: () -> Unit,
    onGoToday: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = igboColors
    val scrollState = rememberScrollState()
    var showDateSearch by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .verticalScroll(scrollState)
    ) {
        // ── App Header ────────────────────────────────────────────────────────
        AppHeader(
            onToggleTheme = onToggleTheme,
            onGoToday = onGoToday,
            onGoToDate = { showDateSearch = true }
        )

        // ── Calendar View Toggle ──────────────────────────────────────────────
        ViewToggleBar(
            currentView = state.calendarView,
            onViewChange = onViewChange
        )

        Spacer(Modifier.height(8.dp))

        // ── Calendar Panel ────────────────────────────────────────────────────
        AnimatedContent(
            targetState = state.calendarView,
            transitionSpec = {
                fadeIn(tween(200)) togetherWith fadeOut(tween(200))
            },
            label = "calendar_view"
        ) { view ->
            when (view) {
                CalendarView.GREGORIAN -> GregorianPanel(
                    state = state,
                    onDateSelected = onDateSelected,
                    onPrevMonth = onGregorianPrevMonth,
                    onNextMonth = onGregorianNextMonth
                )
                CalendarView.IGBO_ERA -> IgboEraPanel(
                    state = state,
                    onDateSelected = onDateSelected,
                    onPrevMonth = onIgboPrevMonth,
                    onNextMonth = onIgboNextMonth
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // ── Selected Date Card ────────────────────────────────────────────────
        IgboDateCard(
            selectedDate = state.selectedDate,
            igboDate = state.igboDate,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // ── Izu Week Info Banner ──────────────────────────────────────────────
        if (!state.igboDate.isUboshiOmumu) {
            Spacer(Modifier.height(12.dp))
            IzuInfoBanner(state = state)
        }

        Spacer(Modifier.height(24.dp))
    }

    if (showDateSearch) {
        DateSearchBottomSheet(
            currentDate = state.selectedDate,
            currentIgboDate = state.igboDate,
            onNavigate = onDateSelected,
            onDismiss = { showDateSearch = false }
        )
    }
}

@Composable
private fun AppHeader(
    onToggleTheme: () -> Unit,
    onGoToday: () -> Unit,
    onGoToDate: () -> Unit
) {
    val colors = igboColors
    val isAmber = colors.appTheme == com.igbocalendar.data.AppTheme.AMBER

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Igbo Calendar",
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = colors.primary
            )
            Text(
                text = "Ọha Igbo",
                fontSize = 12.sp,
                color = colors.onSurfaceMuted,
                fontWeight = FontWeight.Medium
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            // Go to date button
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(androidx.compose.foundation.shape.CircleShape)
                    .background(colors.surfaceVariant)
                    .clickable(onClick = onGoToDate),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Rounded.Search,
                    contentDescription = "Go to date",
                    tint = colors.primary,
                    modifier = Modifier.size(18.dp)
                )
            }

            // Today button
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(colors.primaryContainer)
                    .clickable(onClick = onGoToday)
                    .padding(horizontal = 14.dp, vertical = 7.dp)
            ) {
                Text(
                    text = "Today",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.primary
                )
            }

            // Theme toggle
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(androidx.compose.foundation.shape.CircleShape)
                    .background(colors.surfaceVariant)
                    .clickable(onClick = onToggleTheme),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isAmber) "🌙" else "🌕",
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
private fun ViewToggleBar(
    currentView: CalendarView,
    onViewChange: (CalendarView) -> Unit
) {
    val colors = igboColors

    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(colors.surfaceVariant)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        CalendarView.entries.forEach { view ->
            val selected = currentView == view
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(9.dp))
                    .background(if (selected) colors.primary else Color.Transparent)
                    .clickable { onViewChange(view) }
                    .padding(vertical = 9.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (view == CalendarView.GREGORIAN) "Gregorian" else "Igbo Era",
                    fontSize = 13.sp,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                    color = if (selected) colors.onPrimary else colors.onSurfaceMuted
                )
            }
        }
    }
}

@Composable
private fun GregorianPanel(
    state: CalendarUiState,
    onDateSelected: (LocalDate) -> Unit,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    val monthFmt = DateTimeFormatter.ofPattern("MMMM yyyy")

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        // Month navigation
        MonthNavRow(
            title = state.gregorianDisplayMonth.format(monthFmt),
            subtitle = IgboCalendarEngine.toIgboDate(state.gregorianDisplayMonth).run {
                if (isUboshiOmumu) "Ubọchị Ọmụmụ" else "$month · $year"
            },
            onPrev = onPrevMonth,
            onNext = onNextMonth
        )

        Spacer(Modifier.height(12.dp))

        // IZU legend bar
        IzuLegendBar()

        Spacer(Modifier.height(10.dp))

        // Calendar grid
        GregorianCalendarGrid(
            displayMonth = state.gregorianDisplayMonth,
            selectedDate = state.selectedDate,
            onDateSelected = onDateSelected
        )
    }
}

@Composable
private fun IgboEraPanel(
    state: CalendarUiState,
    onDateSelected: (LocalDate) -> Unit,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    val colors = igboColors
    val anchorIgbo = IgboCalendarEngine.toIgboDate(state.igboDisplayAnchor)

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        MonthNavRow(
            title = anchorIgbo.month,
            subtitle = "Aro ${anchorIgbo.year}  ·  Igbo Era",
            onPrev = onPrevMonth,
            onNext = onNextMonth
        )

        Spacer(Modifier.height(12.dp))

        if (!anchorIgbo.isUboshiOmumu) {
            // 4-day week structure info
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(colors.surfaceVariant)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "IZU (4-day week)",
                    fontSize = 11.sp,
                    color = colors.onSurfaceMuted,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "7 Izu = 28 days / Ọnwa",
                    fontSize = 11.sp,
                    color = colors.onSurfaceMuted
                )
            }

            Spacer(Modifier.height(10.dp))
        }

        IgboMonthGrid(
            anchorDate = state.igboDisplayAnchor,
            selectedDate = state.selectedDate,
            onDateSelected = onDateSelected
        )
    }
}

@Composable
private fun MonthNavRow(
    title: String,
    subtitle: String,
    onPrev: () -> Unit,
    onNext: () -> Unit
) {
    val colors = igboColors

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onPrev) {
            Icon(
                Icons.Rounded.ChevronLeft,
                contentDescription = "Previous month",
                tint = colors.primary
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colors.onBackground
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = colors.onSurfaceMuted
            )
        }

        IconButton(onClick = onNext) {
            Icon(
                Icons.Rounded.ChevronRight,
                contentDescription = "Next month",
                tint = colors.primary
            )
        }
    }
}

@Composable
private fun IzuLegendBar() {
    val colors = igboColors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(colors.surfaceVariant)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "IZU (4-Day Week):",
            fontSize = 10.sp,
            color = colors.onSurfaceMuted,
            fontWeight = FontWeight.SemiBold
        )
        IgboCalendarEngine.IGBO_DAYS.forEachIndexed { idx, name ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Box(
                    Modifier
                        .size(6.dp)
                        .clip(androidx.compose.foundation.shape.CircleShape)
                        .background(com.igbocalendar.ui.theme.dayColor(idx))
                )
                Text(
                    name,
                    fontSize = 10.sp,
                    color = com.igbocalendar.ui.theme.dayColor(idx),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun IzuInfoBanner(state: CalendarUiState) {
    val colors = igboColors
    val igbo = state.igboDate
    val izuDesc = com.igbocalendar.data.IzuData.weeks.getOrNull(igbo.weekIndex)

    if (izuDesc != null) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(colors.surface)
                .border(1.dp, colors.divider, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(izuDesc.icon, fontSize = 18.sp)
                Spacer(Modifier.width(8.dp))
                Text(
                    text = izuDesc.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.onSurface
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = izuDesc.tagline,
                    fontSize = 11.sp,
                    color = colors.primary,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(Modifier.height(6.dp))
            Text(
                text = izuDesc.description,
                fontSize = 12.sp,
                color = colors.onSurfaceMuted,
                lineHeight = 18.sp
            )
        }
    }
}
