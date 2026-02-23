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
import com.igbocalendar.data.IgboDate
import com.igbocalendar.ui.theme.IzuColors
import com.igbocalendar.ui.theme.dayColor
import com.igbocalendar.ui.theme.igboColors
import java.time.LocalDate

/**
 * Displays an Igbo month as a 7-week × 4-day grid.
 * Each row = one Izu (week), each column = one market day (EKE, ORIE, AFỌ, NKWỌ)
 */
@Composable
fun IgboMonthGrid(
    anchorDate: LocalDate,
    selectedDate: LocalDate,
    today: LocalDate = LocalDate.now(),
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val igboAnchor = IgboCalendarEngine.toIgboDate(anchorDate)

    if (igboAnchor.isUboshiOmumu) {
        UboshiOmumuDisplay(
            igboDate = igboAnchor,
            isSelected = anchorDate == selectedDate,
            isToday = anchorDate == today,
            onSelect = { onDateSelected(anchorDate) },
            modifier = modifier
        )
        return
    }

    val igboYearBase = igboAnchor.year - IgboCalendarEngine.IGBO_ERA_OFFSET
    val monthDates = IgboCalendarEngine.getIgboMonthDates(igboYearBase, igboAnchor.monthIndex)

    Column(modifier = modifier) {
        // Column headers: day names
        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.width(54.dp))
            IgboCalendarEngine.IGBO_DAYS.forEachIndexed { idx, dayName ->
                Text(
                    text = dayName,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = dayColor(idx)
                )
            }
        }

        Spacer(Modifier.height(6.dp))

        // 7 weeks × 4 days
        monthDates.chunked(4).forEachIndexed { weekIdx, weekDates ->
            val weekColor = IzuColors[weekIdx]
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Week label
                Text(
                    text = IgboCalendarEngine.IGBO_WEEKS[weekIdx].replace("Izu ", "Izu\n"),
                    modifier = Modifier.width(54.dp),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 11.sp,
                    color = weekColor.copy(alpha = 0.8f)
                )

                weekDates.forEachIndexed { dayIdx, date ->
                    IgboMonthCell(
                        date = date,
                        dayIndex = dayIdx,
                        weekColor = weekColor,
                        isSelected = date == selectedDate,
                        isToday = date == today,
                        onSelect = { onDateSelected(date) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun IgboMonthCell(
    date: LocalDate,
    dayIndex: Int,
    weekColor: Color,
    isSelected: Boolean,
    isToday: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = igboColors
    val accent = dayColor(dayIndex)

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onSelect)
            .then(
                when {
                    isSelected -> Modifier.background(accent)
                    isToday    -> Modifier.border(1.5.dp, accent, RoundedCornerShape(8.dp))
                    else       -> Modifier.background(weekColor.copy(alpha = 0.08f))
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = date.dayOfMonth.toString(),
                fontSize = 12.sp,
                fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Medium,
                color = when {
                    isSelected -> Color.Black.copy(alpha = 0.85f)
                    isToday    -> accent
                    else       -> colors.onSurface
                }
            )
        }
    }
}

@Composable
private fun UboshiOmumuDisplay(
    igboDate: IgboDate,
    isSelected: Boolean,
    isToday: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = igboColors
    val accent = dayColor(igboDate.dayIndex)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onSelect)
            .then(
                when {
                    isSelected -> Modifier
                        .background(accent.copy(alpha = 0.18f))
                        .border(2.dp, accent, RoundedCornerShape(16.dp))
                    isToday    -> Modifier
                        .background(colors.primaryContainer)
                        .border(2.dp, accent, RoundedCornerShape(16.dp))
                    else       -> Modifier.background(colors.primaryContainer)
                }
            )
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("✨", fontSize = 36.sp)
            Spacer(Modifier.height(8.dp))
            Text(
                "Ubọchị Ọmụmụ",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = colors.primary
            )
            Text(
                "No Ọnwa · No Izu",
                fontSize = 12.sp,
                color = colors.onSurfaceMuted,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(accent.copy(alpha = 0.15f))
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Text(
                    text = igboDate.day,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    color = accent
                )
            }
        }
    }
}
