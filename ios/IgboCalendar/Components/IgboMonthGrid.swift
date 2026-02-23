import SwiftUI

// MARK: - Igbo Month Grid

/// Displays an Igbo month as a 7-week × 4-day grid.
/// Each row = one Izu (week), each column = one market day (EKE, ORIE, AFỌ, NKWỌ).
struct IgboMonthGrid: View {
    let anchorDate:     Date
    let selectedDate:   Date
    var today:          Date = .today
    let onDateSelected: (Date) -> Void

    @Environment(\.igboColors) private var colors

    private var igboAnchor: IgboDate {
        IgboCalendarEngine.toIgboDate(anchorDate)
    }

    var body: some View {
        if igboAnchor.isUboshiOmumu {
            UboshiOmumuDisplay(
                igboDate:   igboAnchor,
                isSelected: IgboCalendarEngine.startOfDay(anchorDate) == IgboCalendarEngine.startOfDay(selectedDate),
                isToday:    IgboCalendarEngine.startOfDay(anchorDate) == IgboCalendarEngine.startOfDay(today),
                onSelect:   { onDateSelected(anchorDate) }
            )
        } else {
            let yearBase   = igboAnchor.year - IgboCalendarEngine.igboEraOffset
            let monthDates = IgboCalendarEngine.getIgboMonthDates(igboYearBase: yearBase, monthIndex: igboAnchor.monthIndex)

            VStack(spacing: 0) {
                // Column headers
                HStack(spacing: 0) {
                    Spacer().frame(width: 54)
                    ForEach(Array(IgboCalendarEngine.igboDays.enumerated()), id: \.offset) { idx, dayName in
                        Text(dayName)
                            .frame(maxWidth: .infinity)
                            .font(.system(size: 11, weight: .bold))
                            .foregroundColor(Color.dayColor(idx))
                    }
                }

                Spacer().frame(height: 6)

                // 7 weeks × 4 days
                ForEach(Array(monthDates.chunked(into: 4).enumerated()), id: \.offset) { weekIdx, weekDates in
                    let weekColor = izuColors[weekIdx]
                    HStack(spacing: 0) {
                        // Week label
                        Text(IgboCalendarEngine.igboWeeks[weekIdx]
                                .replacingOccurrences(of: "Izu ", with: "Izu\n"))
                            .frame(width: 54, alignment: .leading)
                            .font(.system(size: 9, weight: .semibold))
                            .foregroundColor(weekColor.opacity(0.8))
                            .lineLimit(2)
                            .multilineTextAlignment(.leading)
                            .padding(.vertical, 2)

                        ForEach(Array(weekDates.enumerated()), id: \.offset) { dayIdx, date in
                            IgboMonthCell(
                                date:       date,
                                dayIndex:   dayIdx,
                                weekColor:  weekColor,
                                isSelected: IgboCalendarEngine.startOfDay(date) == IgboCalendarEngine.startOfDay(selectedDate),
                                isToday:    IgboCalendarEngine.startOfDay(date) == IgboCalendarEngine.startOfDay(today),
                                onSelect:   { onDateSelected(date) }
                            )
                        }
                    }
                    .padding(.vertical, 2)
                }
            }
        }
    }
}

// MARK: - Igbo Month Cell

private struct IgboMonthCell: View {
    let date:       Date
    let dayIndex:   Int
    let weekColor:  Color
    let isSelected: Bool
    let isToday:    Bool
    let onSelect:   () -> Void

    @Environment(\.igboColors) private var colors

    private var accent: Color { Color.dayColor(dayIndex) }

    var body: some View {
        GeometryReader { geo in
            let size = min(geo.size.width, geo.size.height)
            ZStack {
                RoundedRectangle(cornerRadius: 8)
                    .fill(
                        isSelected ? accent :
                        isToday    ? Color.clear :
                                     weekColor.opacity(0.08)
                    )
                    .padding(2)

                if isToday && !isSelected {
                    RoundedRectangle(cornerRadius: 8)
                        .strokeBorder(accent, lineWidth: 1.5)
                        .padding(2)
                }

                Text("\(date.dayOfMonth)")
                    .font(.system(size: 12, weight: isSelected || isToday ? .bold : .medium))
                    .foregroundColor(
                        isSelected ? Color.black.opacity(0.85) :
                        isToday    ? accent :
                                     colors.onSurface
                    )
            }
            .frame(width: size, height: size)
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            .onTapGesture { onSelect() }
        }
        .aspectRatio(1, contentMode: .fit)
    }
}

// MARK: - Ubọchị Ọmụmụ Display

private struct UboshiOmumuDisplay: View {
    let igboDate:   IgboDate
    let isSelected: Bool
    let isToday:    Bool
    let onSelect:   () -> Void

    @Environment(\.igboColors) private var colors

    private var accent: Color { Color.dayColor(igboDate.dayIndex) }

    var body: some View {
        ZStack {
            RoundedRectangle(cornerRadius: 16)
                .fill(
                    isSelected ? accent.opacity(0.18) :
                                 colors.primaryContainer
                )

            if isSelected || isToday {
                RoundedRectangle(cornerRadius: 16)
                    .strokeBorder(accent, lineWidth: 2)
            }

            VStack(spacing: 8) {
                Text("✨").font(.system(size: 36))

                Text("Ubọchị Ọmụmụ")
                    .font(.system(size: 20, weight: .bold))
                    .foregroundColor(colors.primary)

                Text("No Ọnwa · No Izu")
                    .font(.system(size: 12))
                    .foregroundColor(colors.onSurfaceMuted)
                    .multilineTextAlignment(.center)

                Spacer().frame(height: 8)

                Text(igboDate.day)
                    .font(.system(size: 32, weight: .black))
                    .foregroundColor(accent)
                    .padding(.horizontal, 20)
                    .padding(.vertical, 10)
                    .background(
                        RoundedRectangle(cornerRadius: 12)
                            .fill(accent.opacity(0.15))
                    )
            }
            .padding(32)
        }
        .onTapGesture { onSelect() }
    }
}
