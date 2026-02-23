package com.igbocalendar.data

object IzuData {
    val weeks = listOf(
        IzuInfo(
            name = "Izu Mbụ",
            index = 0,
            tagline = "Ritual Cleansing & Preparation",
            description = "The first week of the Igbo month is a time of ritual purification. Communities prepare their hearts, homes, and fields for the month ahead. It is a sacred period of setting intentions and seeking spiritual alignment.",
            icon = "🕊️"
        ),
        IzuInfo(
            name = "Izu Abụọ",
            index = 1,
            tagline = "Farm Clearing & Planting",
            description = "The second week is devoted to agricultural activity. Farmlands are cleared, seeds are planted, and the community cooperates to tend the earth. The spirit of collective labor defines this Izu.",
            icon = "🌱"
        ),
        IzuInfo(
            name = "Izu Atọ",
            index = 2,
            tagline = "Community Gatherings",
            description = "Izu Atọ is the social heartbeat of the month. Families reunite, disputes are settled, and important community decisions are made. It is also a time for cultural performances and storytelling.",
            icon = "🤝"
        ),
        IzuInfo(
            name = "Izu Anọ",
            index = 3,
            tagline = "Major Market Transactions",
            description = "The fourth week sees the busiest trading of the month. Merchants travel from distant communities, goods flow freely, and large commercial agreements are sealed. Wealth and prosperity are the themes of Izu Anọ.",
            icon = "🏺"
        ),
        IzuInfo(
            name = "Izu Ise",
            index = 4,
            tagline = "Rest & Spiritual Reflection",
            description = "A quieter week dedicated to rest, prayer, and communing with the ancestors. Divination is practiced, oracles are consulted, and individuals seek spiritual guidance for their lives and futures.",
            icon = "🔮"
        ),
        IzuInfo(
            name = "Izu Isii",
            index = 5,
            tagline = "Festivals & Masquerades",
            description = "The sixth week is the most celebratory of the month. Masquerades emerge, songs fill the air, and the community honors its cultural heritage through dance, music, and traditional pageantry.",
            icon = "🎭"
        ),
        IzuInfo(
            name = "Izu Asaa",
            index = 6,
            tagline = "Conclusion of the Cycle",
            description = "The final week brings the month to a close. Elders convene to assess the month's events, plan for the season ahead, and in some traditions, consult the stars to determine the next year's farming calendar.",
            icon = "⭐"
        )
    )

    val months = listOf(
        OnwaInfo(
            name = "Ọnwa Mbụ",
            index = 0,
            season = "Dry Season",
            description = "The first month of the Igbo year corresponds to the beginning of the dry season. It is a time of celebration, giving thanks for the previous year's harvest, and setting intentions for the new year.",
            activities = listOf("New Year ceremonies", "Igu Aro proclamation", "Thanksgiving feasts", "Community planning")
        ),
        OnwaInfo(
            name = "Ọnwa Abụọ",
            index = 1,
            season = "Dry Season",
            description = "The second month deepens into the dry season. Preparations for farming begin — tools are repaired, seeds are selected, and land is surveyed. Social visits and marriages are common this month.",
            activities = listOf("Tool preparation", "Seed selection", "Weddings & unions", "Inter-village visits")
        ),
        OnwaInfo(
            name = "Ọnwa Ife Eke",
            index = 2,
            season = "Early Rains",
            description = "Named after the sacred Eke market day, this month heralds the return of rains. The Eke deity is honored with special prayers and offerings as communities prepare their fields for planting.",
            activities = listOf("Rain prayers", "Field preparation", "Eke deity offerings", "Planting ceremonies")
        ),
        OnwaInfo(
            name = "Ọnwa Anọ",
            index = 3,
            season = "Rainy Season",
            description = "The fourth month sees the full arrival of the rainy season. Planting is in full swing, and communities work together to tend their crops. Water spirits are honored during this month.",
            activities = listOf("Active planting", "Communal farming", "Water spirit ceremonies", "Rain festivals")
        ),
        OnwaInfo(
            name = "Ọnwa Agwụ",
            index = 4,
            season = "Rainy Season",
            description = "Named after Agwụ, the deity of medicine and divination, this month is sacred to healers and diviners. Medicinal plants are at their most potent, and healing ceremonies are performed.",
            activities = listOf("Healing ceremonies", "Medicinal plant harvesting", "Divination sessions", "Agwụ deity festivals")
        ),
        OnwaInfo(
            name = "Ọnwa Ifejiọkụ",
            index = 5,
            season = "Mid-Rainy Season",
            description = "Dedicated to Ifejiọkụ, the yam deity. This month is critical for yam cultivation — the most important crop in Igboland. Prayers and offerings to Ifejiọkụ ensure a bountiful harvest.",
            activities = listOf("Yam tending rituals", "Ifejiọkụ offerings", "Farm maintenance", "Mid-year ceremonies")
        ),
        OnwaInfo(
            name = "Ọnwa Alọm Chi",
            index = 6,
            season = "Late Rainy Season",
            description = "A month of spiritual introspection and communion with one's personal deity (Chi). Individuals seek to align with their destiny through prayer, reflection, and consultation with diviners.",
            activities = listOf("Personal shrine rituals", "Chi ceremonies", "Destiny consultations", "Spiritual retreats")
        ),
        OnwaInfo(
            name = "Ọnwa Ilọ Mmụọ",
            index = 7,
            season = "Harvest Season",
            description = "The month of returning spirits. Ancestor veneration reaches its peak as the community honors departed loved ones. Masquerades representing ancestral spirits perform elaborate ceremonies.",
            activities = listOf("Ancestor veneration", "Masquerade festivals", "Mmụọ (spirit) ceremonies", "Libation rituals")
        ),
        OnwaInfo(
            name = "Ọnwa Ana",
            index = 8,
            season = "Harvest Season",
            description = "Named after Ana (Ala), the earth goddess — supreme deity of the Igbo. This harvest month is the most sacred, as gratitude is offered to the earth for its abundance. The harvest festival begins.",
            activities = listOf("Harvest festivals", "Ana/Ala deity ceremonies", "Yam harvest", "Community feasts")
        ),
        OnwaInfo(
            name = "Ọnwa Okike",
            index = 9,
            season = "Harvest Season",
            description = "The month of Okike — the divine force of creation. It celebrates the abundance of the harvest and the creative power of the universe. The Ofala royal festival is often held this month.",
            activities = listOf("Ofala festival", "Royal ceremonies", "Creation celebrations", "Artistic performances")
        ),
        OnwaInfo(
            name = "Ọnwa Ajana",
            index = 10,
            season = "Dry Season Onset",
            description = "As the dry season approaches, communities begin storing food and preparing for the lean months ahead. Marriages are common this month as families have abundant resources to celebrate.",
            activities = listOf("Food preservation", "Marriage ceremonies", "Trade expeditions", "Community storage preparation")
        ),
        OnwaInfo(
            name = "Ọnwa Ede Ajana",
            index = 11,
            season = "Early Dry Season",
            description = "The twelfth month is named for the cocoyam harvest alongside the ongoing dry season preparations. Elders gather to review the year and begin planning for the year ahead.",
            activities = listOf("Cocoyam harvest", "Year-end reviews", "Elder councils", "Gift-giving traditions")
        ),
        OnwaInfo(
            name = "Ọnwa Ụzọ Alụsị",
            index = 12,
            season = "Late Dry Season",
            description = "The final month of the Igbo year — the path of the deities. Shrines are cleaned, deities are propitiated, and the community makes peace with all spiritual forces before the new year begins. Ends with Ubọchị Ọmụmụ.",
            activities = listOf("Shrine cleansing", "Deity propitiation", "Year-end reconciliation", "Ubọchị Ọmụmụ celebration")
        )
    )
}
