package cl.travy.app.model.state

import cl.travy.app.model.data.asiento.TipoAsiento
import cl.travy.app.model.data.asiento.PosicionAsiento
import cl.travy.app.model.data.asiento.EstadoAsiento
import cl.travy.app.model.data.InfoPasajero
import cl.travy.app.model.data.Viaje
import cl.travy.app.model.data.asiento.Asiento

data class GestionAsientosUiState(

    val viajeDetalle: Viaje? = null,
    val asientos: List<Asiento> = emptyList(),
    val pasajeros: List<InfoPasajero> = emptyList(),
    val pasajeroEnEdicion: InfoPasajero? = null,
    val mostrarDialogoPasajero: Boolean = false,
    val isLoading: Boolean = true,
    val ultimoPasajeroGuardado: InfoPasajero? = null
) {
    val asientosSeleccionados: List<Asiento>
        get() = asientos.filter { it.estado == EstadoAsiento.SELECCIONADO }

    val precioTotal: Double
        get() = asientosSeleccionados.sumOf {it.precio}

    val puedeContinuar: Boolean
        get() = asientosSeleccionados.isNotEmpty() && (asientosSeleccionados.size == pasajeros.size)
}