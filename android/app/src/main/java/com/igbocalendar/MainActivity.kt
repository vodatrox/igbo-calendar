package com.igbocalendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.igbocalendar.data.AppScreen
import com.igbocalendar.ui.components.IgboBottomNavBar
import com.igbocalendar.ui.screens.*
import com.igbocalendar.ui.theme.IgboCalendarTheme
import com.igbocalendar.ui.theme.igboColors
import com.igbocalendar.viewmodel.CalendarViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: CalendarViewModel = viewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()

            IgboCalendarTheme(appTheme = state.appTheme) {
                IgboCalendarApp(
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
private fun IgboCalendarApp(viewModel: CalendarViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val colors = igboColors
    var currentScreen by remember { mutableStateOf(AppScreen.CALENDAR) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Main content area
            Box(modifier = Modifier.weight(1f)) {
                AnimatedContent(
                    targetState = currentScreen,
                    transitionSpec = {
                        val enter = fadeIn(tween(220)) + slideInHorizontally(
                            animationSpec = tween(220),
                            initialOffsetX = { if (targetState.ordinal > initialState.ordinal) it / 6 else -it / 6 }
                        )
                        val exit = fadeOut(tween(180)) + slideOutHorizontally(
                            animationSpec = tween(180),
                            targetOffsetX = { if (targetState.ordinal > initialState.ordinal) -it / 6 else it / 6 }
                        )
                        enter togetherWith exit
                    },
                    label = "screen_transition"
                ) { screen ->
                    when (screen) {
                        AppScreen.CALENDAR -> CalendarScreen(
                            state = state,
                            onDateSelected = viewModel::selectDate,
                            onGregorianPrevMonth = viewModel::gregorianPrevMonth,
                            onGregorianNextMonth = viewModel::gregorianNextMonth,
                            onIgboPrevMonth = viewModel::igboPrevMonth,
                            onIgboNextMonth = viewModel::igboNextMonth,
                            onViewChange = viewModel::setCalendarView,
                            onToggleTheme = viewModel::toggleTheme,
                            onGoToday = viewModel::goToToday
                        )
                        AppScreen.MARKET -> MarketScreen()
                        AppScreen.INFO   -> InfoScreen()
                        AppScreen.EVENTS -> EventsScreen()
                    }
                }
            }

            // Bottom navigation bar
            IgboBottomNavBar(
                currentScreen = currentScreen,
                onNavigate = { currentScreen = it }
            )
        }
    }
}
