package com.igbocalendar.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.igbocalendar.data.IgboDate
import com.igbocalendar.ui.theme.dayColor
import com.igbocalendar.ui.theme.igboColors
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun IgboDateCard(
    selectedDate: LocalDate,
    igboDate: IgboDate,
    modifier: Modifier = Modifier
) {
    val colors = igboColors
    val accent = dayColor(igboDate.dayIndex)
    val gregorianFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(colors.surfaceCard)
            .border(1.dp, accent.copy(alpha = 0.25f), RoundedCornerShape(20.dp))
            .padding(20.dp)
    ) {
        Column {
            // Gregorian date header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "Gregorian Date",
                        fontSize = 11.sp,
                        color = colors.onSurfaceMuted,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = selectedDate.format(gregorianFormatter),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.onSurface
                    )
                }
                // Day color dot
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(androidx.compose.foundation.shape.CircleShape)
                        .background(accent)
                )
            }

            Spacer(Modifier.height(16.dp))

            if (igboDate.isUboshiOmumu) {
                UboshiOmumuCard(igboDate, accent, colors)
            } else {
                NormalIgboDateContent(igboDate, accent, colors)
            }
        }
    }
}

@Composable
private fun NormalIgboDateContent(
    igboDate: IgboDate,
    accent: Color,
    colors: com.igbocalendar.ui.theme.IgboColors
) {
    // Large market day display
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "MARKET DAY",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = accent,
                letterSpacing = 1.5.sp
            )
            Text(
                text = igboDate.day,
                fontSize = 48.sp,
                fontWeight = FontWeight.Black,
                color = accent,
                lineHeight = 50.sp
            )
        }

        // Igbo details column
        Column(horizontalAlignment = Alignment.End) {
            IgboDetailChip(label = "IZU", value = igboDate.week, accent = accent)
            Spacer(Modifier.height(6.dp))
            IgboDetailChip(label = "ỌNWA", value = igboDate.month, accent = accent)
            Spacer(Modifier.height(6.dp))
            IgboDetailChip(label = "ARO", value = igboDate.year.toString(), accent = accent)
        }
    }

    Spacer(Modifier.height(14.dp))

    // Divider
    Box(
        Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(
                Brush.horizontalGradient(
                    listOf(accent.copy(alpha = 0.6f), accent.copy(alpha = 0f))
                )
            )
    )

    Spacer(Modifier.height(12.dp))

    // Day within month progress
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Day ${igboDate.dayWithinMonth + 1} of 28",
            fontSize = 12.sp,
            color = colors.onSurfaceMuted
        )
        Text(
            text = "${igboDate.week} · ${igboDate.month}",
            fontSize = 12.sp,
            color = colors.onSurfaceMuted,
            fontWeight = FontWeight.Medium
        )
    }

    Spacer(Modifier.height(8.dp))

    // Month progress bar
    MonthProgressBar(
        progress = (igboDate.dayWithinMonth + 1) / 28f,
        accent = accent
    )
}

@Composable
private fun IgboDetailChip(
    label: String,
    value: String,
    accent: Color
) {
    val colors = igboColors
    Column(
        horizontalAlignment = Alignment.End,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(accent.copy(alpha = 0.1f))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold,
            color = accent,
            letterSpacing = 1.sp
        )
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = colors.onSurface,
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun MonthProgressBar(progress: Float, accent: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(5.dp)
            .clip(RoundedCornerShape(3.dp))
            .background(accent.copy(alpha = 0.15f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress)
                .fillMaxHeight()
                .clip(RoundedCornerShape(3.dp))
                .background(
                    Brush.horizontalGradient(listOf(accent, accent.copy(alpha = 0.7f)))
                )
        )
    }
}

@Composable
private fun UboshiOmumuCard(
    igboDate: IgboDate,
    accent: Color,
    colors: com.igbocalendar.ui.theme.IgboColors
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("✨", fontSize = 32.sp, textAlign = TextAlign.Center)
        Spacer(Modifier.height(6.dp))
        Text(
            text = "Ubọchị Ọmụmụ",
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            color = accent,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Sacred New Year Day",
            fontSize = 13.sp,
            color = colors.onSurfaceMuted,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Igbo Year ${igboDate.year}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = colors.onSurface
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Market Day: ${igboDate.day}",
            fontSize = 13.sp,
            color = accent,
            fontWeight = FontWeight.SemiBold
        )
    }
}
