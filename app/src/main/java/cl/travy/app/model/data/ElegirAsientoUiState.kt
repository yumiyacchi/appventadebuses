package cl.travy.app.model.data

import cl.travy.app.model.data.asiento.Asiento
import cl.travy.app.model.data.asiento.EstadoAsiento
import kotlin.collections.filter

data class ElegirAsientoUiState(
    val isLoading : Boolean = false,
    val listaDeAsientos : List<Asiento> = emptyList(),
) {
    val asientosSeleccionados: List<Asiento>
        get() = listaDeAsientos.filter {it.estado == EstadoAsiento.SELECCIONADO }

    val precioTotal: Double
        get() = asientosSeleccionados.sumOf {it.precio}
}