// Archivo: viewmodel/SeleccionViajeViewModel.kt (TRANSFORMADO)
package cl.travy.app.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.travy.app.model.data.ListaViajesState
import cl.travy.app.model.data.SeleccionViajeUiState
import cl.travy.app.model.data.asiento.TipoAsiento
import cl.travy.app.model.data.Viaje
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SeleccionViajeViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {


    private val _uiState = MutableStateFlow(SeleccionViajeUiState())
    val uiState = _uiState.asStateFlow()


    private val origen: String = savedStateHandle.get<String>("origen") ?: "Error"
    private val destino: String = savedStateHandle.get<String>("destino") ?: "Error"
    private val fecha: Long = savedStateHandle.get<Long>("fecha") ?: 0L
    private val fechaFormateada: String = SimpleDateFormat("dd 'de' MMMM, yyyy", Locale("es", "ES")).format(Date(fecha))

    init {
        _uiState.update {
            it.copy(
                origen = this.origen,
                destino = this.destino,
                fechaFormateada = this.fechaFormateada

            )
        }
        cargarViajes()
    }

    private fun cargarViajes() {
        viewModelScope.launch {
            Log.d("ViewModel", "Iniciando carga de viajes para ${origen} -> ${destino}")

            try {
                val viajesDesdeAPI = withContext(Dispatchers.IO) {
                    delay(1500) // Simula un retardo de red de 1.5 segundos
                    Log.d("ViewModel", "Datos recibidos de la API (simulado). Creando modelos de Viaje...")

                    listOf(
                        Viaje(
                            id = 1, origen = origen, destino = destino, fecha = fechaFormateada,
                            horaSalida = "08:00", horaLlegada = "14:00",
                            transportista = "Pullman Bus",
                            tipoAsiento = TipoAsiento.SALONCAMA,
                            precio = 25000.0, totalAsientos = 40, asientosOcupados = listOf(1, 5, 8, 12, 22)
                        ),
                        Viaje(
                            id = 2, origen = origen, destino = destino, fecha = fechaFormateada,
                            horaSalida = "09:30", horaLlegada = "15:30",
                            transportista = "TUR-BUS",
                            tipoAsiento = TipoAsiento.SEMICAMA,
                            precio = 22000.0, totalAsientos = 44, asientosOcupados = listOf(2, 6, 10, 30, 31, 32, 33)
                        ),
                        Viaje(
                            id = 3, origen = origen, destino = destino, fecha = fechaFormateada,
                            horaSalida = "10:00", horaLlegada = "16:00",
                            transportista = "EME Bus",
                            tipoAsiento = TipoAsiento.STANDARD,
                            precio = 30000.0, totalAsientos = 30, asientosOcupados = listOf(3, 7, 11)
                        )
                    )
                }

                Log.d("ViewModel", "Carga exitosa. Actualizando estado a Exitoso con ${viajesDesdeAPI.size} viajes.")
                _uiState.update {
                    it.copy(estadoLista = ListaViajesState.Exitoso(viajesDesdeAPI))
                }

            } catch (e: Exception) {
                Log.e("ViewModel", "Error al cargar viajes: ${e.message}")
                _uiState.update {
                    it.copy(estadoLista = ListaViajesState.Error("No se pudieron cargar los viajes. Revisa tu conexión."))
                }
            }
        }
    }
}
