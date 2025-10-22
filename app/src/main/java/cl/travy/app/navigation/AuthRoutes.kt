package cl.travy.app.navigation
sealed class AuthRoutes(val route: String) {
    object Login: AuthRoutes("login_screen")
    // object Register: AuthRoutes("register_screen")
    // object ForgotPassword: AuthRoutes("forgot_password_screen")
}
