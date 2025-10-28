package cl.travy.app.viewmodel


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import cl.travy.app.model.data.InfoPasajero
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


data class MetodoPago(
    val id: String,
    val nombreMetodoPago: String,
    val icono: ImageVector
)



data class PagoUiState(

    val pasajerosPorAsiento: Map<Int, InfoPasajero> = emptyMap(),
    val precioTotal: Double = 0.0,
    val metodosDisponibles: List<MetodoPago> = listOf(
        MetodoPago(id = "tarjeta", nombreMetodoPago = "Tarjeta de Crédito o Débito)", icono = Icons.Default.CreditCard),
        MetodoPago(id = "qr", nombreMetodoPago = "QR", icono = Icons.Default.QrCode),
        ),
    val metodoSeleccionadoId: String? = null,
    val pagoRealizadoConExito: Boolean = false)

class PagoViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PagoUiState())
    val uiState = _uiState.asStateFlow()


    fun prepararCompra(pasajeros: Map<Int, InfoPasajero>, precio: Double) {
        _uiState.update { currentState ->
            currentState.copy(
                pasajerosPorAsiento = pasajeros,
                precioTotal = precio,
                pagoRealizadoConExito = false,
                metodoSeleccionadoId = null
            )
        }
    }


    fun seleccionarMetodo(metodoId: String) {
        _uiState.update { currentState ->
            currentState.copy(metodoSeleccionadoId = metodoId)
        }
    }

    fun ejecutarPago() {
        if (_uiState.value.metodoSeleccionadoId != null) {
            _uiState.update { it.copy(pagoRealizadoConExito = true) }
        }
    }

    fun finalizarYResetear(){
        _uiState.value = PagoUiState()
    }
}