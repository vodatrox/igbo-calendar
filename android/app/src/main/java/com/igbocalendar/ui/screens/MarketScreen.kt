package com.igbocalendar.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
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
import com.igbocalendar.data.IgboCalendarEngine
import com.igbocalendar.data.MarketDayData
import com.igbocalendar.data.MarketDayInfo
import com.igbocalendar.ui.theme.igboColors
import java.time.LocalDate

@Composable
fun MarketScreen(
    modifier: Modifier = Modifier
) {
    val colors = igboColors
    val today = LocalDate.now()
    val todayIgbo = IgboCalendarEngine.toIgboDate(today)
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .verticalScroll(scrollState)
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(colors.surface, colors.background)
                    )
                )
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Text(
                text = "Market Days",
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                color = colors.primary
            )
            Text(
                text = "Ụbọchị Ahịa — The Sacred 4-Day Cycle",
                fontSize = 13.sp,
                color = colors.onSurfaceMuted
            )
        }

        // Today's market day highlight
        TodayMarketDayCard(todayIgbo.dayIndex, todayIgbo.day)

        Spacer(Modifier.height(20.dp))

        // All four market days
        Text(
            text = "THE FOUR MARKET DAYS",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = colors.onSurfaceMuted,
            letterSpacing = 1.5.sp,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(Modifier.height(12.dp))

        MarketDayData.days.forEach { dayInfo ->
            MarketDayCard(
                dayInfo = dayInfo,
                isToday = !todayIgbo.isUboshiOmumu && dayInfo.index == todayIgbo.dayIndex,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }

        Spacer(Modifier.height(20.dp))

        // Cosmic cycle info
        CosmicCycleInfo()

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun TodayMarketDayCard(dayIndex: Int, dayName: String) {
    val colors = igboColors
    val dayColor = com.igbocalendar.ui.theme.dayColor(dayIndex)
    val marketData = MarketDayData.days.getOrNull(dayIndex)

    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        dayColor.copy(alpha = 0.25f),
                        dayColor.copy(alpha = 0.05f)
                    )
                )
            )
            .border(1.5.dp, dayColor.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
            .padding(20.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "TODAY'S MARKET DAY",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = dayColor,
                        letterSpacing = 1.5.sp
                    )
                    Text(
                        text = dayName,
                        fontSize = 52.sp,
                        fontWeight = FontWeight.Black,
                        color = dayColor,
                        lineHeight = 54.sp
                    )
                }
                if (marketData != null) {
                    Text(marketData.icon, fontSize = 48.sp)
                }
            }
            if (marketData != null) {
                Text(
                    text = marketData.tagline,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.onSurface
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = marketData.significance.take(120) + "…",
                    fontSize = 12.sp,
                    color = colors.onSurfaceMuted,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
private fun MarketDayCard(
    dayInfo: MarketDayInfo,
    isToday: Boolean,
    modifier: Modifier = Modifier
) {
    val colors = igboColors
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(colors.surfaceCard)
            .border(
                width = if (isToday) 1.5.dp else 1.dp,
                color = if (isToday) dayInfo.color else colors.divider,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { expanded = !expanded }
    ) {
        // Card header
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Color indicator + emoji
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(dayInfo.color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(dayInfo.icon, fontSize = 28.sp)
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = dayInfo.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = dayInfo.color
                    )
                    if (isToday) {
                        Spacer(Modifier.width(8.dp))
                        TodayBadge()
                    }
                }
                Text(
                    text = dayInfo.tagline,
                    fontSize = 12.sp,
                    color = colors.onSurfaceMuted,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(4.dp))
                // Direction & deity row
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    InfoPill("📍 ${dayInfo.direction}", dayInfo.color)
                    InfoPill("🙏 ${dayInfo.deity.split(" ")[0]}", dayInfo.color)
                }
            }
        }

        // Expanded content
        if (expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(dayInfo.color.copy(alpha = 0.04f))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = dayInfo.significance,
                    fontSize = 13.sp,
                    color = colors.onSurface,
                    lineHeight = 20.sp
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "TRADITIONAL ACTIVITIES",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = dayInfo.color,
                    letterSpacing = 1.sp
                )
                Spacer(Modifier.height(6.dp))

                dayInfo.activities.forEach { activity ->
                    Row(
                        modifier = Modifier.padding(vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            Modifier
                                .size(5.dp)
                                .clip(androidx.compose.foundation.shape.CircleShape)
                                .background(dayInfo.color)
                        )
                        Text(
                            text = activity,
                            fontSize = 13.sp,
                            color = colors.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TodayBadge() {
    val colors = igboColors
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(colors.primary)
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(
            "TODAY",
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = colors.onPrimary,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
private fun InfoPill(text: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(color.copy(alpha = 0.12f))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = text,
            fontSize = 10.sp,
            color = color,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun CosmicCycleInfo() {
    val colors = igboColors
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(colors.surface)
            .border(1.dp, colors.divider, RoundedCornerShape(16.dp))
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("🌍", fontSize = 20.sp)
            Spacer(Modifier.width(8.dp))
            Text(
                "Market Cosmology",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = colors.primary
            )
        }
        Spacer(Modifier.height(10.dp))
        Text(
            text = "The four Igbo market days — Eke, Orie, Afọ, and Nkwọ — are not merely commercial days. They represent the four cardinal directions, four divine forces, and the fundamental rhythm of Igbo cosmological time. Every community has its primary market day, which defines its identity and spiritual orientation within the greater Igbo world.",
            fontSize = 13.sp,
            color = colors.onSurfaceMuted,
            lineHeight = 21.sp
        )
    }
}
