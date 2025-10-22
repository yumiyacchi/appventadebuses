package cl.travy.app.model.data

import cl.travy.app.model.data.Viaje

sealed class DetalleViajeState {
    object Cargando: DetalleViajeState()

    data class Exitoso(val viaje: Viaje) : DetalleViajeState()
    data class Error(val mensaje: String) : DetalleViajeState()
}

data class SeleccionAsientoUiState(

    val estadoViaje: DetalleViajeState = DetalleViajeState.Cargando,
    val asientosSeleccionados: Set<Int> = emptySet(),
    val pasajerosPorAsiento: Map<Int, InfoPasajero> = emptyMap(),
    val asientoEnEdicion: Int? = null

) {
    val precioTotal: Double
        get() {
            return if (estadoViaje is DetalleViajeState.Exitoso) {
                estadoViaje.viaje.precio * asientosSeleccionados.size
            } else {
                0.0
            }
        }
}

