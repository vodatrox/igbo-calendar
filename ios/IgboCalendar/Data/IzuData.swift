import Foundation

enum IzuData {

    static let weeks: [IzuInfo] = [
        IzuInfo(
            id: 0,
            name: "Izu Mbụ",
            index: 0,
            tagline: "Ritual Cleansing & Preparation",
            description: "The first week of the Igbo month is a time of ritual purification. Communities prepare their hearts, homes, and fields for the month ahead. It is a sacred period of setting intentions and seeking spiritual alignment.",
            icon: "🕊️"
        ),
        IzuInfo(
            id: 1,
            name: "Izu Abụọ",
            index: 1,
            tagline: "Farm Clearing & Planting",
            description: "The second week is devoted to agricultural activity. Farmlands are cleared, seeds are planted, and the community cooperates to tend the earth. The spirit of collective labor defines this Izu.",
            icon: "🌱"
        ),
        IzuInfo(
            id: 2,
            name: "Izu Atọ",
            index: 2,
            tagline: "Community Gatherings",
            description: "Izu Atọ is the social heartbeat of the month. Families reunite, disputes are settled, and important community decisions are made. It is also a time for cultural performances and storytelling.",
            icon: "🤝"
        ),
        IzuInfo(
            id: 3,
            name: "Izu Anọ",
            index: 3,
            tagline: "Major Market Transactions",
            description: "The fourth week sees the busiest trading of the month. Merchants travel from distant communities, goods flow freely, and large commercial agreements are sealed. Wealth and prosperity are the themes of Izu Anọ.",
            icon: "🏺"
        ),
        IzuInfo(
            id: 4,
            name: "Izu Ise",
            index: 4,
            tagline: "Rest & Spiritual Reflection",
            description: "A quieter week dedicated to rest, prayer, and communing with the ancestors. Divination is practiced, oracles are consulted, and individuals seek spiritual guidance for their lives and futures.",
            icon: "🔮"
        ),
        IzuInfo(
            id: 5,
            name: "Izu Isii",
            index: 5,
            tagline: "Festivals & Masquerades",
            description: "The sixth week is the most celebratory of the month. Masquerades emerge, songs fill the air, and the community honors its cultural heritage through dance, music, and traditional pageantry.",
            icon: "🎭"
        ),
        IzuInfo(
            id: 6,
            name: "Izu Asaa",
            index: 6,
            tagline: "Conclusion of the Cycle",
            description: "The final week brings the month to a close. Elders convene to assess the month's events, plan for the season ahead, and in some traditions, consult the stars to determine the next year's farming calendar.",
            icon: "⭐"
        )
    ]

    static let months: [OnwaInfo] = [
        OnwaInfo(
            id: 0,
            name: "Ọnwa Mbụ",
            index: 0,
            season: "Feb – Mar  ·  New Year Season",
            festival: "Ịgụ Arọ (New Year Proclamation)",
            description: "Ọnwa Mbụ — 'First Month' — begins with the first new moon after the year's start, usually the third week of February. This is the Igbo New Year period. The Eze Nri (Nri king) leads the Ịgụ Arọ ceremony: proclaiming the new year, ordering the sowing of seeds, and signaling the start of the planting season after the rains. Families clear farms and shrines, and old yams are disposed of before new planting begins. A time of renewal, ritual cleansing, and first plowing for the coming season.",
            activities: [
                "Ịgụ Arọ — New Year proclaimed by Eze Nri",
                "Farm & shrine clearing and renewal",
                "First land plowing & preparation",
                "Old yam disposal before new planting",
                "Community intentions & blessings"
            ]
        ),
        OnwaInfo(
            id: 1,
            name: "Ọnwa Abụọ",
            index: 1,
            season: "Mar – Apr  ·  Farming Preparation",
            festival: "Imöka Festival (20th day)",
            description: "Ọnwa Abụọ is dedicated to cleaning and farming. Communities sweep shrines, clear farmland, mend tools, and burn brush in preparation for the rains. Young men travel to bush camps to repair and expand farms. On the 20th day, the Imöka festival is observed — a mid-cycle rite, varying by town, that invokes ancestor blessings and marks the completion of initial planting preparations. This is a purely agricultural month: soil tilling, first seed planting, and communal cleanup.",
            activities: [
                "Shrine sweeping & farm clearing",
                "Tool repair & brush burning",
                "Initial soil tilling & furrow digging",
                "First seed planting begins",
                "Imöka festival on the 20th day"
            ]
        ),
        OnwaInfo(
            id: 2,
            name: "Ọnwa Ife Eke",
            index: 2,
            season: "Apr – May  ·  Penitential Month",
            festival: "Ugani (Fasting & Sacrifice to Ani)",
            description: "Ọnwa Ife Eke — the 'Eke-Sun Month' — is a penitential and hunger month known as Ugani, meaning 'hunger.' It is dedicated to Ani, the Earth goddess. All community members fast and offer sacrifices to Ani, thanking her for past bounty and seeking continued fertility. Wrestling contests and strength displays honoring one's Ikenga are held to symbolically conquer hardship and renew personal vitality. A month of fasting, purification, and deep invocations to the Earth.",
            activities: [
                "Ugani — community fasting period",
                "Sacrifices & prayers to Ani, Earth-mother",
                "Community wrestling festivals",
                "Ikenga (personal strength) rituals",
                "Spirit dances & purification rites"
            ]
        ),
        OnwaInfo(
            id: 3,
            name: "Ọnwa Anọ",
            index: 3,
            season: "May – Jun  ·  Main Planting Season",
            festival: "Ekeleke Dance Festival",
            description: "Ọnwa Anọ — 'Fourth Month' — is the main yam-planting month. As the rains intensify, communities plant seed yams in earnest. The Ekeleke dance festival is celebrated: a joyous ceremony praying for a good harvest and honoring the spirit of endurance through the long growing season. Fields are tilled, yam seed tubers are planted, and maize and other primary crops are also sown. Farmers seek rain and divine favor for the months of growth ahead.",
            activities: [
                "Yam seed tuber planting",
                "Maize & primary crop sowing",
                "Ekeleke harvest-hope dance festival",
                "Rain prayers & farm blessings",
                "Communal farming & field tending"
            ]
        ),
        OnwaInfo(
            id: 4,
            name: "Ọnwa Agwụ",
            index: 4,
            season: "Jun – Jul  ·  Peak Rainy Season",
            festival: "Agwu Veneration & Masquerade Festivals",
            description: "Ọnwa Agwụ is named for Agwu, the divine patron of divination and healing. It coincides with the peak of the rainy season and intensive farm work. Priests (dibia) make special offerings and honor Agwu this month. Adult masquerades — the Igọchi and Mmụọ — emerge for village festivals of ritual and entertainment. In some Nri lore, Ọnwa Agwụ is regarded as a 'new year' in its own right, reflecting its deep spiritual significance for healers and diviners.",
            activities: [
                "Veneration of Alusi Agwu deity",
                "Dibia (diviner) offerings & rites",
                "Igọchi & Mmụọ masquerade festivals",
                "Peak crop care, weeding & tending",
                "Healing ceremonies & consultations"
            ]
        ),
        OnwaInfo(
            id: 5,
            name: "Ọnwa Ifejiọkụ",
            index: 5,
            season: "Jul – Aug  ·  New Yam Festival",
            festival: "Iri Ji / Ịwa Ji (New Yam Festival)",
            description: "Ọnwa Ifejiọkụ is dedicated to Ifejioku, the yam harvest goddess, and her male equivalent Njoku, god of yams. This is the month of the great New Yam Festival — Iri Ji or Ịwa Ji. Families offer the first fruits of the harvest to Ifejioku; all remaining old yams must be finished by the eve of the festival. Then a grand communal feast of new yams begins. The festival embodies abundance, gratitude, and renewal — yam, the king of crops, is celebrated as divine blessing and community wealth.",
            activities: [
                "Iri Ji / Ịwa Ji — New Yam Festival",
                "First-fruits offering to Ifejioku & Njoku",
                "Old yam disposal before festival eve",
                "Grand communal feast of new yams",
                "Gratitude rites & cultural celebrations"
            ]
        ),
        OnwaInfo(
            id: 6,
            name: "Ọnwa Alọm Chi",
            index: 6,
            season: "Aug – Sep  ·  Harvest Thanksgiving",
            festival: "Alọm Chi Shrine Ceremonies",
            description: "Ọnwa Alọm Chi literally means 'Placating the Personal Chi (spirit).' Following the great yam harvest, it is a time of thanksgiving and female-centered rituals. Yams are harvested in full and barns filled. Women build or revisit their Alọm Chi shrines — kola nut sanctuaries for ancestors — and pray for their families' futures and children yet unborn. The month deeply venerates motherhood and lineage: women break kola nuts in honor of ancestors and the first mothers of Igbo cosmology.",
            activities: [
                "Main yam harvest completion & barn filling",
                "Women's Alọm Chi shrine prayers",
                "Kola nut rites for ancestors & unborn",
                "Motherhood & fertility celebrations",
                "Female-centered family thanksgivings"
            ]
        ),
        OnwaInfo(
            id: 7,
            name: "Ọnwa Ilọ Mmụọ",
            index: 7,
            season: "September  ·  Spirit Month",
            festival: "Ọnwa Asatọ (Eighth-Month Festival)",
            description: "Ọnwa Ilọ Mmụọ means 'realm of spirits.' This month hosts the Ọnwa Asatọ — the Eighth-Month Festival — a central event in the Nri priestly calendar. It is a communal festival of renewal held after the main harvest and before the final farming period. The spiritual retreat of ancestral spirits is the central motif: communities honor their guardians through masquerades, gratitude ceremonies, and preparation for the year's final cycle. Minor crops are sown in the season's last planting.",
            activities: [
                "Ọnwa Asatọ — Nri eighth-month festival",
                "Masquerades & ancestral spirit rites",
                "Community renewal & thanksgiving",
                "Honoring spiritual guardians",
                "Last sowing of minor crops"
            ]
        ),
        OnwaInfo(
            id: 8,
            name: "Ọnwa Ana",
            index: 8,
            season: "October  ·  Earth Goddess Month",
            festival: "Ilochi Ala / Ala Day (Earth Festival)",
            description: "Ọnwa Ana is named for Ala (Ani), the supreme Earth Mother goddess and guardian of Igbo morality. This is the time for the annual Ilochi Ala — the Earth Festival: people set aside last yams and produce for the earth, break kola nuts in thanksgiving to Ala, and refrain from farming as a sign of deep respect. Ceremonies ask the earth to remain fertile and to uphold its moral taboos. After this month, fields are left fallow until the rains return.",
            activities: [
                "Ilochi Ala — community Earth Festival",
                "Sacrifices & kola nuts for Ala (Ani)",
                "Fallow field observance — farming stopped",
                "Moral taboo renewal & enforcement",
                "Year-end thanksgiving to the land"
            ]
        ),
        OnwaInfo(
            id: 9,
            name: "Ọnwa Okike",
            index: 9,
            season: "November  ·  Creation Month",
            festival: "Okike Creation Ceremonies",
            description: "Ọnwa Okike means 'creation' and is associated with creation rites honoring Chineke, the Creator of all things. Communities perform ceremonies acknowledging the new creation cycle — including masquerades and communal feasts called Okike. Elders hold 'okpukpe' cleansing ceremonies as final rites of renewal. Notably, Okike is also the name of a community leadership title in Awka, linking this month to traditional governance and the reaffirmation of authority before the year ends.",
            activities: [
                "Chineke (Creator) invocation ceremonies",
                "Okike communal feasts & masquerades",
                "Okpukpe elder cleansing rites",
                "Leadership & community title renewals",
                "Year-end purification & renewal rites"
            ]
        ),
        OnwaInfo(
            id: 10,
            name: "Ọnwa Ajana",
            index: 10,
            season: "Late Nov  ·  Year-End Rites",
            festival: "Continued Okike Purification Rites",
            description: "Ọnwa Ajana continues the themes of creation and final harvest from the preceding month. The month extends Okike's ritual cycle with concluding purification festivals and year-end ceremonies. This is one of the last full months before the year closes: all remaining crops are brought in and stored, fields are fully cleared. Some traditions regard Ọnwa Ajana as the definitive completion of the planting season's cycle — a month of harvest, storage, and quiet preparation.",
            activities: [
                "Continued Okike purification festivals",
                "Final field clearing & crop storage",
                "Concluding year-end community ceremonies",
                "Elder councils & strategic planning",
                "Harvest storage & dry-season preparation"
            ]
        ),
        OnwaInfo(
            id: 11,
            name: "Ọnwa Ede Ajana",
            index: 11,
            season: "December  ·  Ritual Culmination",
            festival: "Conclusion of the Ajana Cycle",
            description: "Ọnwa Ede Ajana literally means 'End of Ajana.' This month is the culmination of the year's ritual cycles — as some accounts simply state: 'Ritual ends.' The Ajana ceremony cycle concludes, farm work finishes, and communities settle into the short dry season. Activities slow to a gentle pace: remaining ceremonies wrap up quietly, all crops are stored, and families gather for small feasts. This quieter month is used for planning ahead, craft work, and welcoming the approaching new year.",
            activities: [
                "Ajana ritual cycle conclusion",
                "Final crop harvest & grain storage",
                "Small family feasts & celebrations",
                "Craft work & dry-season activities",
                "Year-ahead planning & preparation"
            ]
        ),
        OnwaInfo(
            id: 12,
            name: "Ọnwa Ụzọ Alụsị",
            index: 12,
            season: "Jan – Feb  ·  Path of the Deities",
            festival: "Final Alusi Offerings & Farewell Rites",
            description: "Ọnwa Ụzọ Alụsị — 'Way of the Alusi (deities)' — is the final month of the Igbo calendar year. Villages perform farewell ceremonies for the year: kola nut and palm wine are offered at shrines, taboos are strictly observed, and diviners foretell fortunes for the coming year. Rituals 'clear the path' for the new year (the name suggests opening the way for the deities). Fields rest in fallow. The year then ends with Ubọchị Ọmụmụ — the sacred intercalary day of new birth and cosmic renewal.",
            activities: [
                "Final sacrifices to Alusi & ancestors",
                "Kola nut & palm wine shrine offerings",
                "Divination for the coming year",
                "Taboo observances & path-clearing rites",
                "Preparation for Ubọchị Ọmụmụ"
            ]
        )
    ]
}
