package cl.travy.app.model.data

import cl.travy.app.model.data.asiento.Asiento

sealed class DetalleViajeState {
    object Cargando: DetalleViajeState()

    data class Exitoso(val viaje: Viaje) : DetalleViajeState()
    data class Error(val mensaje: String) : DetalleViajeState()
}

data class SeleccionAsientoUiState(

    val estadoViaje: DetalleViajeState = DetalleViajeState.Cargando,
    val asientosSeleccionados: Set<Int> = emptySet(),
    val pasajerosPorAsiento: Map<Int, InfoPasajero> = emptyMap(),
    val asientoEnEdicion: Int? = null,
    val precioTotal: Double = 0.0

)

