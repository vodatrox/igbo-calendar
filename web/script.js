'use strict';

/* ═══════════════════════════════════════════════════════════════════════════
   IGBO CALENDAR ENGINE
   Reference anchor: September 15, 2023 = AFỌ (index 2), Izu Mbụ (index 0),
   Ọnwa Ilọ Mmụọ (index 7), Igbo Year 7023 (base 2023)
   ═══════════════════════════════════════════════════════════════════════════ */

const Engine = (() => {
  const IGBO_DAYS   = ["EKE", "ORIE", "AFỌ", "NKWỌ"];
  const IGBO_WEEKS  = [
    "Izu Mbụ","Izu Abụọ","Izu Atọ","Izu Anọ",
    "Izu Ise","Izu Isii","Izu Asaa"
  ];
  const IGBO_MONTHS = [
    "Ọnwa Mbụ","Ọnwa Abụọ","Ọnwa Ife Eke","Ọnwa Anọ",
    "Ọnwa Agwụ","Ọnwa Ifejiọkụ","Ọnwa Alọm Chi",
    "Ọnwa Ilọ Mmụọ","Ọnwa Ana","Ọnwa Okike",
    "Ọnwa Ajana","Ọnwa Ede Ajana","Ọnwa Ụzọ Alụsị"
  ];

  const DAYS_IN_YEAR        = 365;            // 13*28 + 1
  const IGBO_ERA_OFFSET     = 5000;
  const REF_DATE            = _utc(2023, 9, 15); // Sep 15 2023
  const REF_DAY_INDEX       = 2;             // AFỌ
  const REF_MONTH_INDEX     = 7;             // Ọnwa Ilọ Mmụọ
  const REF_YEAR_BASE       = 2023;
  const REF_DAYS_INTO_YEAR  = REF_MONTH_INDEX * 28; // 196

  // Helpers
  function _utc(y, m, d) {
    return Date.UTC(y, m - 1, d);
  }
  function _norm(date) {
    // Return a Date normalised to start-of-day UTC from any Date/timestamp
    if (date instanceof Date) {
      return Date.UTC(date.getFullYear(), date.getMonth(), date.getDate());
    }
    return date; // already a UTC ms timestamp
  }
  function floorDiv(a, b) { return Math.floor(a / b); }
  function floorMod(a, b) { return ((a % b) + b) % b; }

  function toIgboDate(dateOrMs) {
    const ms = _norm(dateOrMs);
    const deltaDays = Math.round((ms - REF_DATE) / 86400000);
    const daysIntoYear = REF_DAYS_INTO_YEAR + deltaDays;

    const igboYearBase = REF_YEAR_BASE + floorDiv(daysIntoYear, DAYS_IN_YEAR);
    const remaining    = floorMod(daysIntoYear, DAYS_IN_YEAR);
    const dayIndex     = floorMod(REF_DAY_INDEX + deltaDays, 4);

    // Ubọchị Ọmụmụ = last day of the year
    if (remaining === DAYS_IN_YEAR - 1) {
      return {
        day: IGBO_DAYS[dayIndex], dayIndex,
        week: "Ubọchị Ọmụmụ",    weekIndex: -1,
        month: "Ubọchị Ọmụmụ",   monthIndex: -1,
        year: igboYearBase + IGBO_ERA_OFFSET,
        yearBase: igboYearBase,
        dayWithinMonth: -1,
        isUboshiOmumu: true
      };
    }

    const monthIndex     = Math.floor(remaining / 28);
    const dayWithinMonth = remaining % 28;
    const weekIndex      = Math.floor(dayWithinMonth / 4);

    return {
      day: IGBO_DAYS[dayIndex], dayIndex,
      week: IGBO_WEEKS[weekIndex],   weekIndex,
      month: IGBO_MONTHS[monthIndex], monthIndex,
      year: igboYearBase + IGBO_ERA_OFFSET,
      yearBase: igboYearBase,
      dayWithinMonth,
      isUboshiOmumu: false
    };
  }

  /** UTC ms of first day of an Igbo month given yearBase & monthIndex */
  function firstDayOfIgboMonth(yearBase, monthIndex) {
    const daysIntoYear = monthIndex * 28;
    const yearsFromRef = yearBase - REF_YEAR_BASE;
    const deltaDays = yearsFromRef * DAYS_IN_YEAR + daysIntoYear - REF_DAYS_INTO_YEAR;
    return REF_DATE + deltaDays * 86400000;
  }

  /** All 28 UTC-ms timestamps for an Igbo month */
  function getIgboMonthDates(yearBase, monthIndex) {
    const first = firstDayOfIgboMonth(yearBase, monthIndex);
    return Array.from({length: 28}, (_, i) => first + i * 86400000);
  }

  /** UTC ms of Ubọchị Ọmụmụ for given yearBase */
  function uboshiOmumuDate(yearBase) {
    const deltaDays = (yearBase - REF_YEAR_BASE) * DAYS_IN_YEAR
                      + (DAYS_IN_YEAR - 1) - REF_DAYS_INTO_YEAR;
    return REF_DATE + deltaDays * 86400000;
  }

  /** Navigate: return first day of previous Igbo month */
  function prevIgboMonth(dateOrMs) {
    const igbo = toIgboDate(dateOrMs);
    const yb   = igbo.yearBase;
    if (igbo.isUboshiOmumu)    return firstDayOfIgboMonth(yb, 12);
    if (igbo.monthIndex <= 0)  return firstDayOfIgboMonth(yb - 1, 12);
    return firstDayOfIgboMonth(yb, igbo.monthIndex - 1);
  }

  /** Navigate: return first day of next Igbo month (or Ubọchị Ọmụmụ date) */
  function nextIgboMonth(dateOrMs) {
    const igbo = toIgboDate(dateOrMs);
    const yb   = igbo.yearBase;
    if (igbo.isUboshiOmumu)   return firstDayOfIgboMonth(yb + 1, 0);
    if (igbo.monthIndex >= 12) return uboshiOmumuDate(yb);
    return firstDayOfIgboMonth(yb, igbo.monthIndex + 1);
  }

  /** UTC ms of the first day of the Igbo month containing dateOrMs */
  function igboMonthStart(dateOrMs) {
    const igbo = toIgboDate(dateOrMs);
    if (igbo.isUboshiOmumu) return _norm(dateOrMs); // single day
    return firstDayOfIgboMonth(igbo.yearBase, igbo.monthIndex);
  }

  return {
    IGBO_DAYS, IGBO_WEEKS, IGBO_MONTHS, DAYS_IN_YEAR, IGBO_ERA_OFFSET,
    toIgboDate, firstDayOfIgboMonth, getIgboMonthDates,
    uboshiOmumuDate, prevIgboMonth, nextIgboMonth, igboMonthStart,
    _utc, _norm
  };
})();


/* ═══════════════════════════════════════════════════════════════════════════
   STATIC DATA
   ═══════════════════════════════════════════════════════════════════════════ */

const IZU_DATA = [
  { name:"Izu Mbụ",  icon:"🕊️", tagline:"Ritual Cleansing & Preparation",
    desc:"The first week of the Igbo month is a time of ritual purification. Communities prepare their hearts, homes, and fields for the month ahead. It is a sacred period of setting intentions and seeking spiritual alignment.", color:"var(--izu-0)" },
  { name:"Izu Abụọ", icon:"🌱", tagline:"Farm Clearing & Planting",
    desc:"The second week is devoted to agricultural activity. Farmlands are cleared, seeds are planted, and the community cooperates to tend the earth. The spirit of collective labor defines this Izu.", color:"var(--izu-1)" },
  { name:"Izu Atọ",  icon:"🤝", tagline:"Community Gatherings",
    desc:"Izu Atọ is the social heartbeat of the month. Families reunite, disputes are settled, and important community decisions are made. It is also a time for cultural performances and storytelling.", color:"var(--izu-2)" },
  { name:"Izu Anọ",  icon:"🏺", tagline:"Major Market Transactions",
    desc:"The fourth week sees the busiest trading of the month. Merchants travel from distant communities, goods flow freely, and large commercial agreements are sealed. Wealth and prosperity are the themes of Izu Anọ.", color:"var(--izu-3)" },
  { name:"Izu Ise",  icon:"🔮", tagline:"Rest & Spiritual Reflection",
    desc:"A quieter week dedicated to rest, prayer, and communing with the ancestors. Divination is practiced, oracles are consulted, and individuals seek spiritual guidance for their lives and futures.", color:"var(--izu-4)" },
  { name:"Izu Isii", icon:"🎭", tagline:"Festivals & Masquerades",
    desc:"The sixth week is the most celebratory of the month. Masquerades emerge, songs fill the air, and the community honors its cultural heritage through dance, music, and traditional pageantry.", color:"var(--izu-5)" },
  { name:"Izu Asaa", icon:"⭐", tagline:"Conclusion of the Cycle",
    desc:"The final week brings the month to a close. Elders convene to assess the month's events, plan for the season ahead, and in some traditions, consult the stars to determine the next year's farming calendar.", color:"var(--izu-6)" }
];

const ONWA_DATA = [
  { name:"Ọnwa Mbụ",       season:"Feb – Mar  ·  New Year Season",       festival:"Ịgụ Arọ (New Year Proclamation)",
    desc:"Ọnwa Mbụ — 'First Month' — begins with the first new moon after the year's start, usually the third week of February. This is the Igbo New Year period. The Eze Nri (Nri king) leads the Ịgụ Arọ ceremony: proclaiming the new year, ordering the sowing of seeds, and signaling the start of the planting season after the rains. Families clear farms and shrines, and old yams are disposed of before new planting begins. A time of renewal, ritual cleansing, and first plowing for the coming season.",
    activities:["Ịgụ Arọ — New Year proclaimed by Eze Nri","Farm & shrine clearing and renewal","First land plowing & preparation","Old yam disposal before new planting","Community intentions & blessings"] },
  { name:"Ọnwa Abụọ",      season:"Mar – Apr  ·  Farming Preparation",   festival:"Imöka Festival (20th day)",
    desc:"Ọnwa Abụọ is dedicated to cleaning and farming. Communities sweep shrines, clear farmland, mend tools, and burn brush in preparation for the rains. Young men travel to bush camps to repair and expand farms. On the 20th day, the Imöka festival is observed — a mid-cycle rite, varying by town, that invokes ancestor blessings and marks the completion of initial planting preparations. This is a purely agricultural month: soil tilling, first seed planting, and communal cleanup.",
    activities:["Shrine sweeping & farm clearing","Tool repair & brush burning","Initial soil tilling & furrow digging","First seed planting begins","Imöka festival on the 20th day"] },
  { name:"Ọnwa Ife Eke",   season:"Apr – May  ·  Penitential Month",     festival:"Ugani (Fasting & Sacrifice to Ani)",
    desc:"Ọnwa Ife Eke — the 'Eke-Sun Month' — is a penitential and hunger month known as Ugani, meaning 'hunger.' It is dedicated to Ani, the Earth goddess. All community members fast and offer sacrifices to Ani, thanking her for past bounty and seeking continued fertility. Wrestling contests and strength displays honoring one's Ikenga are held to symbolically conquer hardship and renew personal vitality. A month of fasting, purification, and deep invocations to the Earth.",
    activities:["Ugani — community fasting period","Sacrifices & prayers to Ani, Earth-mother","Community wrestling festivals","Ikenga (personal strength) rituals","Spirit dances & purification rites"] },
  { name:"Ọnwa Anọ",       season:"May – Jun  ·  Main Planting Season",  festival:"Ekeleke Dance Festival",
    desc:"Ọnwa Anọ — 'Fourth Month' — is the main yam-planting month. As the rains intensify, communities plant seed yams in earnest. The Ekeleke dance festival is celebrated: a joyous ceremony praying for a good harvest and honoring the spirit of endurance through the long growing season. Fields are tilled, yam seed tubers are planted, and maize and other primary crops are also sown. Farmers seek rain and divine favor for the months of growth ahead.",
    activities:["Yam seed tuber planting","Maize & primary crop sowing","Ekeleke harvest-hope dance festival","Rain prayers & farm blessings","Communal farming & field tending"] },
  { name:"Ọnwa Agwụ",      season:"Jun – Jul  ·  Peak Rainy Season",     festival:"Agwu Veneration & Masquerade Festivals",
    desc:"Ọnwa Agwụ is named for Agwu, the divine patron of divination and healing. It coincides with the peak of the rainy season and intensive farm work. Priests (dibia) make special offerings and honor Agwu this month. Adult masquerades — the Igọchi and Mmụọ — emerge for village festivals of ritual and entertainment. In some Nri lore, Ọnwa Agwụ is regarded as a 'new year' in its own right, reflecting its deep spiritual significance for healers and diviners.",
    activities:["Veneration of Alusi Agwu deity","Dibia (diviner) offerings & rites","Igọchi & Mmụọ masquerade festivals","Peak crop care, weeding & tending","Healing ceremonies & consultations"] },
  { name:"Ọnwa Ifejiọkụ",  season:"Jul – Aug  ·  New Yam Festival",      festival:"Iri Ji / Ịwa Ji (New Yam Festival)",
    desc:"Ọnwa Ifejiọkụ is dedicated to Ifejioku, the yam harvest goddess, and her male equivalent Njoku, god of yams. This is the month of the great New Yam Festival — Iri Ji or Ịwa Ji. Families offer the first fruits of the harvest to Ifejioku; all remaining old yams must be finished by the eve of the festival. Then a grand communal feast of new yams begins. The festival embodies abundance, gratitude, and renewal — yam, the king of crops, is celebrated as divine blessing and community wealth.",
    activities:["Iri Ji / Ịwa Ji — New Yam Festival","First-fruits offering to Ifejioku & Njoku","Old yam disposal before festival eve","Grand communal feast of new yams","Gratitude rites & cultural celebrations"] },
  { name:"Ọnwa Alọm Chi",  season:"Aug – Sep  ·  Harvest Thanksgiving",  festival:"Alọm Chi Shrine Ceremonies",
    desc:"Ọnwa Alọm Chi literally means 'Placating the Personal Chi (spirit).' Following the great yam harvest, it is a time of thanksgiving and female-centered rituals. Yams are harvested in full and barns filled. Women build or revisit their Alọm Chi shrines — kola nut sanctuaries for ancestors — and pray for their families' futures and children yet unborn. The month deeply venerates motherhood and lineage: women break kola nuts in honor of ancestors and the first mothers of Igbo cosmology.",
    activities:["Main yam harvest completion & barn filling","Women's Alọm Chi shrine prayers","Kola nut rites for ancestors & unborn","Motherhood & fertility celebrations","Female-centered family thanksgivings"] },
  { name:"Ọnwa Ilọ Mmụọ", season:"September  ·  Spirit Month",           festival:"Ọnwa Asatọ (Eighth-Month Festival)",
    desc:"Ọnwa Ilọ Mmụọ means 'realm of spirits.' This month hosts the Ọnwa Asatọ — the Eighth-Month Festival — a central event in the Nri priestly calendar. It is a communal festival of renewal held after the main harvest and before the final farming period. The spiritual retreat of ancestral spirits is the central motif: communities honor their guardians through masquerades, gratitude ceremonies, and preparation for the year's final cycle. Minor crops are sown in the season's last planting.",
    activities:["Ọnwa Asatọ — Nri eighth-month festival","Masquerades & ancestral spirit rites","Community renewal & thanksgiving","Honoring spiritual guardians","Last sowing of minor crops"] },
  { name:"Ọnwa Ana",       season:"October  ·  Earth Goddess Month",      festival:"Ilochi Ala / Ala Day (Earth Festival)",
    desc:"Ọnwa Ana is named for Ala (Ani), the supreme Earth Mother goddess and guardian of Igbo morality. This is the time for the annual Ilochi Ala — the Earth Festival: people set aside last yams and produce for the earth, break kola nuts in thanksgiving to Ala, and refrain from farming as a sign of deep respect. Ceremonies ask the earth to remain fertile and to uphold its moral taboos. After this month, fields are left fallow until the rains return.",
    activities:["Ilochi Ala — community Earth Festival","Sacrifices & kola nuts for Ala (Ani)","Fallow field observance — farming stopped","Moral taboo renewal & enforcement","Year-end thanksgiving to the land"] },
  { name:"Ọnwa Okike",     season:"November  ·  Creation Month",          festival:"Okike Creation Ceremonies",
    desc:"Ọnwa Okike means 'creation' and is associated with creation rites honoring Chineke, the Creator of all things. Communities perform ceremonies acknowledging the new creation cycle — including masquerades and communal feasts called Okike. Elders hold 'okpukpe' cleansing ceremonies as final rites of renewal. Notably, Okike is also the name of a community leadership title in Awka, linking this month to traditional governance and the reaffirmation of authority before the year ends.",
    activities:["Chineke (Creator) invocation ceremonies","Okike communal feasts & masquerades","Okpukpe elder cleansing rites","Leadership & community title renewals","Year-end purification & renewal rites"] },
  { name:"Ọnwa Ajana",     season:"Late Nov  ·  Year-End Rites",          festival:"Continued Okike Purification Rites",
    desc:"Ọnwa Ajana continues the themes of creation and final harvest from the preceding month. The month extends Okike's ritual cycle with concluding purification festivals and year-end ceremonies. This is one of the last full months before the year closes: all remaining crops are brought in and stored, fields are fully cleared. Some traditions regard Ọnwa Ajana as the definitive completion of the planting season's cycle — a month of harvest, storage, and quiet preparation.",
    activities:["Continued Okike purification festivals","Final field clearing & crop storage","Concluding year-end community ceremonies","Elder councils & strategic planning","Harvest storage & dry-season preparation"] },
  { name:"Ọnwa Ede Ajana", season:"December  ·  Ritual Culmination",      festival:"Conclusion of the Ajana Cycle",
    desc:"Ọnwa Ede Ajana literally means 'End of Ajana.' This month is the culmination of the year's ritual cycles — as some accounts simply state: 'Ritual ends.' The Ajana ceremony cycle concludes, farm work finishes, and communities settle into the short dry season. Activities slow to a gentle pace: remaining ceremonies wrap up quietly, all crops are stored, and families gather for small feasts. This quieter month is used for planning ahead, craft work, and welcoming the approaching new year.",
    activities:["Ajana ritual cycle conclusion","Final crop harvest & grain storage","Small family feasts & celebrations","Craft work & dry-season activities","Year-ahead planning & preparation"] },
  { name:"Ọnwa Ụzọ Alụsị",season:"Jan – Feb  ·  Path of the Deities",   festival:"Final Alusi Offerings & Farewell Rites",
    desc:"Ọnwa Ụzọ Alụsị — 'Way of the Alusi (deities)' — is the final month of the Igbo calendar year. Villages perform farewell ceremonies for the year: kola nut and palm wine are offered at shrines, taboos are strictly observed, and diviners foretell fortunes for the coming year. Rituals 'clear the path' for the new year (the name suggests opening the way for the deities). Fields rest in fallow. The year then ends with Ubọchị Ọmụmụ — the sacred intercalary day of new birth and cosmic renewal.",
    activities:["Final sacrifices to Alusi & ancestors","Kola nut & palm wine shrine offerings","Divination for the coming year","Taboo observances & path-clearing rites","Preparation for Ubọchị Ọmụmụ"] }
];

const MARKET_DAYS = [
  { name:"EKE",  icon:"🔥", tagline:"Fire of Creation & East",
    color:"var(--eke)", bg:"var(--eke-bg)",
    element:"Fire", direction:"East", deity:"Eke (Spirit of Fire)",
    significance:"Eke is the primal force of fire and creation — the most sacred of all market days. Governing the East where the sun rises, Eke channels the energy of initiation, vitality, and transformation. Its fierce fire energy drives new beginnings and sparks all of existence. Certain spiritual rituals thrive on Eke, though its intense energy makes it inauspicious for travel or settling disputes.",
    activities:["Ritual offerings & spiritual ceremonies","New business ventures & initiations","Birth and naming ceremonies","Major market transactions","Transformative rites of passage"] },
  { name:"ORIE", icon:"💧", tagline:"Waters of Healing & West",
    color:"var(--orie)", bg:"var(--orie-bg)",
    element:"Water", direction:"West", deity:"Nne Mmiri (Water Mother)",
    significance:"Orie embodies the fluid, healing power of water, flowing from the West where the sun sets. Under the guardianship of Nne Mmiri — the great water mother — this day favors nurturing tasks, cleansing rituals, and emotional reflection. It is a time for community care, spiritual purification, and the healing of bonds. Orie's gentle waters soothe conflicts and deepen communal harmony.",
    activities:["Cleansing & purification rituals","Nne Mmiri (water deity) offerings","Community healing & care","Communal gatherings & bonding","Introspection & emotional healing"] },
  { name:"AFỌ",  icon:"🌿", tagline:"Earth of Abundance & North",
    color:"var(--afo)", bg:"var(--afo-bg)",
    element:"Earth", direction:"North", deity:"Ala (Earth Goddess)",
    significance:"Afọ embodies the grounding power of the earth, anchored in the North. Presided over by Ala — the sacred earth goddess and guardian of Igbo morality — this day promotes stability, fertility, and material prosperity. Agriculture, trade, and building flourish on Afọ. It is a day to sow, consolidate, and harvest, honoring the soil that sustains and nourishes all life.",
    activities:["Agriculture, planting & harvest","Ala (earth goddess) offerings","Trade & commercial transactions","Building & construction work","Fertility and ancestral rites"] },
  { name:"NKWỌ", icon:"💨", tagline:"Air of Wisdom & South",
    color:"var(--nkwo)", bg:"var(--nkwo-bg)",
    element:"Air", direction:"South", deity:"Nkwọ (Wind Spirit)",
    significance:"Nkwọ signifies the expansive force of air — the invisible breath that carries thought, words, and ancestral spirit. Linked to the South, it governs intellect, communication, and movement. The ideal day for long-distance trade, philosophical discourse, and spiritual seeking, Nkwọ's winds bring clarity and foresight. It marks the conclusion of the Izu cycle, preparing the community for renewal.",
    activities:["Long-distance travel & trade","Intellectual discourse & learning","Divination & spiritual seeking","Cycle conclusion ceremonies","Masquerade festivals & ancestral celebrations"] }
];

const DAY_CSS_VARS = { EKE:"var(--eke)", ORIE:"var(--orie)", "AFỌ":"var(--afo)", "NKWỌ":"var(--nkwo)" };
const DAY_CSS_BG   = { EKE:"var(--eke-bg)", ORIE:"var(--orie-bg)", "AFỌ":"var(--afo-bg)", "NKWỌ":"var(--nkwo-bg)" };

const FESTIVALS = [
  { name:"Iwa Ji (New Yam Festival)", subtitle:"The King of All Festivals",
    desc:"The New Yam Festival is the most widely celebrated event in Igboland. It marks the end of the farming season and the blessed beginning of the harvest. Yam — the king of crops — is first presented to the earth goddess Ala and the ancestors before the community partakes. Elaborate dances, feasts, masquerades, and wrestling matches are held across communities.",
    marketDay:"ORIE", igboMonth:"Ọnwa Ilọ Mmụọ", location:"Igbo-Ukwu, Anambra", featured:true },
  { name:"Ofala Festival", subtitle:"Royal Pageantry of Onitsha",
    desc:"The Ofala is the annual royal festival of the Onitsha Kingdom, celebrating the Obi (King) in full splendor. The Obi appears in magnificent royal regalia to bless his subjects. The festival features royal dances, traditional music, colorful displays of wealth, and ceremonial rites that have been passed down through centuries of Onitsha kingship.",
    marketDay:"NKWỌ", igboMonth:"Ọnwa Okike", location:"Onitsha, Anambra", featured:false },
  { name:"Mmanwu Festival", subtitle:"The Dance of Ancestral Spirits",
    desc:"The Mmanwu Festival is a mesmerizing celebration of masquerades representing the spirits of departed ancestors. These sacred masked figures perform elaborate dances and deliver messages from the spirit world. The festival serves as a bridge between the living and the dead, reinforcing community values and honoring those who came before.",
    marketDay:"EKE", igboMonth:"Ọnwa Ana", location:"Enugu State", featured:false },
  { name:"Igu Aro (New Year Proclamation)", subtitle:"Proclamation of the Igbo New Year",
    desc:"Igu Aro is the ancient ceremony marking the beginning of a new Igbo year, traditionally proclaimed by the Eze Nri — the spiritual leader of the Igbo. The ceremony involves sacred rites, prayers, sacrifices, and blessings for peace, abundance, and prosperity in the coming year. It is one of the oldest continuous traditions in Africa.",
    marketDay:"AFỌ", igboMonth:"Ọnwa Mbụ", location:"Nri Kingdom, Anambra", featured:false },
  { name:"Onwa Asaa Festival", subtitle:"Mid-Year Harvest Thanksgiving",
    desc:"Celebrated in the seventh Igbo month, Onwa Asaa marks the peak of the agricultural year. Communities gather for an elaborate thanksgiving, featuring traditional music played on ogene (iron gongs), oja (flutes), and udu (clay pot drums). Wrestling matches, community feasts, and dance competitions are central to the celebration.",
    marketDay:"EKE", igboMonth:"Ọnwa Alọm Chi", location:"Owerri, Imo State", featured:false },
  { name:"Agwụ Festival", subtitle:"Feast of the Medicine Deity",
    desc:"The Agwụ Festival honors Agwụ — the divine deity of medicine, divination, and wisdom. Dibia (traditional healers and diviners) perform sacred rites, medicinal plants are consecrated, and the community seeks blessings for good health and spiritual clarity. The festival reaffirms the sacred role of traditional medicine in Igbo culture.",
    marketDay:"AFỌ", igboMonth:"Ọnwa Agwụ", location:"Awka, Anambra", featured:false }
];


/* ═══════════════════════════════════════════════════════════════════════════
   APP STATE
   ═══════════════════════════════════════════════════════════════════════════ */

const today     = Engine._norm(new Date());          // UTC-ms, start-of-day
let selectedMs  = today;                             // currently selected date
let gregViewMs  = today;                             // any date in the greg month being viewed
let igboViewMs  = today;                             // any date in the igbo month being viewed
let calView     = 'gregorian';                       // 'gregorian' | 'igbo'
let currentTab  = '';                                // current bottom-nav tab
let theme       = 'amber';                           // 'amber' | 'green'
let infoTab     = 'izu';                             // 'izu' | 'onwa'


/* ═══════════════════════════════════════════════════════════════════════════
   UTILITY HELPERS
   ═══════════════════════════════════════════════════════════════════════════ */

function msToDate(ms) {
  // Returns a plain Date in local time representing the UTC date
  const d = new Date(ms);
  return new Date(d.getUTCFullYear(), d.getUTCMonth(), d.getUTCDate());
}

function formatGregorianDate(ms) {
  const d = msToDate(ms);
  const days = ['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday'];
  const months = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
  return `${days[d.getDay()]}, ${d.getDate()} ${months[d.getMonth()]} ${d.getFullYear()}`;
}

function formatGregorianMonthYear(ms) {
  const d = msToDate(ms);
  const months = ['January','February','March','April','May','June',
                  'July','August','September','October','November','December'];
  return `${months[d.getMonth()]} ${d.getFullYear()}`;
}

function chevronSVG() {
  return `<svg viewBox="0 0 24 24" fill="none"><path d="M6 9l6 6 6-6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/></svg>`;
}

// IZU row bg color at 18% opacity (CSS hex + alpha)
const IZU_HEX_COLORS = [
  '#D4900A','#1976D2','#2E7D32','#7B1FA2','#E53935','#00838F','#F57C00'
];
function izuRowBg(izuIndex) {
  return IZU_HEX_COLORS[izuIndex] + '2E'; // ~18% opacity
}

// Market day color (raw hex)
const DAY_HEX = ['#D4900A','#1976D2','#2E7D32','#7B1FA2'];


/* ═══════════════════════════════════════════════════════════════════════════
   RENDER: GREGORIAN CALENDAR
   ═══════════════════════════════════════════════════════════════════════════ */

function renderGregorianCalendar() {
  const d     = msToDate(gregViewMs);
  const year  = d.getFullYear();
  const month = d.getMonth();

  document.getElementById('greg-month-label').textContent = formatGregorianMonthYear(gregViewMs);

  const grid    = document.getElementById('greg-grid');
  grid.innerHTML = '';

  const firstDay    = new Date(year, month, 1).getDay(); // 0=Sun
  const daysInMonth = new Date(year, month + 1, 0).getDate();
  const todayDate   = msToDate(today);
  const selDate     = msToDate(selectedMs);

  // Empty prefix cells
  for (let i = 0; i < firstDay; i++) {
    const cell = document.createElement('div');
    cell.className = 'gcell empty';
    grid.appendChild(cell);
  }

  // Day cells
  for (let day = 1; day <= daysInMonth; day++) {
    const cellMs    = Engine._utc(year, month + 1, day);
    const igbo      = Engine.toIgboDate(cellMs);
    const isToday   = (year === todayDate.getFullYear() && month === todayDate.getMonth() && day === todayDate.getDate());
    const isSel     = (msToDate(selectedMs).getFullYear() === year && msToDate(selectedMs).getMonth() === month && msToDate(selectedMs).getDate() === day);
    const dayColor  = DAY_HEX[igbo.dayIndex];

    const cell = document.createElement('div');
    cell.className = 'gcell' + (isToday ? ' today' : '') + (isSel ? ' selected' : '');
    cell.dataset.ms = cellMs;
    cell.innerHTML  = `
      <span class="gcell-num">${day}</span>
      <span class="gcell-igbo" style="color:${dayColor};background:${dayColor}22">${igbo.day}</span>
    `;
    cell.addEventListener('click', () => {
      selectedMs  = cellMs;
      igboViewMs  = cellMs; // sync igbo view to selected date
      renderGregorianCalendar();
      renderIgboCalendar();
      renderDateCard();
    });
    grid.appendChild(cell);
  }
}


/* ═══════════════════════════════════════════════════════════════════════════
   RENDER: IGBO CALENDAR
   ═══════════════════════════════════════════════════════════════════════════ */

function renderIgboCalendar() {
  const igbo = Engine.toIgboDate(igboViewMs);

  const uboshiEl  = document.getElementById('uboshi-display');
  const gridWrap  = document.getElementById('igbo-grid-wrap');
  const monthLbl  = document.getElementById('igbo-month-label');

  if (igbo.isUboshiOmumu) {
    // Special display
    monthLbl.textContent = `Ubọchị Ọmụmụ ${igbo.year}`;
    uboshiEl.classList.remove('hidden');
    gridWrap.classList.add('hidden');
    document.getElementById('uboshi-day-name').textContent = igbo.day;
    return;
  }

  uboshiEl.classList.add('hidden');
  gridWrap.classList.remove('hidden');

  monthLbl.textContent = `${igbo.month} ${igbo.year}`;

  const yearBase   = igbo.yearBase;
  const monthIndex = igbo.monthIndex;
  const dates      = Engine.getIgboMonthDates(yearBase, monthIndex);
  const todayDate  = msToDate(today);
  const selDate    = msToDate(selectedMs);

  const grid = document.getElementById('igbo-grid');
  grid.innerHTML = '';

  for (let week = 0; week < 7; week++) {
    const row = document.createElement('div');
    row.className = 'igbo-row';
    row.style.setProperty('--row-color', izuRowBg(week));

    // Week label
    const lbl = document.createElement('div');
    lbl.className = 'izu-row-label';
    // Short label fitting the narrow column
    const shortNames = ["Izu I","Izu II","Izu III","Izu IV","Izu V","Izu VI","Izu VII"];
    lbl.textContent = shortNames[week];
    row.appendChild(lbl);

    for (let dayCol = 0; dayCol < 4; dayCol++) {
      const idx     = week * 4 + dayCol;
      const cellMs  = dates[idx];
      const cellD   = msToDate(cellMs);
      const igboD   = Engine.toIgboDate(cellMs);
      const isToday = cellD.getFullYear() === todayDate.getFullYear()
                   && cellD.getMonth()    === todayDate.getMonth()
                   && cellD.getDate()     === todayDate.getDate();
      const isSel   = cellD.getFullYear() === selDate.getFullYear()
                   && cellD.getMonth()    === selDate.getMonth()
                   && cellD.getDate()     === selDate.getDate();

      const cell = document.createElement('div');
      cell.className = 'icell'
        + (isToday ? ' today-cell'    : '')
        + (isSel   ? ' selected-cell' : '');
      cell.style.background = IZU_HEX_COLORS[week] + '33'; // 20% opacity
      cell.dataset.ms = cellMs;
      cell.innerHTML = `
        <span class="icell-greg">${cellD.getDate()}</span>
        <span class="icell-mth-day">d${idx + 1}</span>
      `;
      cell.addEventListener('click', () => {
        selectedMs = cellMs;
        gregViewMs = cellMs; // sync gregorian view
        renderGregorianCalendar();
        renderIgboCalendar();
        renderDateCard();
      });
      row.appendChild(cell);
    }

    grid.appendChild(row);
  }
}


/* ═══════════════════════════════════════════════════════════════════════════
   RENDER: IGBO DATE CARD
   ═══════════════════════════════════════════════════════════════════════════ */

function renderDateCard() {
  const igbo       = Engine.toIgboDate(selectedMs);
  const dayColor   = igbo.isUboshiOmumu ? 'var(--primary)' : DAY_CSS_VARS[igbo.day] || 'var(--primary)';
  const cardEl     = document.getElementById('igbo-date-card');

  document.getElementById('idc-greg-date').textContent   = formatGregorianDate(selectedMs);
  document.getElementById('idc-dot').style.background    = dayColor;
  document.getElementById('idc-market-name').textContent = igbo.day;
  document.getElementById('idc-market-name').style.color = dayColor;

  const izuBanner = document.getElementById('izu-banner');

  if (igbo.isUboshiOmumu) {
    document.getElementById('chip-izu').textContent  = '—';
    document.getElementById('chip-onwa').textContent = '—';
    document.getElementById('chip-aro').textContent  = igbo.year;
    document.getElementById('idc-uboshi').classList.remove('hidden');
    document.getElementById('idc-progress-wrap').style.display = 'none';
    izuBanner.classList.add('hidden');
  } else {
    document.getElementById('chip-izu').textContent  = igbo.week;
    document.getElementById('chip-onwa').textContent = igbo.month;
    document.getElementById('chip-aro').textContent  = igbo.year;
    document.getElementById('idc-uboshi').classList.add('hidden');
    document.getElementById('idc-progress-wrap').style.display = '';

    const dayNum = igbo.dayWithinMonth + 1;  // 1-based
    const pct    = Math.round((dayNum / 28) * 100);
    document.getElementById('prog-day-label').textContent   = `Day ${dayNum} of 28`;
    document.getElementById('prog-pct-label').textContent   = `${pct}%`;
    document.getElementById('idc-progress-fill').style.width = `${pct}%`;

    // Izu info banner
    const izuInfo = IZU_DATA[igbo.weekIndex];
    if (izuInfo) {
      document.getElementById('izu-banner-icon').textContent    = izuInfo.icon;
      document.getElementById('izu-banner-name').textContent    = izuInfo.name;
      document.getElementById('izu-banner-tagline').textContent = izuInfo.tagline;
      izuBanner.classList.remove('hidden');
    }
  }
}


/* ═══════════════════════════════════════════════════════════════════════════
   RENDER: MARKET SCREEN
   ═══════════════════════════════════════════════════════════════════════════ */

function renderMarketScreen() {
  const todayIgbo = Engine.toIgboDate(today);
  const md        = MARKET_DAYS[todayIgbo.dayIndex];

  // Today card
  const tmc = document.getElementById('today-market-card');
  tmc.style.background = `linear-gradient(135deg, ${DAY_HEX[todayIgbo.dayIndex]}22 0%, var(--card) 100%)`;
  tmc.style.borderColor = DAY_HEX[todayIgbo.dayIndex] + '60';
  document.getElementById('tmc-icon').textContent     = md.icon;
  document.getElementById('tmc-day-name').textContent = md.name;
  document.getElementById('tmc-day-name').style.color = DAY_HEX[todayIgbo.dayIndex];
  document.getElementById('tmc-tagline').textContent  = md.tagline;
  document.getElementById('tmc-element').textContent  = `🌀 ${md.element}`;
  document.getElementById('tmc-direction').textContent = `📍 ${md.direction}`;
  document.getElementById('tmc-deity').textContent    = `✨ ${md.deity}`;

  // Market day cards list
  const list = document.getElementById('market-day-list');
  list.innerHTML = '';
  MARKET_DAYS.forEach((mday, idx) => {
    const isToday = idx === todayIgbo.dayIndex;
    const hex     = DAY_HEX[idx];
    const card    = document.createElement('div');
    card.className = 'mdc';
    card.innerHTML = `
      <div class="mdc-header">
        <div class="mdc-icon-wrap" style="background:${hex}22;border:1px solid ${hex}44">
          <span>${mday.icon}</span>
        </div>
        <div class="mdc-title-wrap">
          <div class="mdc-name" style="color:${hex}">${mday.name}</div>
          <div class="mdc-tagline">${mday.tagline}</div>
        </div>
        <div class="mdc-chevron">${chevronSVG()}</div>
      </div>
      <div class="mdc-body">
        <p class="mdc-significance">${mday.significance}</p>
        <div class="mdc-meta">
          <span class="meta-badge">🌀 ${mday.element}</span>
          <span class="meta-badge">📍 ${mday.direction}</span>
          <span class="meta-badge">✨ ${mday.deity}</span>
          ${isToday ? `<span class="meta-badge" style="color:${hex};border-color:${hex}60">⊙ TODAY</span>` : ''}
        </div>
        <div class="mdc-activities-title">Activities</div>
        ${mday.activities.map(a => `
          <div class="mdc-activity-item">
            <span class="activity-dot" style="background:${hex}"></span>
            <span>${a}</span>
          </div>`).join('')}
      </div>
    `;
    card.querySelector('.mdc-header').addEventListener('click', () => {
      card.classList.toggle('expanded');
    });
    list.appendChild(card);
  });
}


/* ═══════════════════════════════════════════════════════════════════════════
   RENDER: INFO SCREEN
   ═══════════════════════════════════════════════════════════════════════════ */

function renderInfoScreen() {
  renderIzuList();
  renderOnwaList();
}

function renderIzuList() {
  const container = document.getElementById('izu-list');
  container.innerHTML = '';
  IZU_DATA.forEach((izu, idx) => {
    const hex  = IZU_HEX_COLORS[idx];
    const card = document.createElement('div');
    card.className = 'izu-card';
    card.innerHTML = `
      <div class="izu-card-header">
        <div class="izu-card-icon" style="background:${hex}22;border:1px solid ${hex}44">
          ${izu.icon}
        </div>
        <div class="izu-card-title-wrap">
          <div class="izu-card-name">${izu.name}</div>
          <div class="izu-card-tagline">${izu.tagline}</div>
        </div>
        <div class="izu-card-chevron">${chevronSVG()}</div>
      </div>
      <div class="izu-card-body">
        <p class="izu-card-desc">${izu.desc}</p>
      </div>
    `;
    card.querySelector('.izu-card-header').addEventListener('click', () => {
      card.classList.toggle('expanded');
    });
    container.appendChild(card);
  });
}

function renderOnwaList() {
  const container = document.getElementById('onwa-list');
  container.innerHTML = '';
  ONWA_DATA.forEach((onwa, idx) => {
    const card = document.createElement('div');
    card.className = 'onwa-card';
    card.innerHTML = `
      <div class="onwa-card-header">
        <div class="onwa-num">${idx + 1}</div>
        <div class="onwa-title-wrap">
          <div class="onwa-name">${onwa.name}</div>
          <div class="onwa-season">🌿 ${onwa.season}</div>
        </div>
        <div class="onwa-chevron">${chevronSVG()}</div>
      </div>
      <div class="onwa-card-body">
        <div class="onwa-festival-badge">🎊 ${onwa.festival}</div>
        <p class="onwa-desc">${onwa.desc}</p>
        <div class="onwa-activities-title">Key Activities</div>
        <div class="onwa-activity-tags">
          ${onwa.activities.map(a => `<span class="onwa-tag">${a}</span>`).join('')}
        </div>
      </div>
    `;
    card.querySelector('.onwa-card-header').addEventListener('click', () => {
      card.classList.toggle('expanded');
    });
    container.appendChild(card);
  });
}


/* ═══════════════════════════════════════════════════════════════════════════
   RENDER: EVENTS SCREEN
   ═══════════════════════════════════════════════════════════════════════════ */

function renderEventsScreen() {
  const featured = FESTIVALS.find(f => f.featured);

  // Featured card
  const featEl = document.getElementById('featured-festival');
  if (featured) {
    const dayIdx = MARKET_DAYS.findIndex(m => m.name === featured.marketDay);
    const hex    = DAY_HEX[Math.max(0, dayIdx)];
    featEl.innerHTML = `
      <div class="featured-card">
        <span class="feat-badge">⭐ FEATURED</span>
        <div>
          <span class="feat-day-badge" style="background:${hex}">${featured.marketDay}</span>
        </div>
        <div class="feat-name">${featured.name}</div>
        <div class="feat-subtitle">${featured.subtitle}</div>
        <p class="feat-desc">${featured.desc}</p>
        <div class="feat-meta">
          <span class="feat-meta-item">📅 ${featured.igboMonth}</span>
          <span class="feat-meta-item">📍 ${featured.location}</span>
        </div>
      </div>
    `;
  }

  // Festival list (non-featured)
  const list = document.getElementById('festival-list');
  list.innerHTML = '';
  FESTIVALS.filter(f => !f.featured).forEach(fest => {
    const dayIdx = MARKET_DAYS.findIndex(m => m.name === fest.marketDay);
    const hex    = DAY_HEX[Math.max(0, dayIdx)];
    const card   = document.createElement('div');
    card.className = 'festival-card';
    card.innerHTML = `
      <div class="fc-header">
        <div class="fc-day-dot" style="background:${hex}">${fest.marketDay}</div>
        <div class="fc-title-wrap">
          <div class="fc-name">${fest.name}</div>
          <div class="fc-subtitle">${fest.subtitle}</div>
        </div>
        <div class="fc-chevron">${chevronSVG()}</div>
      </div>
      <div class="fc-body">
        <p class="fc-desc">${fest.desc}</p>
        <div class="fc-meta">
          <span class="fc-meta-item">📅 ${fest.igboMonth}</span>
          <span class="fc-meta-item">📍 ${fest.location}</span>
        </div>
      </div>
    `;
    card.querySelector('.fc-header').addEventListener('click', () => {
      card.classList.toggle('expanded');
    });
    list.appendChild(card);
  });
}


/* ═══════════════════════════════════════════════════════════════════════════
   NAVIGATION: TABS
   ═══════════════════════════════════════════════════════════════════════════ */

function switchTab(tab) {
  if (tab === currentTab) return;
  currentTab = tab;

  document.querySelectorAll('.screen').forEach(s => s.classList.add('hidden'));
  document.querySelectorAll('.bnav-item').forEach(b => b.classList.remove('active'));

  const screen = document.getElementById(`screen-${tab}`);
  if (screen) {
    screen.classList.remove('hidden');
    screen.scrollTop = 0;
  }
  const navBtn = document.querySelector(`.bnav-item[data-screen="${tab}"]`);
  if (navBtn) navBtn.classList.add('active');
}

document.querySelectorAll('.bnav-item').forEach(btn => {
  btn.addEventListener('click', () => switchTab(btn.dataset.screen));
});


/* ═══════════════════════════════════════════════════════════════════════════
   NAVIGATION: CALENDAR VIEW TOGGLE
   ═══════════════════════════════════════════════════════════════════════════ */

document.querySelectorAll('.view-toggle-btn').forEach(btn => {
  btn.addEventListener('click', () => {
    calView = btn.dataset.view;
    document.querySelectorAll('.view-toggle-btn').forEach(b => b.classList.remove('active'));
    btn.classList.add('active');

    if (calView === 'gregorian') {
      document.getElementById('gregorian-panel').classList.remove('hidden');
      document.getElementById('igbo-panel').classList.add('hidden');
    } else {
      document.getElementById('gregorian-panel').classList.add('hidden');
      document.getElementById('igbo-panel').classList.remove('hidden');
      igboViewMs = selectedMs; // sync to selected when switching
      renderIgboCalendar();
    }
  });
});


/* ═══════════════════════════════════════════════════════════════════════════
   NAVIGATION: GREGORIAN PREV/NEXT MONTH
   ═══════════════════════════════════════════════════════════════════════════ */

document.getElementById('greg-prev').addEventListener('click', () => {
  const d    = msToDate(gregViewMs);
  const prev = new Date(d.getFullYear(), d.getMonth() - 1, 1);
  gregViewMs = Engine._utc(prev.getFullYear(), prev.getMonth() + 1, 1);
  renderGregorianCalendar();
});

document.getElementById('greg-next').addEventListener('click', () => {
  const d    = msToDate(gregViewMs);
  const next = new Date(d.getFullYear(), d.getMonth() + 1, 1);
  gregViewMs = Engine._utc(next.getFullYear(), next.getMonth() + 1, 1);
  renderGregorianCalendar();
});


/* ═══════════════════════════════════════════════════════════════════════════
   NAVIGATION: IGBO PREV/NEXT MONTH
   ═══════════════════════════════════════════════════════════════════════════ */

document.getElementById('igbo-prev').addEventListener('click', () => {
  igboViewMs = Engine.prevIgboMonth(igboViewMs);
  renderIgboCalendar();
});

document.getElementById('igbo-next').addEventListener('click', () => {
  igboViewMs = Engine.nextIgboMonth(igboViewMs);
  renderIgboCalendar();
});


/* ═══════════════════════════════════════════════════════════════════════════
   HEADER BUTTONS: TODAY / THEME / SEARCH
   ═══════════════════════════════════════════════════════════════════════════ */

document.getElementById('todayBtn').addEventListener('click', () => {
  selectedMs  = today;
  gregViewMs  = today;
  igboViewMs  = today;
  renderGregorianCalendar();
  renderIgboCalendar();
  renderDateCard();
  renderMarketScreen();   // today's market day may update
  // Switch to calendar tab and gregorian view
  switchTab('calendar');
});

document.getElementById('themeBtn').addEventListener('click', () => {
  theme = theme === 'amber' ? 'green' : 'amber';
  document.documentElement.setAttribute('data-theme', theme);
  localStorage.setItem('igbo-theme', theme);
});


/* ═══════════════════════════════════════════════════════════════════════════
   SEARCH BOTTOM SHEET
   ═══════════════════════════════════════════════════════════════════════════ */

function openSheet() {
  document.getElementById('search-sheet').classList.remove('hidden');
  document.getElementById('sheet-overlay').classList.remove('hidden');
  // Pre-fill with selected date
  const d = msToDate(selectedMs);
  document.getElementById('s-greg-month').value = d.getMonth();
  document.getElementById('s-greg-day').value   = d.getDate();
  document.getElementById('s-greg-year').value  = d.getFullYear();
  const igbo = Engine.toIgboDate(selectedMs);
  if (!igbo.isUboshiOmumu) {
    document.getElementById('s-igbo-month').value = igbo.monthIndex;
    document.getElementById('s-igbo-day').value   = igbo.dayWithinMonth + 1;
    document.getElementById('s-igbo-year').value  = igbo.year;
  }
  clearSheetErrors();
}

function closeSheet() {
  document.getElementById('search-sheet').classList.add('hidden');
  document.getElementById('sheet-overlay').classList.add('hidden');
}

function clearSheetErrors() {
  document.getElementById('s-greg-error').classList.add('hidden');
  document.getElementById('s-igbo-error').classList.add('hidden');
}

document.getElementById('searchBtn').addEventListener('click', openSheet);
document.getElementById('sheet-close').addEventListener('click', closeSheet);
document.getElementById('sheet-overlay').addEventListener('click', closeSheet);

// Sheet sub-tabs
document.querySelectorAll('.sheet-tab').forEach(btn => {
  btn.addEventListener('click', () => {
    document.querySelectorAll('.sheet-tab').forEach(b => b.classList.remove('active'));
    btn.classList.add('active');
    const pane = btn.dataset.stab;
    document.getElementById('sheet-greg').classList.toggle('hidden', pane !== 'gregorian');
    document.getElementById('sheet-igbo').classList.toggle('hidden', pane !== 'igbo');
    clearSheetErrors();
  });
});

// Gregorian search
document.getElementById('s-greg-go').addEventListener('click', () => {
  const month = parseInt(document.getElementById('s-greg-month').value);
  const day   = parseInt(document.getElementById('s-greg-day').value);
  const year  = parseInt(document.getElementById('s-greg-year').value);
  const errEl = document.getElementById('s-greg-error');

  if (isNaN(day) || isNaN(year) || day < 1 || day > 31 || year < 1 || year > 9999) {
    errEl.classList.remove('hidden'); return;
  }
  // Validate date existence
  const testDate = new Date(year, month, day);
  if (testDate.getMonth() !== month || testDate.getDate() !== day) {
    errEl.classList.remove('hidden'); return;
  }
  errEl.classList.add('hidden');
  const ms = Engine._utc(year, month + 1, day);
  selectedMs = ms; gregViewMs = ms; igboViewMs = ms;
  renderGregorianCalendar();
  renderIgboCalendar();
  renderDateCard();
  closeSheet();
  switchTab('calendar');
});

// Igbo Era search
document.getElementById('s-igbo-go').addEventListener('click', () => {
  const monthIdx = parseInt(document.getElementById('s-igbo-month').value);
  const day      = parseInt(document.getElementById('s-igbo-day').value);
  const igboYear = parseInt(document.getElementById('s-igbo-year').value);
  const errEl    = document.getElementById('s-igbo-error');

  if (isNaN(day) || isNaN(igboYear) || day < 1 || day > 28 || igboYear < 1) {
    errEl.classList.remove('hidden'); return;
  }
  const yearBase = igboYear - Engine.IGBO_ERA_OFFSET;
  // day is 1-based, dayWithinMonth is 0-based
  const firstDay  = Engine.firstDayOfIgboMonth(yearBase, monthIdx);
  const targetMs  = firstDay + (day - 1) * 86400000;
  errEl.classList.add('hidden');
  selectedMs = targetMs; gregViewMs = targetMs; igboViewMs = targetMs;
  renderGregorianCalendar();
  renderIgboCalendar();
  renderDateCard();
  closeSheet();
  switchTab('calendar');
});

// Update Igbo year hint
document.getElementById('s-igbo-year').addEventListener('input', () => {
  const val  = parseInt(document.getElementById('s-igbo-year').value);
  const hint = document.getElementById('s-igbo-hint');
  if (!isNaN(val)) {
    hint.textContent = `≈ Gregorian year ${val - Engine.IGBO_ERA_OFFSET}`;
  } else {
    hint.textContent = 'Igbo year = Gregorian year + 5000';
  }
});


/* ═══════════════════════════════════════════════════════════════════════════
   INFO SCREEN SUB-TABS
   ═══════════════════════════════════════════════════════════════════════════ */

document.querySelectorAll('.info-tab').forEach(btn => {
  btn.addEventListener('click', () => {
    infoTab = btn.dataset.itab;
    document.querySelectorAll('.info-tab').forEach(b => b.classList.remove('active'));
    btn.classList.add('active');
    document.getElementById('izu-list').classList.toggle('hidden', infoTab !== 'izu');
    document.getElementById('onwa-list').classList.toggle('hidden', infoTab !== 'onwa');
  });
});


/* ═══════════════════════════════════════════════════════════════════════════
   INIT
   ═══════════════════════════════════════════════════════════════════════════ */

(function init() {
  // Restore theme
  const saved = localStorage.getItem('igbo-theme');
  if (saved === 'green' || saved === 'amber') {
    theme = saved;
    document.documentElement.setAttribute('data-theme', theme);
  }

  // Initial renders (static screens built once)
  renderMarketScreen();
  renderInfoScreen();
  renderEventsScreen();

  // Calendar (dynamic, updated on navigation)
  renderGregorianCalendar();
  renderIgboCalendar();
  renderDateCard();

  // Set default active screen
  switchTab('calendar');
})();
