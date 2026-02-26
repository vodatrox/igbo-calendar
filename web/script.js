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
  { name:"Ọnwa Mbụ",       season:"Dry Season",        desc:"The first month of the Igbo year corresponds to the beginning of the dry season. It is a time of celebration, giving thanks for the previous year's harvest, and setting intentions for the new year.",   activities:["New Year ceremonies","Igu Aro proclamation","Thanksgiving feasts","Community planning"] },
  { name:"Ọnwa Abụọ",      season:"Dry Season",        desc:"The second month deepens into the dry season. Preparations for farming begin — tools are repaired, seeds are selected, and land is surveyed. Social visits and marriages are common this month.", activities:["Tool preparation","Seed selection","Weddings & unions","Inter-village visits"] },
  { name:"Ọnwa Ife Eke",   season:"Early Rains",       desc:"Named after the sacred Eke market day, this month heralds the return of rains. The Eke deity is honored with special prayers and offerings as communities prepare their fields for planting.", activities:["Rain prayers","Field preparation","Eke deity offerings","Planting ceremonies"] },
  { name:"Ọnwa Anọ",       season:"Rainy Season",      desc:"The fourth month sees the full arrival of the rainy season. Planting is in full swing, and communities work together to tend their crops. Water spirits are honored during this month.",    activities:["Active planting","Communal farming","Water spirit ceremonies","Rain festivals"] },
  { name:"Ọnwa Agwụ",      season:"Rainy Season",      desc:"Named after Agwụ, the deity of medicine and divination, this month is sacred to healers and diviners. Medicinal plants are at their most potent, and healing ceremonies are performed.",  activities:["Healing ceremonies","Medicinal plant harvesting","Divination sessions","Agwụ deity festivals"] },
  { name:"Ọnwa Ifejiọkụ",  season:"Mid-Rainy Season",  desc:"Dedicated to Ifejiọkụ, the yam deity. This month is critical for yam cultivation — the most important crop in Igboland. Prayers and offerings to Ifejiọkụ ensure a bountiful harvest.", activities:["Yam tending rituals","Ifejiọkụ offerings","Farm maintenance","Mid-year ceremonies"] },
  { name:"Ọnwa Alọm Chi",  season:"Late Rainy Season", desc:"A month of spiritual introspection and communion with one's personal deity (Chi). Individuals seek to align with their destiny through prayer, reflection, and consultation with diviners.", activities:["Personal shrine rituals","Chi ceremonies","Destiny consultations","Spiritual retreats"] },
  { name:"Ọnwa Ilọ Mmụọ", season:"Harvest Season",    desc:"The month of returning spirits. Ancestor veneration reaches its peak as the community honors departed loved ones. Masquerades representing ancestral spirits perform elaborate ceremonies.",        activities:["Ancestor veneration","Masquerade festivals","Mmụọ (spirit) ceremonies","Libation rituals"] },
  { name:"Ọnwa Ana",       season:"Harvest Season",    desc:"Named after Ana (Ala), the earth goddess — supreme deity of the Igbo. This harvest month is the most sacred, as gratitude is offered to the earth for its abundance. The harvest festival begins.",   activities:["Harvest festivals","Ana/Ala deity ceremonies","Yam harvest","Community feasts"] },
  { name:"Ọnwa Okike",     season:"Harvest Season",    desc:"The month of Okike — the divine force of creation. It celebrates the abundance of the harvest and the creative power of the universe. The Ofala royal festival is often held this month.",          activities:["Ofala festival","Royal ceremonies","Creation celebrations","Artistic performances"] },
  { name:"Ọnwa Ajana",     season:"Dry Season Onset",  desc:"As the dry season approaches, communities begin storing food and preparing for the lean months ahead. Marriages are common this month as families have abundant resources to celebrate.",                 activities:["Food preservation","Marriage ceremonies","Trade expeditions","Community storage preparation"] },
  { name:"Ọnwa Ede Ajana", season:"Early Dry Season",  desc:"The twelfth month is named for the cocoyam harvest alongside the ongoing dry season preparations. Elders gather to review the year and begin planning for the year ahead.",                         activities:["Cocoyam harvest","Year-end reviews","Elder councils","Gift-giving traditions"] },
  { name:"Ọnwa Ụzọ Alụsị",season:"Late Dry Season",   desc:"The final month of the Igbo year — the path of the deities. Shrines are cleaned, deities are propitiated, and the community makes peace with all spiritual forces before the new year begins. Ends with Ubọchị Ọmụmụ.", activities:["Shrine cleansing","Deity propitiation","Year-end reconciliation","Ubọchị Ọmụmụ celebration"] }
];

const MARKET_DAYS = [
  { name:"EKE",  icon:"🌅", tagline:"Day of Creation & Earth",
    color:"var(--eke)", bg:"var(--eke-bg)",
    direction:"East", deity:"Eke (Earth Deity)",
    significance:"Eke is the most sacred of all market days — the day of creation and new beginnings. It is associated with the earth deity Eke, who governs life, fertility, and the land itself. Communities treat this day with deep reverence.",
    activities:["Major market transactions","Ritual cleansing ceremonies","New business ventures","Planting & harvest ceremonies","Birth and naming rituals"] },
  { name:"ORIE", icon:"🌤️", tagline:"Day of Sky & Community",
    color:"var(--orie)", bg:"var(--orie-bg)",
    direction:"West", deity:"Orie (Sky Spirit)",
    significance:"Orie is associated with the sky and communal harmony. It is a day for gathering, resolving disputes, and strengthening the bonds of the community. The spirit of Orie encourages cooperation and justice.",
    activities:["Community meetings","Dispute resolution & justice","Trading in livestock","Village council sessions","Communal work parties"] },
  { name:"AFỌ",  icon:"🌿", tagline:"Day of Rest & Farming",
    color:"var(--afo)", bg:"var(--afo-bg)",
    direction:"North", deity:"Afọ (Farm & Rest Deity)",
    significance:"Afọ is a sacred day of rest and reflection, especially for farming communities. It honors the agricultural cycle and the spirits that govern the land's fertility. On Afọ, heavy physical labor is traditionally avoided.",
    activities:["Rest and spiritual reflection","Farm planning & consulting elders","Ancestral remembrance","Herbal medicine preparation","Quiet family gatherings"] },
  { name:"NKWỌ", icon:"🌙", tagline:"Day of Completion & Cycles",
    color:"var(--nkwo)", bg:"var(--nkwo-bg)",
    direction:"South", deity:"Nkwọ (Cycle Deity)",
    significance:"Nkwọ marks the conclusion of the Izu cycle. It is a powerful day of endings and new beginnings — a time to settle accounts, prepare for the next cycle, and honor the continuity of Igbo tradition.",
    activities:["Cycle conclusion ceremonies","Settling of debts & accounts","Preparation for new Izu","Major masquerade festivals","Elders' strategic gatherings"] }
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
