package com.igbocalendar.viewmodel

import androidx.lifecycle.ViewModel
import com.igbocalendar.data.AppTheme
import com.igbocalendar.data.CalendarView
import com.igbocalendar.data.IgboCalendarEngine
import com.igbocalendar.data.IgboDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

data class CalendarUiState(
    val selectedDate: LocalDate = LocalDate.now(),
    val gregorianDisplayMonth: LocalDate = LocalDate.now().withDayOfMonth(1),
    val igboDisplayAnchor: LocalDate = LocalDate.now(),  // any date in the Igbo month to display
    val igboDate: IgboDate = IgboCalendarEngine.toIgboDate(LocalDate.now()),
    val calendarView: CalendarView = CalendarView.GREGORIAN,
    val appTheme: AppTheme = AppTheme.AMBER
)

class CalendarViewModel : ViewModel() {
    private val _state = MutableStateFlow(CalendarUiState())
    val state: StateFlow<CalendarUiState> = _state.asStateFlow()

    fun selectDate(date: LocalDate) {
        _state.update { it.copy(
            selectedDate = date,
            gregorianDisplayMonth = date.withDayOfMonth(1),
            igboDisplayAnchor = date,
            igboDate = IgboCalendarEngine.toIgboDate(date)
        ) }
    }

    fun goToToday() = selectDate(LocalDate.now())

    // Gregorian navigation
    fun gregorianPrevMonth() {
        _state.update { it.copy(
            gregorianDisplayMonth = it.gregorianDisplayMonth.minusMonths(1)
        ) }
    }

    fun gregorianNextMonth() {
        _state.update { it.copy(
            gregorianDisplayMonth = it.gregorianDisplayMonth.plusMonths(1)
        ) }
    }

    // Igbo month navigation
    fun igboPrevMonth() {
        val prev = IgboCalendarEngine.previousIgboMonth(_state.value.igboDisplayAnchor)
        _state.update { it.copy(igboDisplayAnchor = prev) }
    }

    fun igboNextMonth() {
        val next = IgboCalendarEngine.nextIgboMonth(_state.value.igboDisplayAnchor)
        _state.update { it.copy(igboDisplayAnchor = next) }
    }

    fun setCalendarView(view: CalendarView) {
        _state.update { it.copy(calendarView = view) }
    }

    fun toggleTheme() {
        _state.update { it.copy(
            appTheme = if (it.appTheme == AppTheme.AMBER) AppTheme.GREEN else AppTheme.AMBER
        ) }
    }
}
