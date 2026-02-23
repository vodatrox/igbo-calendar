import SwiftUI

// MARK: - Market Screen

struct MarketScreen: View {
    @EnvironmentObject private var viewModel: CalendarViewModel
    @Environment(\.igboColors) private var colors

    private var todayIgbo: IgboDate {
        IgboCalendarEngine.toIgboDate(.today)
    }

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 0) {
                // Header
                VStack(alignment: .leading, spacing: 4) {
                    Text("Market Days")
                        .font(.system(size: 26, weight: .black))
                        .foregroundColor(colors.primary)
                    Text("Ụbọchị Ahịa — The Sacred 4-Day Cycle")
                        .font(.system(size: 13))
                        .foregroundColor(colors.onSurfaceMuted)
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.horizontal, 20)
                .padding(.vertical, 20)
                .background(
                    LinearGradient(
                        colors: [colors.surface, colors.background],
                        startPoint: .top,
                        endPoint: .bottom
                    )
                )

                // Today's market day
                TodayMarketDayCard(dayIndex: todayIgbo.dayIndex, dayName: todayIgbo.day)
                    .padding(.horizontal, 16)

                Spacer().frame(height: 20)

                Text("THE FOUR MARKET DAYS")
                    .font(.system(size: 11, weight: .bold))
                    .foregroundColor(colors.onSurfaceMuted)
                    .kerning(1.5)
                    .padding(.horizontal, 20)

                Spacer().frame(height: 12)

                ForEach(MarketDayData.days) { dayInfo in
                    MarketDayCard(
                        dayInfo: dayInfo,
                        isToday: !todayIgbo.isUboshiOmumu && dayInfo.index == todayIgbo.dayIndex
                    )
                    .padding(.horizontal, 16)
                    .padding(.vertical, 6)
                }

                Spacer().frame(height: 20)
                CosmicCycleInfo().padding(.horizontal, 16)
                Spacer().frame(height: 24)
            }
        }
        .background(colors.background.ignoresSafeArea())
    }
}

// MARK: - Today Market Day Card

private struct TodayMarketDayCard: View {
    let dayIndex: Int
    let dayName:  String

    @Environment(\.igboColors) private var colors

    private var dayColor: Color { Color.dayColor(dayIndex) }
    private var marketData: MarketDayInfo? { MarketDayData.days.first { $0.index == dayIndex } }

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            HStack(alignment: .top) {
                VStack(alignment: .leading, spacing: 2) {
                    Text("TODAY'S MARKET DAY")
                        .font(.system(size: 10, weight: .bold))
                        .foregroundColor(dayColor)
                        .kerning(1.5)
                    Text(dayName)
                        .font(.system(size: 52, weight: .black))
                        .foregroundColor(dayColor)
                        .lineLimit(1)
                }
                Spacer()
                if let info = marketData {
                    Text(info.icon).font(.system(size: 48))
                }
            }

            if let info = marketData {
                Text(info.tagline)
                    .font(.system(size: 14, weight: .semibold))
                    .foregroundColor(colors.onSurface)
                Spacer().frame(height: 4)
                Text(String(info.significance.prefix(120)) + "…")
                    .font(.system(size: 12))
                    .foregroundColor(colors.onSurfaceMuted)
                    .lineSpacing(4)
            }
        }
        .padding(20)
        .background(
            ZStack {
                LinearGradient(
                    colors: [dayColor.opacity(0.25), dayColor.opacity(0.05)],
                    startPoint: .topLeading,
                    endPoint: .bottomTrailing
                )
                RoundedRectangle(cornerRadius: 20)
                    .strokeBorder(dayColor.opacity(0.4), lineWidth: 1.5)
            }
        )
        .clipShape(RoundedRectangle(cornerRadius: 20))
    }
}

// MARK: - Market Day Card

private struct MarketDayCard: View {
    let dayInfo: MarketDayInfo
    let isToday: Bool

    @Environment(\.igboColors) private var colors
    @State private var expanded = false

    var body: some View {
        VStack(spacing: 0) {
            // Header row
            HStack(spacing: 14) {
                RoundedRectangle(cornerRadius: 12)
                    .fill(dayInfo.color.opacity(0.15))
                    .frame(width: 52, height: 52)
                    .overlay(
                        Text(dayInfo.icon).font(.system(size: 28))
                    )

                VStack(alignment: .leading, spacing: 2) {
                    HStack(spacing: 8) {
                        Text(dayInfo.name)
                            .font(.system(size: 20, weight: .black))
                            .foregroundColor(dayInfo.color)
                        if isToday {
                            TodayBadge()
                        }
                    }
                    Text(dayInfo.tagline)
                        .font(.system(size: 12, weight: .medium))
                        .foregroundColor(colors.onSurfaceMuted)

                    Spacer().frame(height: 4)

                    HStack(spacing: 10) {
                        InfoPill(text: "📍 \(dayInfo.direction)", color: dayInfo.color)
                        InfoPill(text: "🙏 \(dayInfo.deity.components(separatedBy: " ").first ?? "")", color: dayInfo.color)
                    }
                }

                Spacer()
            }
            .padding(16)

            // Expanded content
            if expanded {
                VStack(alignment: .leading, spacing: 12) {
                    Text(dayInfo.significance)
                        .font(.system(size: 13))
                        .foregroundColor(colors.onSurface)
                        .lineSpacing(5)

                    Text("TRADITIONAL ACTIVITIES")
                        .font(.system(size: 10, weight: .bold))
                        .foregroundColor(dayInfo.color)
                        .kerning(1.0)

                    VStack(alignment: .leading, spacing: 6) {
                        ForEach(dayInfo.activities, id: \.self) { activity in
                            HStack(spacing: 8) {
                                Circle()
                                    .fill(dayInfo.color)
                                    .frame(width: 5, height: 5)
                                Text(activity)
                                    .font(.system(size: 13))
                                    .foregroundColor(colors.onSurface)
                            }
                        }
                    }
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.horizontal, 16)
                .padding(.vertical, 12)
                .background(dayInfo.color.opacity(0.04))
            }
        }
        .background(
            RoundedRectangle(cornerRadius: 16)
                .fill(colors.surfaceCard)
                .overlay(
                    RoundedRectangle(cornerRadius: 16)
                        .strokeBorder(
                            isToday ? dayInfo.color : colors.divider,
                            lineWidth: isToday ? 1.5 : 1
                        )
                )
        )
        .clipShape(RoundedRectangle(cornerRadius: 16))
        .onTapGesture { withAnimation(.easeInOut(duration: 0.2)) { expanded.toggle() } }
    }
}

// MARK: - Today Badge

private struct TodayBadge: View {
    @Environment(\.igboColors) private var colors

    var body: some View {
        Text("TODAY")
            .font(.system(size: 9, weight: .bold))
            .foregroundColor(colors.onPrimary)
            .kerning(0.5)
            .padding(.horizontal, 8)
            .padding(.vertical, 2)
            .background(Capsule().fill(colors.primary))
    }
}

// MARK: - Info Pill

struct InfoPill: View {
    let text:  String
    let color: Color

    var body: some View {
        Text(text)
            .font(.system(size: 10, weight: .semibold))
            .foregroundColor(color)
            .padding(.horizontal, 8)
            .padding(.vertical, 3)
            .background(Capsule().fill(color.opacity(0.12)))
    }
}

// MARK: - Cosmic Cycle Info

private struct CosmicCycleInfo: View {
    @Environment(\.igboColors) private var colors

    var body: some View {
        VStack(alignment: .leading, spacing: 10) {
            HStack(spacing: 8) {
                Text("🌍").font(.system(size: 20))
                Text("Market Cosmology")
                    .font(.system(size: 16, weight: .bold))
                    .foregroundColor(colors.primary)
            }
            Text("The four Igbo market days — Eke, Orie, Afọ, and Nkwọ — are not merely commercial days. They represent the four cardinal directions, four divine forces, and the fundamental rhythm of Igbo cosmological time. Every community has its primary market day, which defines its identity and spiritual orientation within the greater Igbo world.")
                .font(.system(size: 13))
                .foregroundColor(colors.onSurfaceMuted)
                .lineSpacing(6)
        }
        .padding(20)
        .background(
            RoundedRectangle(cornerRadius: 16)
                .fill(colors.surface)
                .overlay(
                    RoundedRectangle(cornerRadius: 16)
                        .strokeBorder(colors.divider, lineWidth: 1)
                )
        )
    }
}
