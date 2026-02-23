# Igbo Calendar — Android App

A beautiful native Android app built with **Kotlin + Jetpack Compose** that displays the Igbo traditional calendar alongside the Gregorian calendar.

## Features

- **Calendar Screen** — Gregorian month view with Igbo market day labels (EKE/ORIE/AFỌ/NKWỌ) on every date. Toggle to "Igbo Era" view showing the full 7-week Igbo month grid.
- **Date Converter** — Tap any Gregorian date to instantly see the Igbo date (market day, Izu week, Ọnwa month, Aro year).
- **Market Days Screen** — Deep info on all four Igbo market days: significance, deity, direction, and traditional activities.
- **Info Screen** — Educational content on all 7 Izu (weeks) and 13 Ọnwa (months) with expandable cards.
- **Events Screen** — Igbo cultural festivals (Iwa Ji, Ofala, Mmanwu, Igu Aro, etc.).
- **Dual themes** — Amber/Gold and Forest Green, togglable at runtime.

## Igbo Calendar Structure

| Unit | Igbo | Count |
|------|------|-------|
| Day | Ụbọchị | EKE · ORIE · AFỌ · NKWỌ |
| Week | Izu | 4 days |
| Month | Ọnwa | 7 Izu = 28 days |
| Year | Aro | 13 Ọnwa + 1 day = 365 days |

The extra day is **Ubọchị Ọmụmụ** (Sacred New Year Day).

## Calendar Engine Reference Point

- **2023-08-15** = AFỌ, Izu Mbụ, Ọnwa Ilọ Mmụọ
- Igbo Era ≈ Gregorian Year + 5000

## Project Structure

```
app/src/main/java/com/igbocalendar/
├── data/
│   ├── IgboCalendarEngine.kt   # Core calendar conversion logic
│   ├── Models.kt               # Data classes & enums
│   ├── MarketDayData.kt        # Market day information
│   ├── IzuData.kt              # Week & month info
│   └── FestivalData.kt         # Cultural festivals
├── ui/
│   ├── theme/                  # Colors, Typography, Theme
│   ├── components/             # Reusable Compose components
│   └── screens/                # Four main screens
├── viewmodel/
│   └── CalendarViewModel.kt
└── MainActivity.kt
```

## How to Build

1. Open the `android/` folder in Android Studio
2. Sync Gradle
3. Run on emulator or device (minSdk 26, Android 8.0+)
