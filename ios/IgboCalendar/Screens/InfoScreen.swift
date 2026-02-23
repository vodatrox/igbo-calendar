import SwiftUI

private enum InfoTab { case weeks, months }

// MARK: - Info Screen

struct InfoScreen: View {
    @EnvironmentObject private var viewModel: CalendarViewModel
    @Environment(\.igboColors) private var colors
    @State private var activeTab: InfoTab = .weeks

    var body: some View {
        VStack(spacing: 0) {
            // Header
            VStack(alignment: .leading, spacing: 4) {
                Text("Igbo Calendar")
                    .font(.system(size: 26, weight: .black))
                    .foregroundColor(colors.primary)
                Text("Structure & Heritage")
                    .font(.system(size: 13))
                    .foregroundColor(colors.onSurfaceMuted)
            }
            .frame(maxWidth: .infinity, alignment: .leading)
            .padding(.horizontal, 20)
            .padding(.vertical, 20)
            .background(
                LinearGradient(
                    colors: [colors.surface, colors.background],
                    startPoint: .top, endPoint: .bottom
                )
            )

            // Tab bar
            HStack(spacing: 4) {
                ForEach([InfoTab.weeks, .months], id: \.self) { tab in
                    let selected = activeTab == tab
                    let label    = tab == .weeks ? "Izu (Weeks)" : "Ọnwa (Months)"
                    Text(label)
                        .font(.system(size: 13, weight: selected ? .bold : .regular))
                        .foregroundColor(selected ? colors.onPrimary : colors.onSurfaceMuted)
                        .frame(maxWidth: .infinity)
                        .padding(.vertical, 9)
                        .background(
                            RoundedRectangle(cornerRadius: 9)
                                .fill(selected ? colors.primary : Color.clear)
                        )
                        .onTapGesture { withAnimation { activeTab = tab } }
                }
            }
            .padding(4)
            .background(RoundedRectangle(cornerRadius: 12).fill(colors.surfaceVariant))
            .padding(.horizontal, 20)
            .padding(.vertical, 4)

            ScrollView {
                VStack(spacing: 0) {
                    if activeTab == .weeks {
                        WeeksContent()
                    } else {
                        MonthsContent()
                    }
                    Spacer().frame(height: 24)
                }
            }
        }
        .background(colors.background.ignoresSafeArea())
    }
}

// MARK: - Weeks Content

private struct WeeksContent: View {
    @Environment(\.igboColors) private var colors

    var body: some View {
        VStack(spacing: 0) {
            InfoOverviewCard(
                emoji:   "📅",
                title:   "Izu — The Igbo Week",
                content: "The Igbo month (Ọnwa) is divided into 7 weeks (Izu), each consisting of 4 market days: Eke, Orie, Afọ, and Nkwọ. Each Izu carries its own spiritual energy, traditional purpose, and communal significance."
            )
            .padding(.horizontal, 16)
            .padding(.top, 12)

            Spacer().frame(height: 16)

            ForEach(Array(IzuData.weeks.enumerated()), id: \.element.id) { idx, izu in
                IzuCard(izu: izu, color: izuColors[idx])
                    .padding(.horizontal, 16)
                    .padding(.bottom, 10)
            }

            DidYouKnowCard(
                text: "In many Igbo communities, Izu Asaa is a sacred time when elders convene to consult divination and determine the planting calendar for the upcoming year based on cosmic signs."
            )
            .padding(.horizontal, 16)
        }
    }
}

// MARK: - Months Content

private struct MonthsContent: View {
    @Environment(\.igboColors) private var colors

    var body: some View {
        VStack(spacing: 0) {
            InfoOverviewCard(
                emoji:   "🌙",
                title:   "Ọnwa — The Igbo Month",
                content: "The Igbo year (Aro) consists of 13 months (Ọnwa) of 28 days each, plus one sacred day — Ubọchị Ọmụmụ (New Year Day). Each month is named after a deity, season, or cultural significance."
            )
            .padding(.horizontal, 16)
            .padding(.top, 12)

            Spacer().frame(height: 16)

            ForEach(Array(IzuData.months.enumerated()), id: \.element.id) { idx, onwa in
                OnwaCard(onwa: onwa, index: idx)
                    .padding(.horizontal, 16)
                    .padding(.bottom, 10)
            }

            DidYouKnowCard(
                text: "Ubọchị Ọmụmụ — the 365th day of the Igbo year — is a day of new birth and cosmic renewal. It falls outside the 13-month structure and is the most spiritually charged day of the Igbo year."
            )
            .padding(.horizontal, 16)
        }
    }
}

// MARK: - Info Overview Card

private struct InfoOverviewCard: View {
    let emoji:   String
    let title:   String
    let content: String

    @Environment(\.igboColors) private var colors

    var body: some View {
        VStack(alignment: .leading, spacing: 10) {
            HStack(spacing: 10) {
                Text(emoji).font(.system(size: 24))
                Text(title)
                    .font(.system(size: 17, weight: .bold))
                    .foregroundColor(colors.primary)
            }
            Text(content)
                .font(.system(size: 13))
                .foregroundColor(colors.onSurfaceMuted)
                .lineSpacing(5)
        }
        .padding(18)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(
            RoundedRectangle(cornerRadius: 16)
                .fill(colors.surface)
                .overlay(
                    RoundedRectangle(cornerRadius: 16)
                        .strokeBorder(colors.outline.opacity(0.5), lineWidth: 1)
                )
        )
    }
}

// MARK: - Izu Card

private struct IzuCard: View {
    let izu:   IzuInfo
    let color: Color

    @Environment(\.igboColors) private var colors
    @State private var expanded = false

    var body: some View {
        VStack(spacing: 0) {
            HStack(spacing: 12) {
                RoundedRectangle(cornerRadius: 10)
                    .fill(color.opacity(0.15))
                    .frame(width: 44, height: 44)
                    .overlay(Text(izu.icon).font(.system(size: 22)))

                VStack(alignment: .leading, spacing: 2) {
                    Text(izu.name)
                        .font(.system(size: 16, weight: .bold))
                        .foregroundColor(color)
                    Text(izu.tagline)
                        .font(.system(size: 12))
                        .foregroundColor(colors.onSurfaceMuted)
                }

                Spacer()

                Image(systemName: expanded ? "chevron.up" : "chevron.down")
                    .font(.system(size: 10))
                    .foregroundColor(colors.onSurfaceMuted)
            }
            .padding(14)

            if expanded {
                Text(izu.description)
                    .font(.system(size: 13))
                    .foregroundColor(colors.onSurface)
                    .lineSpacing(5)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding(.horizontal, 14)
                    .padding(.vertical, 12)
                    .background(color.opacity(0.04))
            }
        }
        .background(
            RoundedRectangle(cornerRadius: 14)
                .fill(colors.surfaceCard)
                .overlay(
                    RoundedRectangle(cornerRadius: 14)
                        .strokeBorder(color.opacity(0.25), lineWidth: 1)
                )
        )
        .clipShape(RoundedRectangle(cornerRadius: 14))
        .onTapGesture { withAnimation(.easeInOut(duration: 0.2)) { expanded.toggle() } }
    }
}

// MARK: - Onwa Card

private struct OnwaCard: View {
    let onwa:  OnwaInfo
    let index: Int

    @Environment(\.igboColors) private var colors
    @State private var expanded = false

    private var accent: Color { izuColors[index % izuColors.count] }

    var body: some View {
        VStack(spacing: 0) {
            HStack(spacing: 12) {
                RoundedRectangle(cornerRadius: 10)
                    .fill(accent.opacity(0.15))
                    .frame(width: 44, height: 44)
                    .overlay(
                        Text("\(index + 1)")
                            .font(.system(size: 20, weight: .black))
                            .foregroundColor(accent)
                    )

                VStack(alignment: .leading, spacing: 2) {
                    Text(onwa.name)
                        .font(.system(size: 15, weight: .bold))
                        .foregroundColor(accent)
                    Text(onwa.season)
                        .font(.system(size: 11))
                        .foregroundColor(colors.onSurfaceMuted)
                }

                Spacer()

                Image(systemName: expanded ? "chevron.up" : "chevron.down")
                    .font(.system(size: 10))
                    .foregroundColor(colors.onSurfaceMuted)
            }
            .padding(14)

            if expanded {
                VStack(alignment: .leading, spacing: 10) {
                    Text(onwa.description)
                        .font(.system(size: 13))
                        .foregroundColor(colors.onSurface)
                        .lineSpacing(5)

                    Text("ACTIVITIES")
                        .font(.system(size: 10, weight: .bold))
                        .foregroundColor(accent)
                        .kerning(1.0)

                    VStack(alignment: .leading, spacing: 4) {
                        ForEach(onwa.activities, id: \.self) { activity in
                            HStack(spacing: 8) {
                                Circle().fill(accent).frame(width: 5, height: 5)
                                Text(activity)
                                    .font(.system(size: 13))
                                    .foregroundColor(colors.onSurface)
                            }
                        }
                    }
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.horizontal, 14)
                .padding(.vertical, 12)
                .background(accent.opacity(0.04))
            }
        }
        .background(
            RoundedRectangle(cornerRadius: 14)
                .fill(colors.surfaceCard)
                .overlay(
                    RoundedRectangle(cornerRadius: 14)
                        .strokeBorder(accent.opacity(0.25), lineWidth: 1)
                )
        )
        .clipShape(RoundedRectangle(cornerRadius: 14))
        .onTapGesture { withAnimation(.easeInOut(duration: 0.2)) { expanded.toggle() } }
    }
}

// MARK: - Did You Know Card

struct DidYouKnowCard: View {
    let text: String

    @Environment(\.igboColors) private var colors

    var body: some View {
        VStack(alignment: .leading, spacing: 6) {
            Text("💡 DID YOU KNOW?")
                .font(.system(size: 10, weight: .bold))
                .foregroundColor(colors.primary)
                .kerning(1.0)
            Text(text)
                .font(.system(size: 13))
                .foregroundColor(colors.onSurface)
                .lineSpacing(5)
        }
        .padding(16)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(
            RoundedRectangle(cornerRadius: 14)
                .fill(colors.primaryContainer)
                .overlay(
                    RoundedRectangle(cornerRadius: 14)
                        .strokeBorder(colors.primary.opacity(0.3), lineWidth: 1)
                )
        )
    }
}

// MARK: - InfoTab Hashable

extension InfoTab: Hashable {}
