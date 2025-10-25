package cl.travy.app.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.travy.app.model.data.DetalleViajeState
import cl.travy.app.model.data.InfoPasajero
import cl.travy.app.model.data.SeleccionAsientoUiState
import cl.travy.app.model.data.Viaje
import cl.travy.app.model.data.asiento.Asiento
import cl.travy.app.model.data.asiento.EstadoAsiento
import cl.travy.app.model.data.asiento.PosicionAsiento
import cl.travy.app.model.data.asiento.TipoAsiento
import cl.travy.app.model.data.asiento.PrecioAsientoConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SeleccionAsientoViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(SeleccionAsientoUiState())


    val uiState = _uiState.asStateFlow()

    init {
        val idViaje: Int? = savedStateHandle["idViaje"]
        cargarInformacionViaje(idViaje ?: -1)
    }

    private fun cargarInformacionViaje(idViaje: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(estadoViaje = DetalleViajeState.Cargando) }


            kotlinx.coroutines.delay(1500)

            if (idViaje > 0) {
                val viajeSimulado = Viaje(
                    id = idViaje,
                    origen = "Santiago",
                    destino = "Valparaíso",
                    fecha = "28 de Octubre, 2025",
                    horaSalida = "09:00",
                    horaLlegada = "11:15",
                    transportista = "Pullman Bus",
                    tipoAsiento = TipoAsiento.SEMICAMA,
                    precio = 7500.0,
                    totalAsientos = 40,
                    asientosOcupados = listOf(3, 4, 10, 11, 22, 23, 24, 35)
                )
                _uiState.update {
                    it.copy(estadoViaje = DetalleViajeState.Exitoso(viajeSimulado))
                }
            } else {
                // 3. Carga con Error
                _uiState.update {
                    it.copy(estadoViaje = DetalleViajeState.Error("ID de viaje inválido."))
                }
            }
        }
    }


    fun onAsientoClick(asiento: Asiento) {
        if (asiento.estado == cl.travy.app.model.data.asiento.EstadoAsiento.OCUPADO) {
            return
        }

        _uiState.update { currentState ->
            val asientosSeleccionados = currentState.asientosSeleccionados.toMutableSet()
            val pasajeros = currentState.pasajerosPorAsiento.toMutableMap()

            if (asiento.numero in asientosSeleccionados) {

                asientosSeleccionados.remove(asiento.numero)

                pasajeros.remove(asiento.numero)
                val asientoEnEdicion =
                    if (currentState.asientoEnEdicion == asiento.numero) null else currentState.asientoEnEdicion

                currentState.copy(
                    asientosSeleccionados = asientosSeleccionados,
                    pasajerosPorAsiento = pasajeros,
                    asientoEnEdicion = asientoEnEdicion
                )
            } else {
                asientosSeleccionados.add(asiento.numero)
                currentState.copy(
                    asientosSeleccionados = asientosSeleccionados,
                    asientoEnEdicion = asiento.numero
                )
            }
        }
        recalcularPrecioTotal()
    }


    fun onDatosPasajeroChange(pasajeroActualizado: InfoPasajero) {
        _uiState.update { currentState ->
            val pasajeros = currentState.pasajerosPorAsiento.toMutableMap()
            pasajeros[pasajeroActualizado.numeroAsiento] = pasajeroActualizado
            currentState.copy(pasajerosPorAsiento = pasajeros)
        }
    }

    fun guardarDatosPasajero(pasajero: InfoPasajero) {

        _uiState.update { it.copy(asientoEnEdicion = null) }
    }

    fun cancelarEdicionPasajero() {
        _uiState.update { currentState ->
            val asientoId = currentState.asientoEnEdicion
            val pasajero = asientoId?.let { currentState.pasajerosPorAsiento[it] }


            if (asientoId != null && pasajero?.estaCompleto != true) {
                val seleccionados = currentState.asientosSeleccionados.toMutableSet()
                val pasajeros = currentState.pasajerosPorAsiento.toMutableMap()
                seleccionados.remove(asientoId)
                pasajeros.remove(asientoId)
                recalcularPrecioTotal()
                currentState.copy(
                    asientoEnEdicion = null,
                    asientosSeleccionados = seleccionados,
                    pasajerosPorAsiento = pasajeros
                )
            } else {

                currentState.copy(asientoEnEdicion = null)
            }
        }
    }


    private fun recalcularPrecioTotal() {
        _uiState.update { currentState ->

            val viaje = (currentState.estadoViaje as? DetalleViajeState.Exitoso)?.viaje


            if (viaje == null || currentState.asientosSeleccionados.isEmpty()) {
                return@update currentState.copy(precioTotal = 0.0)
            }


            val precioPorAsiento = PrecioAsientoConfig.obtenerPrecio(viaje.tipoAsiento)


            val precioFinal = currentState.asientosSeleccionados.size * precioPorAsiento


            currentState.copy(precioTotal = precioFinal)
        }
    }

}