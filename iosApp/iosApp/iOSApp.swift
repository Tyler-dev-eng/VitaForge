import SwiftUI
import GoogleSignIn
import navigation

class AppDelegate: NSObject, UIApplicationDelegate {
    func application(
        _ app: UIApplication,
        open url: URL, options: [UIApplication.OpenURLOptionsKey: Any] = [:]
    ) -> Bool {
        return GIDSignIn.sharedInstance.handle(url)
    }
}

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate

    var body: some Scene {
        WindowGroup {
            ContentView().onOpenURL(perform: { url in
                if !GIDSignIn.sharedInstance.handle(url) {
                    MainViewControllerKt.handlePaypalReturn(url: url.absoluteString)
                }
            })
        }
    }
}