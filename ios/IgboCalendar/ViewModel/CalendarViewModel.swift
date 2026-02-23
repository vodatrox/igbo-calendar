import SwiftUI
import Combine

// MARK: - UI State

struct CalendarUiState {
    var selectedDate:          Date            = .today
    var gregorianDisplayMonth: Date            = Date.today.firstOfMonth
    var igboDisplayAnchor:     Date            = .today
    var igboDate:              IgboDate        = IgboCalendarEngine.toIgboDate(.today)
    var calendarView:          CalendarViewMode = .gregorian
    var appTheme:              AppTheme        = .amber
}

// MARK: - ViewModel

final class CalendarViewModel: ObservableObject {

    @Published private(set) var state = CalendarUiState()
    @Published var selectedTab: AppTab = .calendar

    // MARK: - Computed

    var colors: IgboColors {
        state.appTheme == .amber ? .amber : .green
    }

    // MARK: - Date Selection

    func selectDate(_ date: Date) {
        let normalized = date.normalized
        state.selectedDate          = normalized
        state.gregorianDisplayMonth = normalized.firstOfMonth
        state.igboDisplayAnchor     = normalized
        state.igboDate              = IgboCalendarEngine.toIgboDate(normalized)
    }

    func goToToday() {
        selectDate(.today)
    }

    // MARK: - Gregorian Navigation

    func gregorianPrevMonth() {
        state.gregorianDisplayMonth = state.gregorianDisplayMonth.addingMonths(-1)
    }

    func gregorianNextMonth() {
        state.gregorianDisplayMonth = state.gregorianDisplayMonth.addingMonths(1)
    }

    // MARK: - Igbo Navigation

    func igboPrevMonth() {
        state.igboDisplayAnchor = IgboCalendarEngine.previousIgboMonth(state.igboDisplayAnchor)
    }

    func igboNextMonth() {
        state.igboDisplayAnchor = IgboCalendarEngine.nextIgboMonth(state.igboDisplayAnchor)
    }

    // MARK: - View & Theme

    func setCalendarView(_ view: CalendarViewMode) {
        state.calendarView = view
    }

    func toggleTheme() {
        state.appTheme = state.appTheme == .amber ? .green : .amber
    }
}
