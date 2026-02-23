import SwiftUI

@main
struct IgboCalendarApp: App {
    @StateObject private var viewModel = CalendarViewModel()

    var body: some Scene {
        WindowGroup {
            ContentView()
                .environmentObject(viewModel)
                .preferredColorScheme(.dark)
        }
    }
}
