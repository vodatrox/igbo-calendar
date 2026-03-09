package com.igbocalendar.data

import androidx.compose.ui.graphics.Color

object MarketDayData {
    val days = listOf(
        MarketDayInfo(
            name = "EKE",
            index = 0,
            tagline = "Fire of Creation & East",
            significance = "Eke is the primal force of fire and creation — the most sacred of all market days. Governing the East where the sun rises, Eke channels the energy of initiation, vitality, and transformation. Its fierce fire energy drives new beginnings and sparks all of existence. Certain spiritual rituals thrive on Eke, though its intense energy makes it inauspicious for travel or settling disputes.",
            element = "Fire",
            direction = "East",
            deity = "Eke (Spirit of Fire)",
            activities = listOf(
                "Ritual offerings & spiritual ceremonies",
                "New business ventures & initiations",
                "Birth and naming ceremonies",
                "Major market transactions",
                "Transformative rites of passage"
            ),
            color = Color(0xFFD4900A),
            icon = "🔥"
        ),
        MarketDayInfo(
            name = "ORIE",
            index = 1,
            tagline = "Waters of Healing & West",
            significance = "Orie embodies the fluid, healing power of water, flowing from the West where the sun sets. Under the guardianship of Nne Mmiri — the great water mother — this day favors nurturing tasks, cleansing rituals, and emotional reflection. It is a time for community care, spiritual purification, and the healing of bonds. Orie's gentle waters soothe conflicts and deepen communal harmony.",
            element = "Water",
            direction = "West",
            deity = "Nne Mmiri (Water Mother)",
            activities = listOf(
                "Cleansing & purification rituals",
                "Nne Mmiri (water deity) offerings",
                "Community healing & care",
                "Communal gatherings & bonding",
                "Introspection & emotional healing"
            ),
            color = Color(0xFF1565C0),
            icon = "💧"
        ),
        MarketDayInfo(
            name = "AFỌ",
            index = 2,
            tagline = "Earth of Abundance & North",
            significance = "Afọ embodies the grounding power of the earth, anchored in the North. Presided over by Ala — the sacred earth goddess and guardian of Igbo morality — this day promotes stability, fertility, and material prosperity. Agriculture, trade, and building flourish on Afọ. It is a day to sow, consolidate, and harvest, honoring the soil that sustains and nourishes all life.",
            element = "Earth",
            direction = "North",
            deity = "Ala (Earth Goddess)",
            activities = listOf(
                "Agriculture, planting & harvest",
                "Ala (earth goddess) offerings",
                "Trade & commercial transactions",
                "Building & construction work",
                "Fertility and ancestral rites"
            ),
            color = Color(0xFF2E7D32),
            icon = "🌿"
        ),
        MarketDayInfo(
            name = "NKWỌ",
            index = 3,
            tagline = "Air of Wisdom & South",
            significance = "Nkwọ signifies the expansive force of air — the invisible breath that carries thought, words, and ancestral spirit. Linked to the South, it governs intellect, communication, and movement. The ideal day for long-distance trade, philosophical discourse, and spiritual seeking, Nkwọ's winds bring clarity and foresight. It marks the conclusion of the Izu cycle, preparing the community for renewal.",
            element = "Air",
            direction = "South",
            deity = "Nkwọ (Wind Spirit)",
            activities = listOf(
                "Long-distance travel & trade",
                "Intellectual discourse & learning",
                "Divination & spiritual seeking",
                "Cycle conclusion ceremonies",
                "Masquerade festivals & ancestral celebrations"
            ),
            color = Color(0xFF7B1FA2),
            icon = "💨"
        )
    )
}
