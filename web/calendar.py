from datetime import date, timedelta

class IgboCalendar:
    IGBO_DAYS = [
        "EKE",
        "ORIE",
        "AFỌ",
        "NKWỌ"
    ]

    IGBO_WEEKS = [
        "Izu Mbụ",  # Week 1
        "Izu Abụọ", # Week 2
        "Izu Atọ",  # Week 3
        "Izu Anọ",  # Week 4
        "Izu Ise",  # Week 5
        "Izu Isii", # Week 6
        "Izu Asaa"  # Week 7
    ]

    IGBO_MONTHS = [
        "Ọnwa Mbụ",  # First Month
        "Ọnwa Abụọ", # Second Month
        "Ọnwa Ife Eke", # Third Month
        "Ọnwa Anọ", # Fourth Month
        "Ọnwa Agwụ", # Fifth Month
        "Ọnwa Ifejiọkụ", # Sixth Month
        "Ọnwa Alọm Chi", # Seventh Month
        "Ọnwa Ilọ Mmụọ", # Eighth Month
        "Ọnwa Ana", # Ninth Month
        "Ọnwa Okike", # Tenth Month
        "Ọnwa Ajana", # Eleventh Month
        "Ọnwa Ede Ajana", # Twelfth Month
        "Ọnwa Ụzọ Alụsị", # Thirteenth Month
    ]

    DAYS_IN_YEAR = (13 * 28) + 1

    # Reference Igbo calendar date
    REFERENCE_DATE = date(2023, 9, 15)  # Gregorian date
    REFERENCE_IGBO_DAY_INDEX = 2  # AFỌ (3rd day)
    REFERENCE_IGBO_WEEK_INDEX = 0  # Izu Mbụ (1st week)
    REFERENCE_IGBO_MONTH_INDEX = 7  # Ọnwa Ilọ Mmụọ (8th month)
    REFERENCE_IGBO_YEAR = 1001

    def __init__(self, gregorian_date):
        self.gregorian_date = gregorian_date

    def to_igbo_date(self):
        # Calculate the difference in days from the reference date
        delta_days = (self.gregorian_date - self.REFERENCE_DATE).days

        # Adjust for the Igbo year cycle
        days_into_year = (7 * 28) + delta_days  # Adjust to the first day of Igbo year 1001
        igbo_year = self.REFERENCE_IGBO_YEAR + (days_into_year // self.DAYS_IN_YEAR)
        remaining_days = days_into_year % self.DAYS_IN_YEAR

        if remaining_days < 0:
            igbo_year -= 1
            remaining_days += self.DAYS_IN_YEAR

        # Calculate the Igbo day
        igbo_day_index = (self.REFERENCE_IGBO_DAY_INDEX + delta_days) % 4
        if igbo_day_index < 0:
            igbo_day_index += 4
        igbo_day = self.IGBO_DAYS[igbo_day_index]

        # Determine if it's Ubọchị Arọ (end of year day)
        if remaining_days == self.DAYS_IN_YEAR - 1:
            return {
                "day": igbo_day,
                "week": "Ubọchị Arọ",
                "month": "Ubọchị Arọ",
                "year": igbo_year
            }

        # Calculate the Igbo month and day within the month
        igbo_month_index = remaining_days // 28
        day_within_month = remaining_days % 28

        # Calculate the Igbo week and day within the week
        igbo_week_index = day_within_month // 4

        igbo_week = self.IGBO_WEEKS[igbo_week_index]
        igbo_month = self.IGBO_MONTHS[igbo_month_index]

        return {
            "day": igbo_day,
            "week": igbo_week,
            "month": igbo_month,
            "year": igbo_year
        }

# Example
date = date(2025, 1, 3)
igbo_calendar = IgboCalendar(date)
print(igbo_calendar.to_igbo_date())
