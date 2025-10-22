package cl.travy.app.viewmodel



import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


data class MetodoPago(
    val id: String,
    val nombreMetodoPago: String,
    val icono: ImageVector
)

data class PagoUiState(
    val metodosDisponibles: List<MetodoPago> = listOf(
        MetodoPago(id = "tarjeta", nombreMetodoPago = "Tarjeta de Crédito o Débito)", icono = Icons.Default.CreditCard),
        MetodoPago(id = "qr", nombreMetodoPago = "QR", icono = Icons.Default.QrCode),

        ),
    val metodoSeleccionadoId: String? = null)

class PagoViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PagoUiState())
    val uiState = _uiState.asStateFlow()

    fun seleccionarMetodo(metodoId: String) {
        _uiState.update { currentState ->
            currentState.copy(metodoSeleccionadoId = metodoId)
        }
    }
}