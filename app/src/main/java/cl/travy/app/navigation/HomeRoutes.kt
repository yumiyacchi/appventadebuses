package cl.travy.app.navigation


sealed class HomeRoutes(val route: String) {
    object Main: HomeRoutes("main_screen")
    object Profile: HomeRoutes("profile_screen")
    object Settings: HomeRoutes("settings_screen")
}
