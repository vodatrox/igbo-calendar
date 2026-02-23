import SwiftUI

// MARK: - IgboDateCard

struct IgboDateCard: View {
    let selectedDate: Date
    let igboDate:     IgboDate

    @Environment(\.igboColors) private var colors

    private var accent: Color { Color.dayColor(igboDate.dayIndex) }

    private var gregorianLabel: String {
        let formatter = DateFormatter()
        formatter.dateFormat = "EEEE, MMMM d, yyyy"
        return formatter.string(from: selectedDate)
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            // Gregorian date header
            HStack {
                VStack(alignment: .leading, spacing: 2) {
                    Text("Gregorian Date")
                        .font(.system(size: 11, weight: .medium))
                        .foregroundColor(colors.onSurfaceMuted)
                    Text(gregorianLabel)
                        .font(.system(size: 14, weight: .semibold))
                        .foregroundColor(colors.onSurface)
                }
                Spacer()
                Circle()
                    .fill(accent)
                    .frame(width: 10, height: 10)
            }

            Spacer().frame(height: 16)

            if igboDate.isUboshiOmumu {
                UboshiOmumuContent(igboDate: igboDate, accent: accent)
            } else {
                NormalIgboContent(igboDate: igboDate, accent: accent)
            }
        }
        .padding(20)
        .background(
            RoundedRectangle(cornerRadius: 20)
                .fill(colors.surfaceCard)
        )
        .overlay(
            RoundedRectangle(cornerRadius: 20)
                .strokeBorder(accent.opacity(0.25), lineWidth: 1)
        )
    }
}

// MARK: - Normal Igbo Date Content

private struct NormalIgboContent: View {
    let igboDate: IgboDate
    let accent:   Color

    @Environment(\.igboColors) private var colors

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            // Market day + chips
            HStack(alignment: .top) {
                VStack(alignment: .leading, spacing: 0) {
                    Text("MARKET DAY")
                        .font(.system(size: 10, weight: .bold))
                        .foregroundColor(accent)
                        .kerning(1.5)
                    Text(igboDate.day)
                        .font(.system(size: 48, weight: .black))
                        .foregroundColor(accent)
                        .lineLimit(1)
                }

                Spacer()

                VStack(alignment: .trailing, spacing: 6) {
                    IgboDetailChip(label: "IZU",   value: igboDate.week,         accent: accent)
                    IgboDetailChip(label: "ỌNWA",  value: igboDate.month,        accent: accent)
                    IgboDetailChip(label: "ARO",   value: "\(igboDate.year)",    accent: accent)
                }
            }

            Spacer().frame(height: 14)

            // Gradient divider
            LinearGradient(
                colors: [accent.opacity(0.6), accent.opacity(0)],
                startPoint: .leading,
                endPoint: .trailing
            )
            .frame(height: 1)

            Spacer().frame(height: 12)

            // Progress row
            HStack {
                Text("Day \(igboDate.dayWithinMonth + 1) of 28")
                    .font(.system(size: 12))
                    .foregroundColor(colors.onSurfaceMuted)
                Spacer()
                Text("\(igboDate.week) · \(igboDate.month)")
                    .font(.system(size: 12, weight: .medium))
                    .foregroundColor(colors.onSurfaceMuted)
                    .multilineTextAlignment(.trailing)
            }

            Spacer().frame(height: 8)

            // Month progress bar
            MonthProgressBar(
                progress: Float(igboDate.dayWithinMonth + 1) / 28.0,
                accent: accent
            )
        }
    }
}

// MARK: - Ubọchị Ọmụmụ Content

private struct UboshiOmumuContent: View {
    let igboDate: IgboDate
    let accent:   Color

    @Environment(\.igboColors) private var colors

    var body: some View {
        VStack(spacing: 6) {
            Text("✨").font(.system(size: 32))

            Text("Ubọchị Ọmụmụ")
                .font(.system(size: 24, weight: .black))
                .foregroundColor(accent)
                .multilineTextAlignment(.center)

            Text("Sacred New Year Day")
                .font(.system(size: 13))
                .foregroundColor(colors.onSurfaceMuted)

            Spacer().frame(height: 4)

            Text("Igbo Year \(igboDate.year)")
                .font(.system(size: 16, weight: .bold))
                .foregroundColor(colors.onSurface)

            Text("Market Day: \(igboDate.day)")
                .font(.system(size: 13, weight: .semibold))
                .foregroundColor(accent)
        }
        .frame(maxWidth: .infinity)
    }
}

// MARK: - IgboDetailChip

private struct IgboDetailChip: View {
    let label:  String
    let value:  String
    let accent: Color

    @Environment(\.igboColors) private var colors

    var body: some View {
        VStack(alignment: .trailing, spacing: 1) {
            Text(label)
                .font(.system(size: 8, weight: .bold))
                .foregroundColor(accent)
                .kerning(1.0)
            Text(value)
                .font(.system(size: 12, weight: .semibold))
                .foregroundColor(colors.onSurface)
                .multilineTextAlignment(.trailing)
        }
        .padding(.horizontal, 10)
        .padding(.vertical, 4)
        .background(
            RoundedRectangle(cornerRadius: 8)
                .fill(accent.opacity(0.1))
        )
    }
}

// MARK: - MonthProgressBar

struct MonthProgressBar: View {
    let progress: Float
    let accent:   Color

    var body: some View {
        GeometryReader { geo in
            ZStack(alignment: .leading) {
                RoundedRectangle(cornerRadius: 3)
                    .fill(accent.opacity(0.15))

                RoundedRectangle(cornerRadius: 3)
                    .fill(
                        LinearGradient(
                            colors: [accent, accent.opacity(0.7)],
                            startPoint: .leading,
                            endPoint: .trailing
                        )
                    )
                    .frame(width: geo.size.width * CGFloat(progress))
            }
        }
        .frame(height: 5)
    }
}
