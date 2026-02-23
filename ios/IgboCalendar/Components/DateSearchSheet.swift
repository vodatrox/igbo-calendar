import SwiftUI

private let gregorianMonths = [
    "January","February","March","April","May","June",
    "July","August","September","October","November","December"
]

private let igboMonthOptions = IgboCalendarEngine.igboMonths + ["Ubọchị Ọmụmụ"]
private let uboshiIdx = 13

// MARK: - Date Search Sheet

struct DateSearchSheet: View {
    let currentDate:     Date
    let currentIgboDate: IgboDate
    let onNavigate:      (Date) -> Void
    let onDismiss:       () -> Void

    @Environment(\.igboColors) private var colors

    @State private var activeTab: Int = 0  // 0 = Gregorian, 1 = Igbo

    // Gregorian form
    @State private var gMonthIdx: Int    = 0
    @State private var gDay:      String = ""
    @State private var gYear:     String = ""

    // Igbo form
    @State private var iMonthIdx: Int    = 0
    @State private var iDay:      String = ""
    @State private var iAro:      String = ""

    init(currentDate: Date, currentIgboDate: IgboDate,
         onNavigate: @escaping (Date) -> Void, onDismiss: @escaping () -> Void)
    {
        self.currentDate     = currentDate
        self.currentIgboDate = currentIgboDate
        self.onNavigate      = onNavigate
        self.onDismiss       = onDismiss

        let cal = Calendar.current
        _gMonthIdx = State(initialValue: cal.component(.month, from: currentDate) - 1)
        _gDay      = State(initialValue: "\(cal.component(.day,  from: currentDate))")
        _gYear     = State(initialValue: "\(cal.component(.year, from: currentDate))")

        let initIgboMonth = currentIgboDate.isUboshiOmumu ? uboshiIdx : currentIgboDate.monthIndex
        _iMonthIdx = State(initialValue: initIgboMonth)
        _iDay      = State(initialValue: currentIgboDate.isUboshiOmumu ? "" :
                           "\(currentIgboDate.dayWithinMonth + 1)")
        _iAro      = State(initialValue: "\(currentIgboDate.year)")
    }

    // MARK: Validation

    private var resolvedDate: Date? {
        activeTab == 0
            ? resolveGregorianDate(monthIdx: gMonthIdx, dayStr: gDay, yearStr: gYear)
            : resolveIgboDate(monthIdx: iMonthIdx, dayStr: iDay, aroStr: iAro)
    }

    private var hasInput: Bool {
        activeTab == 0
            ? (!gDay.isEmpty && !gYear.isEmpty)
            : (!iAro.isEmpty && (iMonthIdx == uboshiIdx || !iDay.isEmpty))
    }

    var body: some View {
        NavigationStack {
            ScrollView {
                VStack(alignment: .leading, spacing: 0) {
                    // Tab toggle
                    HStack(spacing: 4) {
                        ForEach(["Gregorian", "Igbo Era"].indices, id: \.self) { idx in
                            let label    = ["Gregorian", "Igbo Era"][idx]
                            let selected = activeTab == idx
                            Text(label)
                                .font(.system(size: 13, weight: selected ? .bold : .medium))
                                .foregroundColor(selected ? colors.onPrimary : colors.onSurfaceMuted)
                                .frame(maxWidth: .infinity)
                                .padding(.vertical, 9)
                                .background(
                                    RoundedRectangle(cornerRadius: 9)
                                        .fill(selected ? colors.primary : Color.clear)
                                )
                                .onTapGesture { activeTab = idx }
                        }
                    }
                    .padding(4)
                    .background(RoundedRectangle(cornerRadius: 12).fill(colors.surfaceVariant))
                    .padding(.bottom, 20)

                    // Form fields
                    if activeTab == 0 {
                        GregorianFields(
                            monthIdx:  $gMonthIdx,
                            day:       $gDay,
                            year:      $gYear
                        )
                    } else {
                        IgboFields(
                            monthIdx:  $iMonthIdx,
                            day:       $iDay,
                            aro:       $iAro
                        )
                    }

                    Spacer().frame(height: 12)

                    // Validation error
                    if hasInput && resolvedDate == nil {
                        Text("Please enter a valid date.")
                            .font(.system(size: 12))
                            .foregroundColor(colors.error)
                            .padding(.bottom, 4)
                    }

                    Spacer().frame(height: 12)

                    // Go button
                    Button {
                        if let date = resolvedDate {
                            onNavigate(date)
                            onDismiss()
                        }
                    } label: {
                        Text("Go to Date")
                            .font(.system(size: 15, weight: .bold))
                            .foregroundColor(resolvedDate != nil ? colors.onPrimary : colors.onSurfaceMuted)
                            .frame(maxWidth: .infinity)
                            .padding(.vertical, 14)
                            .background(
                                RoundedRectangle(cornerRadius: 14)
                                    .fill(resolvedDate != nil ? colors.primary : colors.surfaceVariant)
                            )
                    }
                    .disabled(resolvedDate == nil)
                }
                .padding(.horizontal, 20)
                .padding(.bottom, 32)
            }
            .background(colors.surface.ignoresSafeArea())
            .navigationTitle("Go to Date")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button(action: onDismiss) {
                        Image(systemName: "xmark")
                            .foregroundColor(colors.onSurfaceMuted)
                    }
                }
            }
        }
    }
}

// MARK: - Gregorian Fields

private struct GregorianFields: View {
    @Binding var monthIdx: Int
    @Binding var day:      String
    @Binding var year:     String

    @Environment(\.igboColors) private var colors

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            // Month picker
            VStack(alignment: .leading, spacing: 4) {
                Text("Month")
                    .font(.system(size: 12, weight: .medium))
                    .foregroundColor(colors.onSurfaceMuted)
                Picker("Month", selection: $monthIdx) {
                    ForEach(0..<12, id: \.self) { i in
                        Text(gregorianMonths[i]).tag(i)
                    }
                }
                .pickerStyle(.menu)
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.horizontal, 12)
                .padding(.vertical, 8)
                .background(
                    RoundedRectangle(cornerRadius: 10)
                        .strokeBorder(colors.divider, lineWidth: 1)
                )
                .accentColor(colors.primary)
            }

            HStack(spacing: 12) {
                ThemedTextField(
                    title: "Day",
                    placeholder: "1–31",
                    text: $day,
                    maxLength: 2,
                    isNumeric: true
                )
                .frame(maxWidth: .infinity)

                ThemedTextField(
                    title: "Year",
                    placeholder: "e.g. 2025",
                    text: $year,
                    maxLength: 4,
                    isNumeric: true
                )
                .frame(maxWidth: .infinity)
            }
        }
    }
}

// MARK: - Igbo Fields

private struct IgboFields: View {
    @Binding var monthIdx: Int
    @Binding var day:      String
    @Binding var aro:      String

    @Environment(\.igboColors) private var colors

    private var isUboshi: Bool { monthIdx == uboshiIdx }

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            // Month picker
            VStack(alignment: .leading, spacing: 4) {
                Text("Ọnwa (Month)")
                    .font(.system(size: 12, weight: .medium))
                    .foregroundColor(colors.onSurfaceMuted)
                Picker("Ọnwa", selection: $monthIdx) {
                    ForEach(0..<igboMonthOptions.count, id: \.self) { i in
                        Text(igboMonthOptions[i])
                            .tag(i)
                    }
                }
                .pickerStyle(.menu)
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.horizontal, 12)
                .padding(.vertical, 8)
                .background(
                    RoundedRectangle(cornerRadius: 10)
                        .strokeBorder(colors.divider, lineWidth: 1)
                )
                .accentColor(colors.primary)
                .onChange(of: monthIdx) { newVal in
                    if newVal == uboshiIdx { day = "" }
                }
            }

            HStack(spacing: 12) {
                if !isUboshi {
                    ThemedTextField(
                        title: "Day (1–28)",
                        placeholder: "1–28",
                        text: $day,
                        maxLength: 2,
                        isNumeric: true
                    )
                    .frame(maxWidth: .infinity)
                }

                ThemedTextField(
                    title: "Aro (Igbo Year)",
                    placeholder: "e.g. 7025",
                    text: $aro,
                    maxLength: 5,
                    isNumeric: true
                )
                .frame(maxWidth: .infinity)
            }

            // Gregorian year hint
            if let aroNum = Int(aro), aroNum > IgboCalendarEngine.igboEraOffset {
                Text("Aro \(aroNum) ≈ Gregorian \(aroNum - IgboCalendarEngine.igboEraOffset)")
                    .font(.system(size: 11))
                    .foregroundColor(colors.onSurfaceMuted)
            }
        }
    }
}

// MARK: - Themed Text Field

private struct ThemedTextField: View {
    let title:      String
    let placeholder: String
    @Binding var text: String
    let maxLength:  Int
    let isNumeric:  Bool

    @Environment(\.igboColors) private var colors
    @FocusState private var focused: Bool

    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text(title)
                .font(.system(size: 12, weight: .medium))
                .foregroundColor(focused ? colors.primary : colors.onSurfaceMuted)

            TextField(placeholder, text: $text)
                .keyboardType(isNumeric ? .numberPad : .default)
                .focused($focused)
                .font(.system(size: 15))
                .foregroundColor(colors.onSurface)
                .padding(.horizontal, 12)
                .padding(.vertical, 8)
                .background(
                    RoundedRectangle(cornerRadius: 10)
                        .strokeBorder(focused ? colors.primary : colors.divider, lineWidth: focused ? 1.5 : 1)
                )
                .onChange(of: text) { newVal in
                    if isNumeric {
                        let filtered = newVal.filter(\.isNumber)
                        let clamped  = String(filtered.prefix(maxLength))
                        if clamped != newVal { text = clamped }
                    } else if newVal.count > maxLength {
                        text = String(newVal.prefix(maxLength))
                    }
                }
        }
    }
}

// MARK: - Resolution Helpers

private func resolveGregorianDate(monthIdx: Int, dayStr: String, yearStr: String) -> Date? {
    guard let day  = Int(dayStr),
          let year = Int(yearStr) else { return nil }
    var c = DateComponents()
    c.year = year; c.month = monthIdx + 1; c.day = day
    guard let d = Calendar.current.date(from: c),
          Calendar.current.component(.day,   from: d) == day,
          Calendar.current.component(.month, from: d) == monthIdx + 1,
          Calendar.current.component(.year,  from: d) == year else { return nil }
    return d.normalized
}

private func resolveIgboDate(monthIdx: Int, dayStr: String, aroStr: String) -> Date? {
    guard let aro = Int(aroStr), aro > IgboCalendarEngine.igboEraOffset else { return nil }
    let yearBase = aro - IgboCalendarEngine.igboEraOffset
    if monthIdx == uboshiIdx {
        return IgboCalendarEngine.uboshiOmumuDate(igboYearBase: yearBase)
    }
    guard let day = Int(dayStr), day >= 1, day <= 28 else { return nil }
    return IgboCalendarEngine.adding(
        days: day - 1,
        to: IgboCalendarEngine.firstGregorianDateOfIgboMonth(igboYearBase: yearBase, monthIndex: monthIdx)
    )
}
