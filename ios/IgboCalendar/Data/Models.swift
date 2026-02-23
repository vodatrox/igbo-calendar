import SwiftUI

// MARK: - Core Calendar Model

struct IgboDate: Equatable {
    let day: String           // "EKE", "ORIE", "AFỌ", "NKWỌ"
    let dayIndex: Int         // 0–3
    let week: String          // "Izu Mbụ" etc., or "Ubọchị Ọmụmụ"
    let weekIndex: Int        // 0–6, or -1 for Ubọchị Ọmụmụ
    let month: String         // "Ọnwa Ilọ Mmụọ" etc., or "Ubọchị Ọmụmụ"
    let monthIndex: Int       // 0–12, or -1 for Ubọchị Ọmụmụ
    let year: Int             // Igbo display year (Gregorian base + 5000)
    let dayWithinMonth: Int   // 0–27, or -1 for Ubọchị Ọmụmụ
    let isUboshiOmumu: Bool
}

// MARK: - Market Day

struct MarketDayInfo: Identifiable {
    let id: Int
    let name: String
    let index: Int
    let tagline: String
    let significance: String
    let direction: String
    let deity: String
    let activities: [String]
    let color: Color
    let icon: String
}

// MARK: - Izu (Week)

struct IzuInfo: Identifiable {
    let id: Int
    let name: String
    let index: Int
    let tagline: String
    let description: String
    let icon: String
}

// MARK: - Ọnwa (Month)

struct OnwaInfo: Identifiable {
    let id: Int
    let name: String
    let index: Int
    let season: String
    let description: String
    let activities: [String]
}

// MARK: - Festival

struct Festival: Identifiable {
    let id: Int
    let name: String
    let subtitle: String
    let description: String
    let marketDay: String
    let igboMonth: String
    let location: String
    let featured: Bool
}

// MARK: - App Enums

enum AppTheme {
    case amber, green
}

enum AppTab: Hashable {
    case calendar, market, info, events
}

enum CalendarViewMode {
    case gregorian, igboEra
}
