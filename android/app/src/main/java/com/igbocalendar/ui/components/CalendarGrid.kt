package com.igbocalendar.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.igbocalendar.data.IgboCalendarEngine
import com.igbocalendar.ui.theme.dayColor
import com.igbocalendar.ui.theme.igboColors
import java.time.LocalDate

private val DAY_HEADERS = listOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")

@Composable
fun GregorianCalendarGrid(
    displayMonth: LocalDate,
    selectedDate: LocalDate,
    today: LocalDate = LocalDate.now(),
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = igboColors

    Column(modifier = modifier) {
        // Day-of-week headers
        Row(modifier = Modifier.fillMaxWidth()) {
            DAY_HEADERS.forEach { header ->
                Text(
                    text = header,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.onSurfaceMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Build grid
        val firstDayOfMonth = displayMonth.withDayOfMonth(1)
        val startOffset = firstDayOfMonth.dayOfWeek.value % 7  // Sun=0
        val daysInMonth = displayMonth.lengthOfMonth()

        val cells = mutableListOf<LocalDate?>()
        repeat(startOffset) { cells.add(null) }
        for (d in 1..daysInMonth) cells.add(firstDayOfMonth.withDayOfMonth(d))

        // Pad to complete last row
        while (cells.size % 7 != 0) cells.add(null)

        cells.chunked(7).forEach { week ->
            Row(modifier = Modifier.fillMaxWidth()) {
                week.forEach { date ->
                    CalendarCell(
                        date = date,
                        isSelected = date == selectedDate,
                        isToday = date == today,
                        onSelect = { if (date != null) onDateSelected(date) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun CalendarCell(
    date: LocalDate?,
    isSelected: Boolean,
    isToday: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = igboColors

    val igboDay = date?.let { IgboCalendarEngine.toIgboDate(it) }
    val dayIdx = igboDay?.dayIndex ?: 0
    val accent = dayColor(dayIdx)

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .clip(RoundedCornerShape(10.dp))
            .then(
                if (date != null) Modifier.clickable(onClick = onSelect) else Modifier
            )
            .then(
                when {
                    isSelected -> Modifier.background(accent)
                    isToday    -> Modifier.border(1.5.dp, accent, RoundedCornerShape(10.dp))
                    else       -> Modifier
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        if (date != null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize().padding(2.dp)
            ) {
                Text(
                    text = date.dayOfMonth.toString(),
                    fontSize = 14.sp,
                    fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Medium,
                    color = when {
                        isSelected -> Color.Black.copy(alpha = 0.85f)
                        isToday    -> accent
                        else       -> colors.onSurface
                    }
                )
                if (igboDay != null) {
                    Text(
                        text = igboDay.day,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = when {
                            isSelected -> Color.Black.copy(alpha = 0.65f)
                            else -> accent
                        },
                        lineHeight = 9.sp
                    )
                }
            }
        }
    }
}
