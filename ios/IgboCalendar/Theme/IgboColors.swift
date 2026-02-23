import SwiftUI

// MARK: - IgboColors

struct IgboColors {
    let background:       Color
    let surface:          Color
    let surfaceVariant:   Color
    let surfaceCard:      Color
    let primary:          Color
    let primaryVariant:   Color
    let primaryContainer: Color
    let onPrimary:        Color
    let secondary:        Color
    let onBackground:     Color
    let onSurface:        Color
    let onSurfaceMuted:   Color
    let divider:          Color
    let outline:          Color
    let error:            Color
    let success:          Color
    let navBar:           Color
    let appTheme:         AppTheme

    // MARK: - Amber / Gold Theme
    static let amber = IgboColors(
        background:       Color(hex: 0x0C0800),
        surface:          Color(hex: 0x181000),
        surfaceVariant:   Color(hex: 0x241A00),
        surfaceCard:      Color(hex: 0x1E1500),
        primary:          Color(hex: 0xD4900A),
        primaryVariant:   Color(hex: 0xB07808),
        primaryContainer: Color(hex: 0x3D2800),
        onPrimary:        Color(hex: 0x0C0800),
        secondary:        Color(hex: 0xF0B429),
        onBackground:     Color(hex: 0xFFF8E7),
        onSurface:        Color(hex: 0xF5E6C8),
        onSurfaceMuted:   Color(hex: 0x9E8555),
        divider:          Color(hex: 0x3D2F00),
        outline:          Color(hex: 0x5C4400),
        error:            Color(hex: 0xCF6679),
        success:          Color(hex: 0x4CAF50),
        navBar:           Color(hex: 0x100C00),
        appTheme:         .amber
    )

    // MARK: - Forest Green Theme
    static let green = IgboColors(
        background:       Color(hex: 0x050F05),
        surface:          Color(hex: 0x081508),
        surfaceVariant:   Color(hex: 0x0D2410),
        surfaceCard:      Color(hex: 0x0A1E0C),
        primary:          Color(hex: 0x00C853),
        primaryVariant:   Color(hex: 0x00A040),
        primaryContainer: Color(hex: 0x003015),
        onPrimary:        Color(hex: 0x050F05),
        secondary:        Color(hex: 0x69F0AE),
        onBackground:     Color(hex: 0xE8F5E9),
        onSurface:        Color(hex: 0xD4EDDA),
        onSurfaceMuted:   Color(hex: 0x5A9468),
        divider:          Color(hex: 0x1B4020),
        outline:          Color(hex: 0x254D2C),
        error:            Color(hex: 0xCF6679),
        success:          Color(hex: 0x00E676),
        navBar:           Color(hex: 0x040C04),
        appTheme:         .green
    )
}

// MARK: - Market Day Colors (fixed across all themes)

extension Color {
    static let eke:   Color = Color(hex: 0xD4900A)   // Amber gold
    static let orie:  Color = Color(hex: 0x1976D2)   // Royal blue
    static let afo:   Color = Color(hex: 0x2E7D32)   // Deep green
    static let nkwo:  Color = Color(hex: 0x7B1FA2)   // Purple

    static let ekeLightBg:   Color = Color(hex: 0xFFF3CD)
    static let orieLightBg:  Color = Color(hex: 0xE3F2FD)
    static let afoLightBg:   Color = Color(hex: 0xE8F5E9)
    static let nkwoLightBg:  Color = Color(hex: 0xF3E5F5)

    static func dayColor(_ index: Int) -> Color {
        switch index {
        case 0: return .eke
        case 1: return .orie
        case 2: return .afo
        case 3: return .nkwo
        default: return .gray
        }
    }

    static func dayColorLight(_ index: Int) -> Color {
        switch index {
        case 0: return .ekeLightBg
        case 1: return .orieLightBg
        case 2: return .afoLightBg
        case 3: return .nkwoLightBg
        default: return Color.gray.opacity(0.2)
        }
    }
}

// MARK: - Izu (Week) Accent Colors

let izuColors: [Color] = [
    Color(hex: 0xD4900A),  // Izu Mbụ  – amber
    Color(hex: 0x1976D2),  // Izu Abụọ – blue
    Color(hex: 0x2E7D32),  // Izu Atọ  – green
    Color(hex: 0x7B1FA2),  // Izu Anọ  – purple
    Color(hex: 0xE53935),  // Izu Ise  – red
    Color(hex: 0x00838F),  // Izu Isii – teal
    Color(hex: 0xF57C00)   // Izu Asaa – deep orange
]

// MARK: - Color Hex Initializer

extension Color {
    init(hex: UInt32, opacity: Double = 1.0) {
        let r = Double((hex >> 16) & 0xFF) / 255.0
        let g = Double((hex >>  8) & 0xFF) / 255.0
        let b = Double( hex        & 0xFF) / 255.0
        self.init(.sRGB, red: r, green: g, blue: b, opacity: opacity)
    }
}

// MARK: - Theme Environment Key

private struct IgboColorsKey: EnvironmentKey {
    static let defaultValue: IgboColors = .amber
}

extension EnvironmentValues {
    var igboColors: IgboColors {
        get { self[IgboColorsKey.self] }
        set { self[IgboColorsKey.self] = newValue }
    }
}

extension View {
    func igboTheme(_ colors: IgboColors) -> some View {
        environment(\.igboColors, colors)
    }
}
