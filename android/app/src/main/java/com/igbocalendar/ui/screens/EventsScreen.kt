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
import com.igbocalendar.data.Festival
import com.igbocalendar.data.FestivalData
import com.igbocalendar.ui.theme.dayColor
import com.igbocalendar.ui.theme.igboColors

private fun marketDayIndex(marketDay: String): Int = when (marketDay.uppercase()) {
    "EKE"  -> 0
    "ORIE" -> 1
    "AFỌ", "AFO" -> 2
    "NKWỌ", "NKWO" -> 3
    else   -> 0
}

@Composable
fun EventsScreen(modifier: Modifier = Modifier) {
    val colors = igboColors
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
                .background(Brush.verticalGradient(listOf(colors.surface, colors.background)))
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Text(
                text = "Events & Festivals",
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                color = colors.primary
            )
            Text(
                text = "Ọ mụnụ ya n'omenala — Born of tradition",
                fontSize = 12.sp,
                color = colors.onSurfaceMuted,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )
        }

        // Featured festival
        val featured = FestivalData.festivals.firstOrNull { it.featured }
        if (featured != null) {
            FeaturedFestivalCard(festival = featured)
            Spacer(Modifier.height(20.dp))
        }

        // Upcoming festivals list
        Text(
            text = "IGBO FESTIVALS",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = colors.onSurfaceMuted,
            letterSpacing = 1.5.sp,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(Modifier.height(12.dp))

        FestivalData.festivals.filter { !it.featured }.forEach { festival ->
            FestivalCard(
                festival = festival,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }

        Spacer(Modifier.height(20.dp))

        // Cultural note
        CulturalNoteCard()

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun FeaturedFestivalCard(festival: Festival) {
    val colors = igboColors
    val accentIdx = marketDayIndex(festival.marketDay)
    val accent = dayColor(accentIdx)

    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        accent.copy(alpha = 0.3f),
                        colors.surface,
                        colors.surfaceVariant
                    )
                )
            )
            .border(1.5.dp, accent.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                FestivalMarketDayBadge(festival.marketDay, accent)
                FeaturedBadge(accent)
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = festival.name,
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = colors.onBackground,
                lineHeight = 28.sp
            )
            Text(
                text = festival.subtitle,
                fontSize = 13.sp,
                color = accent,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = festival.description.take(200) + "…",
                fontSize = 13.sp,
                color = colors.onSurfaceMuted,
                lineHeight = 20.sp
            )

            Spacer(Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("📍", fontSize = 12.sp)
                    Text(
                        festival.location,
                        fontSize = 12.sp,
                        color = colors.onSurfaceMuted
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("🌙", fontSize = 12.sp)
                    Text(
                        festival.igboMonth,
                        fontSize = 12.sp,
                        color = colors.onSurfaceMuted
                    )
                }
            }
        }
    }
}

@Composable
private fun FestivalCard(festival: Festival, modifier: Modifier = Modifier) {
    val colors = igboColors
    val accentIdx = marketDayIndex(festival.marketDay)
    val accent = dayColor(accentIdx)
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(colors.surfaceCard)
            .border(1.dp, accent.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
            .clickable { expanded = !expanded }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Market day badge box
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(accent.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = festival.marketDay.take(3),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = accent,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        "Day",
                        fontSize = 9.sp,
                        color = accent.copy(alpha = 0.7f)
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = festival.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.onSurface
                )
                Text(
                    text = festival.subtitle,
                    fontSize = 12.sp,
                    color = accent,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "📍 ${festival.location}",
                        fontSize = 11.sp,
                        color = colors.onSurfaceMuted
                    )
                }
            }

            Text(
                text = if (expanded) "▲" else "▼",
                fontSize = 10.sp,
                color = colors.onSurfaceMuted,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        if (expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(accent.copy(alpha = 0.04f))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = festival.description,
                    fontSize = 13.sp,
                    color = colors.onSurface,
                    lineHeight = 21.sp
                )
                Spacer(Modifier.height(10.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    InfoPill("🌙 ${festival.igboMonth}", accent)
                    InfoPill("🥁 ${festival.marketDay}", accent)
                }
            }
        }
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
            fontSize = 11.sp,
            color = color,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun FestivalMarketDayBadge(marketDay: String, accent: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(accent.copy(alpha = 0.15f))
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(
            text = "MARKET DAY: $marketDay",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = accent,
            letterSpacing = 0.8.sp
        )
    }
}

@Composable
private fun FeaturedBadge(accent: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                Brush.horizontalGradient(listOf(accent, accent.copy(alpha = 0.7f)))
            )
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(
            text = "✦ FEATURED",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black.copy(alpha = 0.8f),
            letterSpacing = 0.8.sp
        )
    }
}

@Composable
private fun CulturalNoteCard() {
    val colors = igboColors
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(colors.surface)
            .border(1.dp, colors.divider, RoundedCornerShape(16.dp))
            .padding(18.dp)
    ) {
        Text("🥁", fontSize = 22.sp)
        Spacer(Modifier.height(8.dp))
        Text(
            "Did You Know?",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = colors.primary
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = "Market days (Eke, Orie, Afọ, Nkwọ) dictate the rhythm of Igbo life — determining when to farm, trade, hold major gatherings, and celebrate festivals. Each community's primary market day is a core part of its identity and spiritual heritage.",
            fontSize = 13.sp,
            color = colors.onSurfaceMuted,
            lineHeight = 21.sp
        )
    }
}
