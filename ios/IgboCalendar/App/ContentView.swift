import SwiftUI

struct ContentView: View {
    @EnvironmentObject private var viewModel: CalendarViewModel

    /// Computed directly from the view model so theme changes cause a full re-render.
    private var colors: IgboColors { viewModel.colors }

    var body: some View {
        TabView(selection: $viewModel.selectedTab) {
            CalendarScreen()
                .tabItem { Label("Calendar", systemImage: "calendar") }
                .tag(AppTab.calendar)

            MarketScreen()
                .tabItem { Label("Market", systemImage: "storefront") }
                .tag(AppTab.market)

            InfoScreen()
                .tabItem { Label("Info", systemImage: "info.circle") }
                .tag(AppTab.info)

            EventsScreen()
                .tabItem { Label("Events", systemImage: "music.note.list") }
                .tag(AppTab.events)
        }
        .accentColor(colors.primary)
        // Inject the live colors into the environment for all child views
        .igboTheme(colors)
    }
}
