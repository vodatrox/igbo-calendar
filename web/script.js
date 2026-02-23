class IgboCalendar {
    static IGBO_DAYS = ["EKE", "ORIE", "AFỌ", "NKWỌ"];
    static IGBO_WEEKS = [
        "Izu Mbụ", "Izu Abụọ", "Izu Atọ", "Izu Anọ",
        "Izu Ise", "Izu Isii", "Izu Asaa"
    ];
    static IGBO_MONTHS = [
        "Ọnwa Mbụ", "Ọnwa Abụọ", "Ọnwa Ife Eke", "Ọnwa Anọ",
        "Ọnwa Agwụ", "Ọnwa Ifejiọkụ", "Ọnwa Alọm Chi",
        "Ọnwa Ilọ Mmụọ", "Ọnwa Ana", "Ọnwa Okike",
        "Ọnwa Ajana", "Ọnwa Ede Ajana", "Ọnwa Ụzọ Alụsị"
    ];
    static DAYS_IN_YEAR = (13 * 28) + 1;
    static REFERENCE_DATE = new Date(2023, 8, 15);
    static REFERENCE_IGBO_DAY_INDEX = 2;
    static REFERENCE_IGBO_WEEK_INDEX = 0;
    static REFERENCE_IGBO_MONTH_INDEX = 7;
    static REFERENCE_IGBO_YEAR = 2023;

    constructor(gregorianDate) {
        this.gregorianDate = new Date(gregorianDate);
    }

    toIgboDate() {
        const deltaDays = Math.floor((this.gregorianDate - IgboCalendar.REFERENCE_DATE) / 86400000);
        let daysIntoYear = (7 * 28) + deltaDays;
        let igboYear = IgboCalendar.REFERENCE_IGBO_YEAR + Math.floor(daysIntoYear / IgboCalendar.DAYS_IN_YEAR);
        let remainingDays = daysIntoYear % IgboCalendar.DAYS_IN_YEAR;

        if (remainingDays < 0) {
            igboYear -= 1;
            remainingDays += IgboCalendar.DAYS_IN_YEAR;
        }

        let igboDayIndex = (IgboCalendar.REFERENCE_IGBO_DAY_INDEX + deltaDays) % 4;
        igboDayIndex = igboDayIndex < 0 ? igboDayIndex + 4 : igboDayIndex;

        if (remainingDays === IgboCalendar.DAYS_IN_YEAR - 1) {
            return {
                day: IgboCalendar.IGBO_DAYS[igboDayIndex],
                week: "Ubọchị Ọmụmụ",
                month: "Ubọchị Ọmụmụ",
                year: igboYear,
                isUboshiOmumu: true
            };
        }

        const igboMonthIndex = Math.floor(remainingDays / 28);
        const dayWithinMonth = remainingDays % 28;
        const igboWeekIndex = Math.floor(dayWithinMonth / 4);

        return {
            day: IgboCalendar.IGBO_DAYS[igboDayIndex],
            week: IgboCalendar.IGBO_WEEKS[igboWeekIndex],
            month: IgboCalendar.IGBO_MONTHS[igboMonthIndex],
            year: igboYear,
            dayWithinMonth,
            isUboshiOmumu: false
        };
    }
}

let currentGregorianDate = new Date();
let currentIgboDate = new Date();

// Updated JavaScript with date picker functionality
const months = [
    'January', 'February', 'March', 'April', 'May', 'June',
    'July', 'August', 'September', 'October', 'November', 'December'
];

const yearSelect = document.getElementById('gregorianYear');
const monthSelect = document.getElementById('gregorianMonth');

function populateDatePicker() {
    // Populate years (from 2000 to 2030)
    const currentYear = new Date().getFullYear();
    for (let year = 1920; year <= 2030; year++) {
        const option = document.createElement('option');
        option.value = year;
        option.textContent = year;
        option.selected = year === currentYear;
        yearSelect.appendChild(option);
    }

    // Populate months
    months.forEach((month, index) => {
        const option = document.createElement('option');
        option.value = index;
        option.textContent = month;
        option.selected = index === currentGregorianDate.getMonth();
        monthSelect.appendChild(option);
    });
}

function generateGregorianCalendar() {
    const month = currentGregorianDate.getMonth();
    const year = currentGregorianDate.getFullYear();

    // Update selects
    monthSelect.value = month;
    yearSelect.value = year;

    const firstDay = new Date(year, month, 1);
    const lastDay = new Date(year, month + 1, 0);
    const daysInMonth = lastDay.getDate();
    const startingDay = firstDay.getDay();

    const grid = document.getElementById('gregorianDays');
    grid.innerHTML = '';

    // Empty days
    for (let i = 0; i < startingDay; i++) {
        const empty = document.createElement('div');
        empty.classList.add('day', 'empty');
        grid.appendChild(empty);
    }

    // Actual days
    for (let day = 1; day <= daysInMonth; day++) {
        const dayDiv = document.createElement('div');
        dayDiv.classList.add('day');
        dayDiv.textContent = day;
        dayDiv.dataset.date = new Date(year, month, day).toISOString();
        dayDiv.addEventListener('click', handleGregorianDayClick);
        grid.appendChild(dayDiv);
    }
}

function generateIgboCalendar(date) {
    const igboDate = new IgboCalendar(date).toIgboDate();
    const uboshiOmumu = document.getElementById('uboshiOmumu');
    const calendarContent = document.getElementById('igboCalendarContent');
    const daysGrid = document.getElementById('igboDays');

    document.getElementById('igboMonthYear').textContent =
        `${igboDate.month} ${igboDate.year}`;

    // Add window resize handler
    let resizeTimer;
    window.addEventListener('resize', () => {
        clearTimeout(resizeTimer);
        resizeTimer = setTimeout(() => {
            generateGregorianCalendar();
            generateIgboCalendar(currentIgboDate);
        }, 250);
    });

    if (igboDate.isUboshiOmumu) {
        uboshiOmumu.classList.add('active');
        calendarContent.style.display = 'none';
        document.getElementById('omumuDay').textContent = igboDate.day;
    } else {
        uboshiOmumu.classList.remove('active');
        calendarContent.style.display = 'block';
        daysGrid.innerHTML = '';

        const weekColors = getComputedStyle(document.documentElement)
            .getPropertyValue('--week-colors')
            .split(',')
            .map(c => c.trim());

        const weekText = getComputedStyle(document.documentElement)
            .getPropertyValue('--week-text');

        for (let i = 0; i < 28; i++) {
            const tempDate = new Date(date);
            tempDate.setDate(date.getDate() - igboDate.dayWithinMonth + i);
            const tempIgbo = new IgboCalendar(tempDate).toIgboDate();
            const weekIndex = Math.floor(i / 4);

            const dayDiv = document.createElement('div');
            dayDiv.classList.add('day');
            dayDiv.style.setProperty('--week-color', weekColors[weekIndex % 7]);
            dayDiv.style.setProperty('--week-text', weekText);

            dayDiv.innerHTML = `
                    <div class="date-number">${i+1}</div>
                    <div class="day-name">${tempIgbo.day}</div>
                    <div class="week-name">${tempIgbo.week}</div>
                `;

            if (tempDate.toDateString() === date.toDateString()) {
                dayDiv.classList.add('highlight');
            }

            daysGrid.appendChild(dayDiv);
        }
    }
}

function handleGregorianDayClick(e) {
    document.querySelectorAll('.highlight').forEach(el => el.classList.remove('highlight'));
    e.target.classList.add('highlight');

    const selectedDate = new Date(e.target.dataset.date);
    currentIgboDate = selectedDate;
    generateIgboCalendar(currentIgboDate);
}


document.getElementById('igboPrevMonth').addEventListener('click', () => {
    currentIgboDate.setMonth(currentIgboDate.getMonth() - 1);
    generateIgboCalendar(currentIgboDate);
});

document.getElementById('igboNextMonth').addEventListener('click', () => {
    currentIgboDate.setMonth(currentIgboDate.getMonth() + 1);
    generateIgboCalendar(currentIgboDate);
});

// Remove dark mode toggle click handler and replace with:
document.getElementById('toggleDarkMode').addEventListener('click', () => {
    document.body.classList.toggle('dark-mode');
    localStorage.setItem('darkMode', document.body.classList.contains('dark-mode'));
    generateIgboCalendar(currentIgboDate);
});

// Add persistent dark mode
if (localStorage.getItem('darkMode') === 'false') {
    document.body.classList.remove('dark-mode');
}

// Event Listeners for date picker
monthSelect.addEventListener('change', () => {
    currentGregorianDate.setMonth(parseInt(monthSelect.value));
    generateGregorianCalendar();
});

yearSelect.addEventListener('change', () => {
    currentGregorianDate.setFullYear(parseInt(yearSelect.value));
    generateGregorianCalendar();
});

document.getElementById('gregorianPrevMonth').addEventListener('click', () => {
    currentGregorianDate.setMonth(currentGregorianDate.getMonth() - 1);
    generateGregorianCalendar();
});

document.getElementById('gregorianNextMonth').addEventListener('click', () => {
    currentGregorianDate.setMonth(currentGregorianDate.getMonth() + 1);
    generateGregorianCalendar();
});

// Initialize
populateDatePicker();
generateGregorianCalendar();
generateIgboCalendar(currentGregorianDate);