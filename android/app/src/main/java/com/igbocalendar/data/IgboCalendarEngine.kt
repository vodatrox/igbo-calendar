package com.igbocalendar.data

import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * Igbo Calendar Conversion Engine
 *
 * Reference anchor: September 15, 2023 = AFỌ (index 2), Izu Mbụ (index 0),
 * Ọnwa Ilọ Mmụọ (index 7), Igbo Year 7023.
 *
 * Calendar structure:
 *  - 4 days  → 1 Izu (week):  EKE, ORIE, AFỌ, NKWỌ
 *  - 7 weeks → 1 Ọnwa (month): 28 days
 *  - 13 months + 1 day → 1 Aro (year): 365 days
 *  - The extra day is "Ubọchị Ọmụmụ" (New Year day)
 *
 * Igbo Era offset: Gregorian year + 5000 ≈ Igbo year display
 */
object IgboCalendarEngine {

    val IGBO_DAYS = listOf("EKE", "ORIE", "AFỌ", "NKWỌ")

    val IGBO_WEEKS = listOf(
        "Izu Mbụ", "Izu Abụọ", "Izu Atọ", "Izu Anọ",
        "Izu Ise", "Izu Isii", "Izu Asaa"
    )

    val IGBO_MONTHS = listOf(
        "Ọnwa Mbụ", "Ọnwa Abụọ", "Ọnwa Ife Eke", "Ọnwa Anọ",
        "Ọnwa Agwụ", "Ọnwa Ifejiọkụ", "Ọnwa Alọm Chi",
        "Ọnwa Ilọ Mmụọ", "Ọnwa Ana", "Ọnwa Okike",
        "Ọnwa Ajana", "Ọnwa Ede Ajana", "Ọnwa Ụzọ Alụsị"
    )

    private val REFERENCE_DATE = LocalDate.of(2023, 9, 15)
    private const val REFERENCE_DAY_INDEX = 2      // AFỌ
    private const val REFERENCE_MONTH_INDEX = 7    // Ọnwa Ilọ Mmụọ (0-indexed)
    private const val REFERENCE_YEAR_BASE = 2023
    const val IGBO_ERA_OFFSET = 5000
    const val DAYS_IN_YEAR = 13 * 28 + 1           // 365

    // The reference date is day-0 of Ọnwa Ilọ Mmụọ, so days-into-year = 7 * 28 = 196
    private val REFERENCE_DAYS_INTO_YEAR = REFERENCE_MONTH_INDEX * 28

    fun toIgboDate(date: LocalDate): IgboDate {
        val deltaDays = ChronoUnit.DAYS.between(REFERENCE_DATE, date).toInt()
        val daysIntoYear = REFERENCE_DAYS_INTO_YEAR + deltaDays

        val igboYearBase = REFERENCE_YEAR_BASE + Math.floorDiv(daysIntoYear, DAYS_IN_YEAR)
        val remainingDays = Math.floorMod(daysIntoYear, DAYS_IN_YEAR)
        val igboDayIndex = Math.floorMod(REFERENCE_DAY_INDEX + deltaDays, 4)

        // Last day of the Igbo year = Ubọchị Ọmụmụ
        if (remainingDays == DAYS_IN_YEAR - 1) {
            return IgboDate(
                day = IGBO_DAYS[igboDayIndex],
                dayIndex = igboDayIndex,
                week = "Ubọchị Ọmụmụ",
                weekIndex = -1,
                month = "Ubọchị Ọmụmụ",
                monthIndex = -1,
                year = igboYearBase + IGBO_ERA_OFFSET,
                dayWithinMonth = -1,
                isUboshiOmumu = true
            )
        }

        val monthIndex = remainingDays / 28
        val dayWithinMonth = remainingDays % 28
        val weekIndex = dayWithinMonth / 4

        return IgboDate(
            day = IGBO_DAYS[igboDayIndex],
            dayIndex = igboDayIndex,
            week = IGBO_WEEKS[weekIndex],
            weekIndex = weekIndex,
            month = IGBO_MONTHS[monthIndex],
            monthIndex = monthIndex,
            year = igboYearBase + IGBO_ERA_OFFSET,
            dayWithinMonth = dayWithinMonth,
            isUboshiOmumu = false
        )
    }

    /** Returns the first LocalDate of an Igbo month given the Igbo year base and month index. */
    fun firstGregorianDateOfIgboMonth(igboYearBase: Int, monthIndex: Int): LocalDate {
        val daysIntoYear = monthIndex * 28
        // daysIntoYear = REFERENCE_DAYS_INTO_YEAR + delta => delta = daysIntoYear - REFERENCE_DAYS_INTO_YEAR
        val yearsFromRef = igboYearBase - REFERENCE_YEAR_BASE
        val deltaDays = (yearsFromRef * DAYS_IN_YEAR) + daysIntoYear - REFERENCE_DAYS_INTO_YEAR
        return REFERENCE_DATE.plusDays(deltaDays.toLong())
    }

    /** Get all 28 LocalDates of an Igbo month. */
    fun getIgboMonthDates(igboYearBase: Int, monthIndex: Int): List<LocalDate> {
        val first = firstGregorianDateOfIgboMonth(igboYearBase, monthIndex)
        return (0 until 28).map { first.plusDays(it.toLong()) }
    }

    /** Returns the Gregorian date of Ubọchị Ọmụmụ for a given Igbo year base. */
    fun uboshiOmumuDate(igboYearBase: Int): LocalDate {
        val deltaDays = (igboYearBase - REFERENCE_YEAR_BASE).toLong() * DAYS_IN_YEAR +
                        (DAYS_IN_YEAR - 1) - REFERENCE_DAYS_INTO_YEAR
        return REFERENCE_DATE.plusDays(deltaDays)
    }

    /** Navigate to the previous Igbo month given a date. */
    fun previousIgboMonth(date: LocalDate): LocalDate {
        val igbo = toIgboDate(date)
        val yearBase = igbo.year - IGBO_ERA_OFFSET
        return when {
            // Ubọchị Ọmụmụ is the last day of the year — go back to month 12 of same year
            igbo.isUboshiOmumu -> firstGregorianDateOfIgboMonth(yearBase, 12)
            igbo.monthIndex <= 0 -> firstGregorianDateOfIgboMonth(yearBase - 1, 12)
            else -> firstGregorianDateOfIgboMonth(yearBase, igbo.monthIndex - 1)
        }
    }

    /** Navigate to the next Igbo month given a date. */
    fun nextIgboMonth(date: LocalDate): LocalDate {
        val igbo = toIgboDate(date)
        val yearBase = igbo.year - IGBO_ERA_OFFSET
        return when {
            // Ubọchị Ọmụmụ is the last day of the year — go forward to month 0 of next year
            igbo.isUboshiOmumu -> firstGregorianDateOfIgboMonth(yearBase + 1, 0)
            igbo.monthIndex >= 12 -> uboshiOmumuDate(yearBase)
            else -> firstGregorianDateOfIgboMonth(yearBase, igbo.monthIndex + 1)
        }
    }
}
