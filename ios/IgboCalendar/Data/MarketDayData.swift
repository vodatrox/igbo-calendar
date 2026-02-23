import SwiftUI

enum MarketDayData {
    static let days: [MarketDayInfo] = [
        MarketDayInfo(
            id: 0,
            name: "EKE",
            index: 0,
            tagline: "Day of Creation & Earth",
            significance: "Eke is the most sacred of all market days — the day of creation and new beginnings. It is associated with the earth deity Eke, who governs life, fertility, and the land itself. Communities treat this day with deep reverence.",
            direction: "East",
            deity: "Eke (Earth Deity)",
            activities: [
                "Major market transactions",
                "Ritual cleansing ceremonies",
                "New business ventures",
                "Planting & harvest ceremonies",
                "Birth and naming rituals"
            ],
            color: Color(hex: 0xD4900A),
            icon: "🌅"
        ),
        MarketDayInfo(
            id: 1,
            name: "ORIE",
            index: 1,
            tagline: "Day of Sky & Community",
            significance: "Orie is associated with the sky and communal harmony. It is a day for gathering, resolving disputes, and strengthening the bonds of the community. The spirit of Orie encourages cooperation and justice.",
            direction: "West",
            deity: "Orie (Sky Spirit)",
            activities: [
                "Community meetings",
                "Dispute resolution & justice",
                "Trading in livestock",
                "Village council sessions",
                "Communal work parties"
            ],
            color: Color(hex: 0x1565C0),
            icon: "🌤️"
        ),
        MarketDayInfo(
            id: 2,
            name: "AFỌ",
            index: 2,
            tagline: "Day of Rest & Farming",
            significance: "Afọ is a sacred day of rest and reflection, especially for farming communities. It honors the agricultural cycle and the spirits that govern the land's fertility. On Afọ, heavy physical labor is traditionally avoided.",
            direction: "North",
            deity: "Afọ (Farm & Rest Deity)",
            activities: [
                "Rest and spiritual reflection",
                "Farm planning & consulting elders",
                "Ancestral remembrance",
                "Herbal medicine preparation",
                "Quiet family gatherings"
            ],
            color: Color(hex: 0x2E7D32),
            icon: "🌿"
        ),
        MarketDayInfo(
            id: 3,
            name: "NKWỌ",
            index: 3,
            tagline: "Day of Completion & Cycles",
            significance: "Nkwọ marks the conclusion of the Izu cycle. It is a powerful day of endings and new beginnings — a time to settle accounts, prepare for the next cycle, and honor the continuity of Igbo tradition.",
            direction: "South",
            deity: "Nkwọ (Cycle Deity)",
            activities: [
                "Cycle conclusion ceremonies",
                "Settling of debts & accounts",
                "Preparation for new Izu",
                "Major masquerade festivals",
                "Elders' strategic gatherings"
            ],
            color: Color(hex: 0x7B1FA2),
            icon: "🌙"
        )
    ]
}
