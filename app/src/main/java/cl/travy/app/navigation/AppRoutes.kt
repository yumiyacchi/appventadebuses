package cl.travy.app.navigation

sealed class AppRoutes(val route: String) {
    object Login : AppRoutes("login_screen")
    object Home : AppRoutes("home_screen")
    object Settings: AppRoutes("settings_screen")
    object BuscarViaje : AppRoutes("buscar_viaje_screen")
    object SeleccionarViaje : AppRoutes("seleccionar_viajes_screen/{origen}/{destino}/{fecha}") {
        fun createRoute(origen: String, destino: String, fecha: Long) = "seleccionar_viajes_screen/$origen/$destino/$fecha"
    }
    object SeleccionarAsiento : AppRoutes("seleccionar_asientos_screen") {
        fun createRoute(idViaje: Int) = "seleccionar_asientos_screen/$idViaje"
    }
    object PagarViaje : AppRoutes("pagar_viaje_screen")
    object ComprobanteViaje : AppRoutes("comprobante_viaje_screen")
}