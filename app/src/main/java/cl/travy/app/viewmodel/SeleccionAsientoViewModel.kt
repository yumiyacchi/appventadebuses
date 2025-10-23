package cl.travy.app.viewmodel


import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.travy.app.model.data.DetalleViajeState
import cl.travy.app.model.data.SeleccionAsientoUiState
import cl.travy.app.model.data.asiento.TipoAsiento
import cl.travy.app.model.data.Viaje
import cl.travy.app.model.data.InfoPasajero
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SeleccionAsientoViewModel (savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _uiState = MutableStateFlow(SeleccionAsientoUiState())
    val uiState = _uiState.asStateFlow()

    private val viajeId: Int = savedStateHandle.get<Int>("idViaje") ?: -1

    init {
        Log.d("AsientoViewModel", "ViewModel iniciado para idViaje: $viajeId")
        if (viajeId != -1) {
            cargarDetallesDelViaje(viajeId)
        } else {
            Log.e("AsientoViewModel", "Error: idViaje no fue proporcionado")
            _uiState.update { it.copy(estadoViaje = DetalleViajeState.Error("ID de viaje no encontrado.")) }
        }
    }

    private fun cargarDetallesDelViaje(id: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(estadoViaje = DetalleViajeState.Cargando) }
            Log.d("AsientoViewModel", "Iniciando carga para IdViaje: $id")

            try {
                val viajeEncontrado = withContext(Dispatchers.IO) {
                    delay(1200)
                    Viaje(
                        id = id,
                        origen = "Santiago",
                        destino = "La Serena",
                        fecha = "29 de Octubre, 2025",
                        horaSalida = "22:00",
                        horaLlegada = "06:30",
                        transportista = "Pullman Bus",
                        tipoAsiento = TipoAsiento.SALONCAMA,
                        precio = 12000.00,
                        totalAsientos = 40,
                        asientosOcupados = listOf(3, 4, 10, 11, 12, 25, 30, 38, 39, 40)
                    )
                }
                Log.d("AsientoViewModel", "Carga exitosa para viajeId: $id")
                _uiState.update { it.copy(estadoViaje = DetalleViajeState.Exitoso(viajeEncontrado)) }
            } catch (e: Exception) {
                Log.e("AsientoViewModel", "Error al cargar detalles del viaje: ${e.message}")
                _uiState.update { it.copy(estadoViaje = DetalleViajeState.Error("No se pudo cargar el detalle del viaje. ")) }

            }
        }
    }

    fun onAsientoClick(asientoId: Int) {
        val estadoActualViaje = _uiState.value.estadoViaje

        if (estadoActualViaje !is DetalleViajeState.Exitoso) return
        if (asientoId in estadoActualViaje.viaje.asientosOcupados) {
            Log.w(
                "AsientoViewModel",
                "Intento de seleccionar asiento ocupado: $asientoId. Acción ignorada."
            )
            return
        }

        _uiState.update { currentState ->
            if (currentState.asientosSeleccionados.contains(asientoId)) {
                Log.d("AsientoViewModel", "Deseleccionando asiento: $asientoId")
                currentState.copy(
                    asientosSeleccionados = currentState.asientosSeleccionados - asientoId,
                    pasajerosPorAsiento = currentState.pasajerosPorAsiento - asientoId
                )
            } else {
                Log.d("AsientoViewModel", "Seleccionando asiento: $asientoId. Abriendo diálogo.")
                currentState.copy(
                    asientosSeleccionados = currentState.asientosSeleccionados + asientoId,
                    asientoEnEdicion = asientoId
                )
            }
        }
    }

    fun guardarDatosPasajero(info: InfoPasajero) {
        _uiState.update { currentState ->
            val asientoId = currentState.asientoEnEdicion
            if (asientoId == null || asientoId != info.numeroAsiento) {
                Log.w("AsientoViewModel", "guardarDatosPasajero llamado en un estado inconsistente.")
                return@update currentState
            }

            Log.d("AsientoViewModel", "Guardando datos para asiento: $asientoId. Pasajero: ${info.nombre}")

            currentState.copy(
                pasajerosPorAsiento = currentState.pasajerosPorAsiento + (asientoId to info),
                asientoEnEdicion = null
            )
        }
    }

    fun cancelarEdicionPasajero() {
        _uiState.update { currentState ->
            val asientoId = currentState.asientoEnEdicion
            if (asientoId == null) {
                return@update currentState
            }

            Log.d("AsientoViewModel", "Cancelando edición para asiento: $asientoId. Deseleccionando asiento.")

            currentState.copy(
                asientoEnEdicion = null,
                asientosSeleccionados = currentState.asientosSeleccionados - asientoId
            )
        }
    }



}

