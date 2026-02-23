package com.igbocalendar.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.igbocalendar.data.AppScreen
import com.igbocalendar.ui.theme.igboColors

private data class NavItem(
    val screen: AppScreen,
    val icon: ImageVector,
    val selectedIcon: ImageVector
)

private val navItems = listOf(
    NavItem(AppScreen.CALENDAR, Icons.Rounded.CalendarMonth, Icons.Rounded.CalendarMonth),
    NavItem(AppScreen.MARKET,   Icons.Rounded.Storefront,    Icons.Rounded.Storefront),
    NavItem(AppScreen.INFO,     Icons.AutoMirrored.Rounded.MenuBook, Icons.AutoMirrored.Rounded.MenuBook),
    NavItem(AppScreen.EVENTS,   Icons.Rounded.Celebration,   Icons.Rounded.Celebration)
)

@Composable
fun IgboBottomNavBar(
    currentScreen: AppScreen,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = igboColors

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.navBar)
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        navItems.forEach { item ->
            val selected = currentScreen == item.screen
            NavBarItem(
                item = item,
                selected = selected,
                colors = colors,
                onClick = { onNavigate(item.screen) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun NavBarItem(
    item: NavItem,
    selected: Boolean,
    colors: com.igbocalendar.ui.theme.IgboColors,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(
                    if (selected) colors.primaryContainer
                    else androidx.compose.ui.graphics.Color.Transparent
                )
                .padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = if (selected) item.selectedIcon else item.icon,
                contentDescription = item.screen.label,
                tint = if (selected) colors.primary else colors.onSurfaceMuted,
                modifier = Modifier.size(22.dp)
            )
        }
        Text(
            text = item.screen.label,
            fontSize = 10.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (selected) colors.primary else colors.onSurfaceMuted
        )
    }
}
