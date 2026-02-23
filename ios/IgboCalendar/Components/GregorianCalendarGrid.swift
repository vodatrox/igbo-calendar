import SwiftUI

private let dayHeaders = ["SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"]

// MARK: - Gregorian Calendar Grid

struct GregorianCalendarGrid: View {
    let displayMonth: Date
    let selectedDate:  Date
    var today:         Date = .today
    let onDateSelected: (Date) -> Void

    @Environment(\.igboColors) private var colors

    private var cells: [Date?] {
        let first      = displayMonth.firstOfMonth
        let offset     = first.weekdayIndex          // Sun = 0
        let daysCount  = displayMonth.daysInMonth

        var result: [Date?] = Array(repeating: nil, count: offset)
        for d in 1...daysCount {
            result.append(IgboCalendarEngine.adding(days: d - 1, to: first))
        }
        while result.count % 7 != 0 { result.append(nil) }
        return result
    }

    var body: some View {
        VStack(spacing: 0) {
            // Day-of-week headers
            HStack(spacing: 0) {
                ForEach(dayHeaders, id: \.self) { header in
                    Text(header)
                        .frame(maxWidth: .infinity)
                        .font(.system(size: 11, weight: .semibold))
                        .foregroundColor(colors.onSurfaceMuted)
                }
            }

            Spacer().frame(height: 8)

            // Calendar rows
            let weeks = cells.chunked(into: 7)
            ForEach(Array(weeks.enumerated()), id: \.offset) { _, week in
                HStack(spacing: 0) {
                    ForEach(0..<7, id: \.self) { col in
                        CalendarDayCell(
                            date:       week[col],
                            isSelected: week[col].map { IgboCalendarEngine.startOfDay($0) == IgboCalendarEngine.startOfDay(selectedDate) } ?? false,
                            isToday:    week[col].map { IgboCalendarEngine.startOfDay($0) == IgboCalendarEngine.startOfDay(today) } ?? false,
                            onSelect:   { if let d = week[col] { onDateSelected(d) } }
                        )
                    }
                }
            }
        }
    }
}

// MARK: - Day Cell

private struct CalendarDayCell: View {
    let date:       Date?
    let isSelected: Bool
    let isToday:    Bool
    let onSelect:   () -> Void

    @Environment(\.igboColors) private var colors

    private var igboDay: IgboDate? { date.map { IgboCalendarEngine.toIgboDate($0) } }
    private var accent:  Color     { Color.dayColor(igboDay?.dayIndex ?? 0) }

    var body: some View {
        GeometryReader { geo in
            let size = min(geo.size.width, geo.size.height)

            ZStack {
                if let date = date {
                    // Background
                    RoundedRectangle(cornerRadius: 10)
                        .fill(isSelected ? accent : Color.clear)
                        .padding(2)

                    // Today border
                    if isToday && !isSelected {
                        RoundedRectangle(cornerRadius: 10)
                            .strokeBorder(accent, lineWidth: 1.5)
                            .padding(2)
                    }

                    // Content
                    VStack(spacing: 1) {
                        Text("\(date.dayOfMonth)")
                            .font(.system(size: 14, weight: isSelected || isToday ? .bold : .medium))
                            .foregroundColor(
                                isSelected ? Color.black.opacity(0.85) :
                                isToday    ? accent :
                                             colors.onSurface
                            )

                        if let igbo = igboDay {
                            Text(igbo.day)
                                .font(.system(size: 7, weight: .semibold))
                                .foregroundColor(
                                    isSelected ? Color.black.opacity(0.65) : accent
                                )
                                .lineLimit(1)
                                .minimumScaleFactor(0.5)
                        }
                    }
                    .onTapGesture { onSelect() }
                }
            }
            .frame(width: size, height: size)
            .frame(maxWidth: .infinity, maxHeight: .infinity)
        }
        .aspectRatio(1, contentMode: .fit)
    }
}

// MARK: - Array Chunk Extension

extension Array {
    func chunked(into size: Int) -> [[Element]] {
        stride(from: 0, to: count, by: size).map {
            Array(self[$0..<Swift.min($0 + size, count)])
        }
    }
}
