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
import com.igbocalendar.data.IzuData
import com.igbocalendar.data.IzuInfo
import com.igbocalendar.data.OnwaInfo
import com.igbocalendar.ui.theme.IzuColors
import com.igbocalendar.ui.theme.igboColors

private enum class InfoTab { WEEKS, MONTHS }

@Composable
fun InfoScreen(modifier: Modifier = Modifier) {
    val colors = igboColors
    var activeTab by remember { mutableStateOf(InfoTab.WEEKS) }
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(listOf(colors.surface, colors.background))
                )
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Text(
                text = "Igbo Calendar",
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                color = colors.primary
            )
            Text(
                text = "Structure & Heritage",
                fontSize = 13.sp,
                color = colors.onSurfaceMuted
            )
        }

        // Tab bar
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 4.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(colors.surfaceVariant)
                .padding(4.dp)
        ) {
            InfoTab.entries.forEach { tab ->
                val selected = activeTab == tab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(9.dp))
                        .background(if (selected) colors.primary else Color.Transparent)
                        .clickable { activeTab = tab }
                        .padding(vertical = 9.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (tab == InfoTab.WEEKS) "Izu (Weeks)" else "Ọnwa (Months)",
                        fontSize = 13.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        color = if (selected) colors.onPrimary else colors.onSurfaceMuted
                    )
                }
            }
        }

        Spacer(Modifier.height(4.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            when (activeTab) {
                InfoTab.WEEKS  -> WeeksContent()
                InfoTab.MONTHS -> MonthsContent()
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun WeeksContent() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        // Overview card
        InfoOverviewCard(
            emoji = "📅",
            title = "Izu — The Igbo Week",
            body = "The Igbo month (Ọnwa) is divided into 7 weeks (Izu), each consisting of 4 market days: Eke, Orie, Afọ, and Nkwọ. Each Izu carries its own spiritual energy, traditional purpose, and communal significance."
        )

        Spacer(Modifier.height(16.dp))

        IzuData.weeks.forEachIndexed { index, izu ->
            IzuCard(izu = izu, color = IzuColors[index])
            Spacer(Modifier.height(10.dp))
        }

        // Did you know
        DidYouKnowCard(
            text = "In many Igbo communities, Izu Asaa is a sacred time when elders convene to consult divination and determine the planting calendar for the upcoming year based on cosmic signs."
        )
    }
}

@Composable
private fun MonthsContent() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        InfoOverviewCard(
            emoji = "🌙",
            title = "Ọnwa — The Igbo Month",
            body = "The Igbo year (Aro) consists of 13 months (Ọnwa) of 28 days each, plus one sacred day — Ubọchị Ọmụmụ (New Year Day). Each month is named after a deity, season, or cultural significance."
        )

        Spacer(Modifier.height(16.dp))

        IzuData.months.forEachIndexed { index, onwa ->
            OnwaCard(onwa = onwa, index = index)
            Spacer(Modifier.height(10.dp))
        }

        DidYouKnowCard(
            text = "Ubọchị Ọmụmụ — the 365th day of the Igbo year — is a day of new birth and cosmic renewal. It falls outside the 13-month structure and is the most spiritually charged day of the Igbo year."
        )
    }
}

@Composable
private fun InfoOverviewCard(emoji: String, title: String, body: String) {
    val colors = igboColors
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(colors.surface)
            .border(1.dp, colors.outline.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .padding(18.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(emoji, fontSize = 24.sp)
            Spacer(Modifier.width(10.dp))
            Text(
                text = title,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = colors.primary
            )
        }
        Spacer(Modifier.height(10.dp))
        Text(
            text = body,
            fontSize = 13.sp,
            color = colors.onSurfaceMuted,
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun IzuCard(izu: IzuInfo, color: Color) {
    val colors = igboColors
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(colors.surfaceCard)
            .border(1.dp, color.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
            .clickable { expanded = !expanded }
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(izu.icon, fontSize = 22.sp)
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = izu.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
                Text(
                    text = izu.tagline,
                    fontSize = 12.sp,
                    color = colors.onSurfaceMuted
                )
            }

            Text(
                text = if (expanded) "▲" else "▼",
                fontSize = 10.sp,
                color = colors.onSurfaceMuted
            )
        }

        if (expanded) {
            Text(
                text = izu.description,
                fontSize = 13.sp,
                color = colors.onSurface,
                lineHeight = 20.sp,
                modifier = Modifier
                    .background(color.copy(alpha = 0.04f))
                    .padding(horizontal = 14.dp, vertical = 12.dp)
            )
        }
    }
}

@Composable
private fun OnwaCard(onwa: OnwaInfo, index: Int) {
    val colors = igboColors
    val accent = IzuColors[index % IzuColors.size]
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(colors.surfaceCard)
            .border(1.dp, accent.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
            .clickable { expanded = !expanded }
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Month number badge
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(accent.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${index + 1}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = accent
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = onwa.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = accent
                )
                Text(
                    text = onwa.season,
                    fontSize = 11.sp,
                    color = colors.onSurfaceMuted
                )
            }

            Text(
                text = if (expanded) "▲" else "▼",
                fontSize = 10.sp,
                color = colors.onSurfaceMuted
            )
        }

        if (expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(accent.copy(alpha = 0.04f))
                    .padding(horizontal = 14.dp, vertical = 12.dp)
            ) {
                Text(
                    text = onwa.description,
                    fontSize = 13.sp,
                    color = colors.onSurface,
                    lineHeight = 20.sp
                )

                Spacer(Modifier.height(10.dp))
                Text(
                    "ACTIVITIES",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = accent,
                    letterSpacing = 1.sp
                )
                Spacer(Modifier.height(4.dp))
                onwa.activities.forEach { activity ->
                    Row(
                        modifier = Modifier.padding(vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            Modifier
                                .size(5.dp)
                                .clip(androidx.compose.foundation.shape.CircleShape)
                                .background(accent)
                        )
                        Text(activity, fontSize = 13.sp, color = colors.onSurface)
                    }
                }
            }
        }
    }
}

@Composable
private fun DidYouKnowCard(text: String) {
    val colors = igboColors
    Spacer(Modifier.height(8.dp))
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(colors.primaryContainer)
            .border(1.dp, colors.primary.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
            .padding(16.dp)
    ) {
        Text(
            "💡 DID YOU KNOW?",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = colors.primary,
            letterSpacing = 1.sp
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = text,
            fontSize = 13.sp,
            color = colors.onSurface,
            lineHeight = 20.sp
        )
    }
}
