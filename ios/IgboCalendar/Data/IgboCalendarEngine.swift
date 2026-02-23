import Foundation

/// Igbo Calendar Conversion Engine
///
/// Reference anchor: September 15, 2023 = AFỌ (index 2), Izu Mbụ (index 0),
/// Ọnwa Ilọ Mmụọ (index 7), Igbo Year 7023.
///
/// Calendar structure:
///  - 4 days  → 1 Izu (week):  EKE, ORIE, AFỌ, NKWỌ
///  - 7 weeks → 1 Ọnwa (month): 28 days
///  - 13 months + 1 day → 1 Aro (year): 365 days
///  - The extra day is "Ubọchị Ọmụmụ" (New Year day)
///
/// Igbo Era offset: Gregorian year + 5000 ≈ Igbo year display
enum IgboCalendarEngine {

    static let igboDays   = ["EKE", "ORIE", "AFỌ", "NKWỌ"]

    static let igboWeeks  = [
        "Izu Mbụ", "Izu Abụọ", "Izu Atọ", "Izu Anọ",
        "Izu Ise",  "Izu Isii",  "Izu Asaa"
    ]

    static let igboMonths = [
        "Ọnwa Mbụ",       "Ọnwa Abụọ",      "Ọnwa Ife Eke",   "Ọnwa Anọ",
        "Ọnwa Agwụ",      "Ọnwa Ifejiọkụ",  "Ọnwa Alọm Chi",
        "Ọnwa Ilọ Mmụọ",  "Ọnwa Ana",       "Ọnwa Okike",
        "Ọnwa Ajana",     "Ọnwa Ede Ajana", "Ọnwa Ụzọ Alụsị"
    ]

    static let igboEraOffset = 5000
    static let daysInYear    = 13 * 28 + 1  // 365

    // Reference: September 15, 2023 = AFỌ, Izu Mbụ, Ọnwa Ilọ Mmụọ, year 7023
    private static let referenceDate: Date = {
        var c = DateComponents()
        c.year = 2023; c.month = 9; c.day = 15
        return Calendar.current.date(from: c)!
    }()

    private static let referenceDayIndex       = 2    // AFỌ
    private static let referenceMonthIndex     = 7    // Ọnwa Ilọ Mmụọ (0-indexed)
    private static let referenceYearBase       = 2023
    private static let referenceDaysIntoYear   = 7 * 28  // 196

    // MARK: - Arithmetic Helpers

    /// Integer division rounding toward negative infinity (mirrors Java's Math.floorDiv)
    private static func floorDiv(_ a: Int, _ b: Int) -> Int {
        let q = a / b
        return (a % b != 0 && (a < 0) != (b < 0)) ? q - 1 : q
    }

    /// Modulo always returning a non-negative result (mirrors Java's Math.floorMod)
    private static func floorMod(_ a: Int, _ b: Int) -> Int {
        let r = a % b
        return (r != 0 && (a < 0) != (b < 0)) ? r + b : r
    }

    // MARK: - Date Helpers

    private static let gregorianCalendar: Calendar = {
        var cal = Calendar(identifier: .gregorian)
        cal.timeZone = TimeZone.current
        return cal
    }()

    private static func daysBetween(_ from: Date, _ to: Date) -> Int {
        let fromDay = gregorianCalendar.startOfDay(for: from)
        let toDay   = gregorianCalendar.startOfDay(for: to)
        return gregorianCalendar.dateComponents([.day], from: fromDay, to: toDay).day ?? 0
    }

    static func adding(days: Int, to date: Date) -> Date {
        gregorianCalendar.date(byAdding: .day, value: days, to: date) ?? date
    }

    static func startOfDay(_ date: Date) -> Date {
        gregorianCalendar.startOfDay(for: date)
    }

    // MARK: - Core Conversion

    /// Converts a Gregorian date to its corresponding IgboDate.
    static func toIgboDate(_ date: Date) -> IgboDate {
        let deltaDays    = daysBetween(referenceDate, date)
        let daysIntoYear = referenceDaysIntoYear + deltaDays

        let igboYearBase  = referenceYearBase + floorDiv(daysIntoYear, daysInYear)
        let remainingDays = floorMod(daysIntoYear, daysInYear)
        let igboDayIndex  = floorMod(referenceDayIndex + deltaDays, 4)

        // Last day of the Igbo year = Ubọchị Ọmụmụ
        if remainingDays == daysInYear - 1 {
            return IgboDate(
                day:            igboDays[igboDayIndex],
                dayIndex:       igboDayIndex,
                week:           "Ubọchị Ọmụmụ",
                weekIndex:      -1,
                month:          "Ubọchị Ọmụmụ",
                monthIndex:     -1,
                year:           igboYearBase + igboEraOffset,
                dayWithinMonth: -1,
                isUboshiOmumu:  true
            )
        }

        let monthIndex     = remainingDays / 28
        let dayWithinMonth = remainingDays % 28
        let weekIndex      = dayWithinMonth / 4

        return IgboDate(
            day:            igboDays[igboDayIndex],
            dayIndex:       igboDayIndex,
            week:           igboWeeks[weekIndex],
            weekIndex:      weekIndex,
            month:          igboMonths[monthIndex],
            monthIndex:     monthIndex,
            year:           igboYearBase + igboEraOffset,
            dayWithinMonth: dayWithinMonth,
            isUboshiOmumu:  false
        )
    }

    // MARK: - Month Utilities

    /// Returns the first Gregorian date of the given Igbo year base and month index.
    static func firstGregorianDateOfIgboMonth(igboYearBase: Int, monthIndex: Int) -> Date {
        let daysIntoYear = monthIndex * 28
        let yearsFromRef = igboYearBase - referenceYearBase
        let deltaDays    = yearsFromRef * daysInYear + daysIntoYear - referenceDaysIntoYear
        return adding(days: deltaDays, to: referenceDate)
    }

    /// Returns all 28 Gregorian dates of the specified Igbo month.
    static func getIgboMonthDates(igboYearBase: Int, monthIndex: Int) -> [Date] {
        let first = firstGregorianDateOfIgboMonth(igboYearBase: igboYearBase, monthIndex: monthIndex)
        return (0..<28).map { adding(days: $0, to: first) }
    }

    /// Returns the Gregorian date of Ubọchị Ọmụmụ for the given Igbo year base.
    static func uboshiOmumuDate(igboYearBase: Int) -> Date {
        let deltaDays = (igboYearBase - referenceYearBase) * daysInYear
                      + (daysInYear - 1) - referenceDaysIntoYear
        return adding(days: deltaDays, to: referenceDate)
    }

    // MARK: - Navigation

    /// Returns a date in the previous Igbo month relative to the given date.
    static func previousIgboMonth(_ date: Date) -> Date {
        let igbo     = toIgboDate(date)
        let yearBase = igbo.year - igboEraOffset
        if igbo.isUboshiOmumu {
            return firstGregorianDateOfIgboMonth(igboYearBase: yearBase, monthIndex: 12)
        } else if igbo.monthIndex <= 0 {
            return firstGregorianDateOfIgboMonth(igboYearBase: yearBase - 1, monthIndex: 12)
        } else {
            return firstGregorianDateOfIgboMonth(igboYearBase: yearBase, monthIndex: igbo.monthIndex - 1)
        }
    }

    /// Returns a date in the next Igbo month relative to the given date.
    static func nextIgboMonth(_ date: Date) -> Date {
        let igbo     = toIgboDate(date)
        let yearBase = igbo.year - igboEraOffset
        if igbo.isUboshiOmumu {
            return firstGregorianDateOfIgboMonth(igboYearBase: yearBase + 1, monthIndex: 0)
        } else if igbo.monthIndex >= 12 {
            return uboshiOmumuDate(igboYearBase: yearBase)
        } else {
            return firstGregorianDateOfIgboMonth(igboYearBase: yearBase, monthIndex: igbo.monthIndex + 1)
        }
    }
}

// MARK: - Date Extension

extension Date {
    /// Normalises to the start of the calendar day.
    var normalized: Date { IgboCalendarEngine.startOfDay(self) }

    static var today: Date { Date().normalized }

    /// Day of month (1-based).
    var dayOfMonth: Int {
        Calendar.current.component(.day, from: self)
    }

    /// Weekday index where Sunday = 0, Monday = 1, … Saturday = 6.
    var weekdayIndex: Int {
        Calendar.current.component(.weekday, from: self) - 1
    }

    /// Number of days in the month this date belongs to.
    var daysInMonth: Int {
        Calendar.current.range(of: .day, in: .month, for: self)?.count ?? 30
    }

    /// First day of the month for this date.
    var firstOfMonth: Date {
        let cal = Calendar.current
        let components = cal.dateComponents([.year, .month], from: self)
        return cal.date(from: components)!
    }

    /// Advance by the given number of months.
    func addingMonths(_ n: Int) -> Date {
        Calendar.current.date(byAdding: .month, value: n, to: self) ?? self
    }

    /// Year component.
    var year: Int { Calendar.current.component(.year, from: self) }

    /// Month component (1-based).
    var month: Int { Calendar.current.component(.month, from: self) }
}
