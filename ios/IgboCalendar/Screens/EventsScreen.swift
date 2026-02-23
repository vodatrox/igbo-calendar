import SwiftUI

// MARK: - Events Screen

struct EventsScreen: View {
    @EnvironmentObject private var viewModel: CalendarViewModel
    @Environment(\.igboColors) private var colors

    private var featured: Festival? { FestivalData.festivals.first { $0.featured } }
    private var others:   [Festival] { FestivalData.festivals.filter { !$0.featured } }

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 0) {
                // Header
                VStack(alignment: .leading, spacing: 4) {
                    Text("Events & Festivals")
                        .font(.system(size: 26, weight: .black))
                        .foregroundColor(colors.primary)
                    Text("Ọ mụnụ ya n'omenala — Born of tradition")
                        .font(.system(size: 12))
                        .foregroundColor(colors.onSurfaceMuted)
                        .italic()
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

                // Featured festival
                if let f = featured {
                    FeaturedFestivalCard(festival: f)
                        .padding(.horizontal, 16)
                    Spacer().frame(height: 20)
                }

                // Festival list
                Text("IGBO FESTIVALS")
                    .font(.system(size: 11, weight: .bold))
                    .foregroundColor(colors.onSurfaceMuted)
                    .kerning(1.5)
                    .padding(.horizontal, 20)

                Spacer().frame(height: 12)

                ForEach(others) { festival in
                    FestivalCard(festival: festival)
                        .padding(.horizontal, 16)
                        .padding(.vertical, 6)
                }

                Spacer().frame(height: 20)
                CulturalNoteCard().padding(.horizontal, 16)
                Spacer().frame(height: 24)
            }
        }
        .background(colors.background.ignoresSafeArea())
    }
}

// MARK: - Featured Festival Card

private struct FeaturedFestivalCard: View {
    let festival: Festival

    @Environment(\.igboColors) private var colors

    private var accent: Color { Color.dayColor(marketDayIndex(festival.marketDay)) }

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            HStack {
                FestivalMarketDayBadge(marketDay: festival.marketDay, accent: accent)
                Spacer()
                FeaturedBadge(accent: accent)
            }

            Spacer().frame(height: 16)

            Text(festival.name)
                .font(.system(size: 22, weight: .black))
                .foregroundColor(colors.onBackground)

            Text(festival.subtitle)
                .font(.system(size: 13, weight: .semibold))
                .foregroundColor(accent)

            Spacer().frame(height: 12)

            Text(String(festival.description.prefix(200)) + "…")
                .font(.system(size: 13))
                .foregroundColor(colors.onSurfaceMuted)
                .lineSpacing(5)

            Spacer().frame(height: 12)

            HStack(spacing: 12) {
                HStack(spacing: 4) {
                    Text("📍").font(.system(size: 12))
                    Text(festival.location)
                        .font(.system(size: 12))
                        .foregroundColor(colors.onSurfaceMuted)
                }
                HStack(spacing: 4) {
                    Text("🌙").font(.system(size: 12))
                    Text(festival.igboMonth)
                        .font(.system(size: 12))
                        .foregroundColor(colors.onSurfaceMuted)
                }
            }
        }
        .padding(20)
        .background(
            ZStack {
                LinearGradient(
                    colors: [accent.opacity(0.3), colors.surface, colors.surfaceVariant],
                    startPoint: .topLeading,
                    endPoint: .bottomTrailing
                )
                RoundedRectangle(cornerRadius: 20)
                    .strokeBorder(accent.opacity(0.4), lineWidth: 1.5)
            }
        )
        .clipShape(RoundedRectangle(cornerRadius: 20))
    }
}

// MARK: - Festival Card

private struct FestivalCard: View {
    let festival: Festival

    @Environment(\.igboColors) private var colors
    @State private var expanded = false

    private var accent: Color { Color.dayColor(marketDayIndex(festival.marketDay)) }

    var body: some View {
        VStack(spacing: 0) {
            HStack(alignment: .top, spacing: 14) {
                // Market day badge box
                RoundedRectangle(cornerRadius: 12)
                    .fill(accent.opacity(0.15))
                    .frame(width: 52, height: 52)
                    .overlay(
                        VStack(spacing: 1) {
                            Text(String(festival.marketDay.prefix(3)))
                                .font(.system(size: 11, weight: .black))
                                .foregroundColor(accent)
                            Text("Day")
                                .font(.system(size: 9))
                                .foregroundColor(accent.opacity(0.7))
                        }
                    )

                VStack(alignment: .leading, spacing: 2) {
                    Text(festival.name)
                        .font(.system(size: 15, weight: .bold))
                        .foregroundColor(colors.onSurface)
                    Text(festival.subtitle)
                        .font(.system(size: 12, weight: .medium))
                        .foregroundColor(accent)

                    Spacer().frame(height: 4)

                    Text("📍 \(festival.location)")
                        .font(.system(size: 11))
                        .foregroundColor(colors.onSurfaceMuted)
                }

                Spacer()

                Image(systemName: expanded ? "chevron.up" : "chevron.down")
                    .font(.system(size: 10))
                    .foregroundColor(colors.onSurfaceMuted)
                    .padding(.top, 4)
            }
            .padding(16)

            if expanded {
                VStack(alignment: .leading, spacing: 10) {
                    Text(festival.description)
                        .font(.system(size: 13))
                        .foregroundColor(colors.onSurface)
                        .lineSpacing(5)

                    HStack(spacing: 12) {
                        InfoPill(text: "🌙 \(festival.igboMonth)", color: accent)
                        InfoPill(text: "🥁 \(festival.marketDay)", color: accent)
                    }
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.horizontal, 16)
                .padding(.vertical, 12)
                .background(accent.opacity(0.04))
            }
        }
        .background(
            RoundedRectangle(cornerRadius: 16)
                .fill(colors.surfaceCard)
                .overlay(
                    RoundedRectangle(cornerRadius: 16)
                        .strokeBorder(accent.opacity(0.2), lineWidth: 1)
                )
        )
        .clipShape(RoundedRectangle(cornerRadius: 16))
        .onTapGesture { withAnimation(.easeInOut(duration: 0.2)) { expanded.toggle() } }
    }
}

// MARK: - Badges

private struct FestivalMarketDayBadge: View {
    let marketDay: String
    let accent:    Color

    var body: some View {
        Text("MARKET DAY: \(marketDay)")
            .font(.system(size: 10, weight: .bold))
            .foregroundColor(accent)
            .kerning(0.8)
            .padding(.horizontal, 10)
            .padding(.vertical, 5)
            .background(
                RoundedRectangle(cornerRadius: 8)
                    .fill(accent.opacity(0.15))
            )
    }
}

private struct FeaturedBadge: View {
    let accent: Color

    var body: some View {
        Text("✦ FEATURED")
            .font(.system(size: 10, weight: .bold))
            .foregroundColor(Color.black.opacity(0.8))
            .kerning(0.8)
            .padding(.horizontal, 10)
            .padding(.vertical, 5)
            .background(
                RoundedRectangle(cornerRadius: 8)
                    .fill(
                        LinearGradient(
                            colors: [accent, accent.opacity(0.7)],
                            startPoint: .leading,
                            endPoint: .trailing
                        )
                    )
            )
    }
}

// MARK: - Cultural Note Card

private struct CulturalNoteCard: View {
    @Environment(\.igboColors) private var colors

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("🥁").font(.system(size: 22))

            Text("Did You Know?")
                .font(.system(size: 15, weight: .bold))
                .foregroundColor(colors.primary)

            Text("Market days (Eke, Orie, Afọ, Nkwọ) dictate the rhythm of Igbo life — determining when to farm, trade, hold major gatherings, and celebrate festivals. Each community's primary market day is a core part of its identity and spiritual heritage.")
                .font(.system(size: 13))
                .foregroundColor(colors.onSurfaceMuted)
                .lineSpacing(6)
        }
        .padding(18)
        .frame(maxWidth: .infinity, alignment: .leading)
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

// MARK: - Helpers

private func marketDayIndex(_ marketDay: String) -> Int {
    switch marketDay.uppercased() {
    case "EKE":          return 0
    case "ORIE":         return 1
    case "AFỌ", "AFO":  return 2
    case "NKWỌ","NKWO": return 3
    default:             return 0
    }
}
