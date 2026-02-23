import SwiftUI

// MARK: - Calendar Screen

struct CalendarScreen: View {
    @EnvironmentObject private var viewModel: CalendarViewModel
    @State private var showDateSearch = false

    private var state: CalendarUiState { viewModel.state }
    @Environment(\.igboColors) private var colors

    var body: some View {
        ScrollView {
            VStack(spacing: 0) {
                AppHeader(
                    onToggleTheme:  { viewModel.toggleTheme() },
                    onGoToday:      { viewModel.goToToday() },
                    onGoToDate:     { showDateSearch = true }
                )

                ViewToggleBar(
                    currentView:   state.calendarView,
                    onViewChange:  { viewModel.setCalendarView($0) }
                )

                Spacer().frame(height: 8)

                // Animated panel swap
                Group {
                    if state.calendarView == .gregorian {
                        GregorianPanel(
                            state:       state,
                            onDateSelected: { viewModel.selectDate($0) },
                            onPrevMonth:    { viewModel.gregorianPrevMonth() },
                            onNextMonth:    { viewModel.gregorianNextMonth() }
                        )
                        .transition(.opacity)
                    } else {
                        IgboEraPanel(
                            state:       state,
                            onDateSelected: { viewModel.selectDate($0) },
                            onPrevMonth:    { viewModel.igboPrevMonth() },
                            onNextMonth:    { viewModel.igboNextMonth() }
                        )
                        .transition(.opacity)
                    }
                }
                .animation(.easeInOut(duration: 0.2), value: state.calendarView)

                Spacer().frame(height: 16)

                IgboDateCard(
                    selectedDate: state.selectedDate,
                    igboDate:     state.igboDate
                )
                .padding(.horizontal, 16)

                if !state.igboDate.isUboshiOmumu {
                    Spacer().frame(height: 12)
                    IzuInfoBanner(igboDate: state.igboDate)
                        .padding(.horizontal, 16)
                }

                Spacer().frame(height: 24)
            }
        }
        .background(colors.background.ignoresSafeArea())
        .sheet(isPresented: $showDateSearch) {
            DateSearchSheet(
                currentDate:     state.selectedDate,
                currentIgboDate: state.igboDate,
                onNavigate:      { viewModel.selectDate($0) },
                onDismiss:       { showDateSearch = false }
            )
            .igboTheme(colors)
            .presentationDetents([.medium, .large])
            .presentationDragIndicator(.visible)
        }
    }
}

// MARK: - App Header

private struct AppHeader: View {
    let onToggleTheme: () -> Void
    let onGoToday:     () -> Void
    let onGoToDate:    () -> Void

    @Environment(\.igboColors) private var colors

    var body: some View {
        HStack {
            VStack(alignment: .leading, spacing: 2) {
                Text("Igbo Calendar")
                    .font(.system(size: 22, weight: .black))
                    .foregroundColor(colors.primary)
                Text("Ọha Igbo")
                    .font(.system(size: 12, weight: .medium))
                    .foregroundColor(colors.onSurfaceMuted)
            }

            Spacer()

            HStack(spacing: 8) {
                // Search button
                CircleButton(icon: "magnifyingglass", action: onGoToDate)

                // Today button
                Button(action: onGoToday) {
                    Text("Today")
                        .font(.system(size: 13, weight: .semibold))
                        .foregroundColor(colors.primary)
                        .padding(.horizontal, 14)
                        .padding(.vertical, 7)
                        .background(
                            RoundedRectangle(cornerRadius: 20)
                                .fill(colors.primaryContainer)
                        )
                }

                // Theme toggle
                Button(action: onToggleTheme) {
                    Text(colors.appTheme == .amber ? "🌙" : "🌕")
                        .font(.system(size: 16))
                        .frame(width: 36, height: 36)
                        .background(Circle().fill(colors.surfaceVariant))
                }
            }
        }
        .padding(.horizontal, 20)
        .padding(.vertical, 16)
    }
}

private struct CircleButton: View {
    let icon:   String
    let action: () -> Void

    @Environment(\.igboColors) private var colors

    var body: some View {
        Button(action: action) {
            Image(systemName: icon)
                .font(.system(size: 14, weight: .medium))
                .foregroundColor(colors.primary)
                .frame(width: 36, height: 36)
                .background(Circle().fill(colors.surfaceVariant))
        }
    }
}

// MARK: - View Toggle Bar

private struct ViewToggleBar: View {
    let currentView:  CalendarViewMode
    let onViewChange: (CalendarViewMode) -> Void

    @Environment(\.igboColors) private var colors

    var body: some View {
        HStack(spacing: 4) {
            ForEach([CalendarViewMode.gregorian, .igboEra], id: \.self) { view in
                let selected = currentView == view
                let label    = view == .gregorian ? "Gregorian" : "Igbo Era"
                Text(label)
                    .font(.system(size: 13, weight: selected ? .bold : .medium))
                    .foregroundColor(selected ? colors.onPrimary : colors.onSurfaceMuted)
                    .frame(maxWidth: .infinity)
                    .padding(.vertical, 9)
                    .background(
                        RoundedRectangle(cornerRadius: 9)
                            .fill(selected ? colors.primary : Color.clear)
                    )
                    .onTapGesture { onViewChange(view) }
            }
        }
        .padding(4)
        .background(RoundedRectangle(cornerRadius: 12).fill(colors.surfaceVariant))
        .padding(.horizontal, 20)
    }
}

// MARK: - Gregorian Panel

private struct GregorianPanel: View {
    let state:          CalendarUiState
    let onDateSelected: (Date) -> Void
    let onPrevMonth:    () -> Void
    let onNextMonth:    () -> Void

    @Environment(\.igboColors) private var colors

    private var monthTitle: String {
        let f = DateFormatter()
        f.dateFormat = "MMMM yyyy"
        return f.string(from: state.gregorianDisplayMonth)
    }

    private var igboSubtitle: String {
        let igbo = IgboCalendarEngine.toIgboDate(state.gregorianDisplayMonth)
        return igbo.isUboshiOmumu ? "Ubọchị Ọmụmụ" : "\(igbo.month) · \(igbo.year)"
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            MonthNavRow(
                title:    monthTitle,
                subtitle: igboSubtitle,
                onPrev:   onPrevMonth,
                onNext:   onNextMonth
            )

            Spacer().frame(height: 12)
            IzuLegendBar()
            Spacer().frame(height: 10)

            GregorianCalendarGrid(
                displayMonth:   state.gregorianDisplayMonth,
                selectedDate:   state.selectedDate,
                onDateSelected: onDateSelected
            )
        }
        .padding(.horizontal, 16)
    }
}

// MARK: - Igbo Era Panel

private struct IgboEraPanel: View {
    let state:          CalendarUiState
    let onDateSelected: (Date) -> Void
    let onPrevMonth:    () -> Void
    let onNextMonth:    () -> Void

    @Environment(\.igboColors) private var colors

    private var anchorIgbo: IgboDate {
        IgboCalendarEngine.toIgboDate(state.igboDisplayAnchor)
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            MonthNavRow(
                title:    anchorIgbo.month,
                subtitle: "Aro \(anchorIgbo.year)  ·  Igbo Era",
                onPrev:   onPrevMonth,
                onNext:   onNextMonth
            )

            Spacer().frame(height: 12)

            if !anchorIgbo.isUboshiOmumu {
                HStack {
                    Text("IZU (4-day week)")
                        .font(.system(size: 11, weight: .semibold))
                        .foregroundColor(colors.onSurfaceMuted)
                    Spacer()
                    Text("7 Izu = 28 days / Ọnwa")
                        .font(.system(size: 11))
                        .foregroundColor(colors.onSurfaceMuted)
                }
                .padding(.horizontal, 12)
                .padding(.vertical, 8)
                .background(RoundedRectangle(cornerRadius: 8).fill(colors.surfaceVariant))

                Spacer().frame(height: 10)
            }

            IgboMonthGrid(
                anchorDate:     state.igboDisplayAnchor,
                selectedDate:   state.selectedDate,
                onDateSelected: onDateSelected
            )
        }
        .padding(.horizontal, 16)
    }
}

// MARK: - Month Navigation Row

struct MonthNavRow: View {
    let title:    String
    let subtitle: String
    let onPrev:   () -> Void
    let onNext:   () -> Void

    @Environment(\.igboColors) private var colors

    var body: some View {
        HStack {
            Button(action: onPrev) {
                Image(systemName: "chevron.left")
                    .font(.system(size: 18, weight: .semibold))
                    .foregroundColor(colors.primary)
                    .frame(width: 40, height: 40)
            }

            Spacer()

            VStack(spacing: 2) {
                Text(title)
                    .font(.system(size: 18, weight: .bold))
                    .foregroundColor(colors.onBackground)
                Text(subtitle)
                    .font(.system(size: 11))
                    .foregroundColor(colors.onSurfaceMuted)
            }

            Spacer()

            Button(action: onNext) {
                Image(systemName: "chevron.right")
                    .font(.system(size: 18, weight: .semibold))
                    .foregroundColor(colors.primary)
                    .frame(width: 40, height: 40)
            }
        }
    }
}

// MARK: - IZU Legend Bar

private struct IzuLegendBar: View {
    @Environment(\.igboColors) private var colors

    var body: some View {
        HStack(spacing: 8) {
            Text("IZU (4-Day Week):")
                .font(.system(size: 10, weight: .semibold))
                .foregroundColor(colors.onSurfaceMuted)

            ForEach(Array(IgboCalendarEngine.igboDays.enumerated()), id: \.offset) { idx, name in
                HStack(spacing: 3) {
                    Circle()
                        .fill(Color.dayColor(idx))
                        .frame(width: 6, height: 6)
                    Text(name)
                        .font(.system(size: 10, weight: .semibold))
                        .foregroundColor(Color.dayColor(idx))
                }
            }
        }
        .padding(.horizontal, 8)
        .padding(.vertical, 6)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(RoundedRectangle(cornerRadius: 8).fill(colors.surfaceVariant))
    }
}

// MARK: - Izu Info Banner

private struct IzuInfoBanner: View {
    let igboDate: IgboDate

    @Environment(\.igboColors) private var colors

    private var izuDesc: IzuInfo? {
        guard igboDate.weekIndex >= 0 else { return nil }
        return IzuData.weeks.first { $0.index == igboDate.weekIndex }
    }

    var body: some View {
        if let izu = izuDesc {
            VStack(alignment: .leading, spacing: 6) {
                HStack {
                    Text(izu.icon).font(.system(size: 18))
                    Text(izu.name)
                        .font(.system(size: 15, weight: .bold))
                        .foregroundColor(colors.onSurface)
                    Spacer()
                    Text(izu.tagline)
                        .font(.system(size: 11, weight: .medium))
                        .foregroundColor(colors.primary)
                }
                Text(izu.description)
                    .font(.system(size: 12))
                    .foregroundColor(colors.onSurfaceMuted)
                    .lineSpacing(4)
            }
            .padding(16)
            .background(
                RoundedRectangle(cornerRadius: 16)
                    .fill(colors.surface)
                    .overlay(
                        RoundedRectangle(cornerRadius: 16)
                            .strokeBorder(colors.divider, lineWidth: 1)
                    )
            )
        }
    }
}

// MARK: - CalendarViewMode Hashable

extension CalendarViewMode: Hashable {}
