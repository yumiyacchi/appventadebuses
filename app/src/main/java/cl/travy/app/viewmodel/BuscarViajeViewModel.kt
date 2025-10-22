package cl.travy.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.travy.app.model.data.BuscarViajeUiState
import cl.travy.app.navigation.AppRoutes
import cl.travy.app.viewmodel.events.BuscarViajeEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class BuscarViajeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(BuscarViajeUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = Channel<NavigationEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    fun onEvent(event: BuscarViajeEvent) {
        when (event) {
            is BuscarViajeEvent.OnOrigenChange -> {
                _uiState.update { it.copy(origen = event.origen) }
                actualizarEstadoBoton()
            }

            is BuscarViajeEvent.OnDestinoChange -> {
                _uiState.update { it.copy(destino = event.destino) }
                actualizarEstadoBoton()
            }

            is BuscarViajeEvent.OnFechaChange -> {
                _uiState.update { it.copy(fecha = event.fecha) }
                actualizarEstadoBoton()
            }

            BuscarViajeEvent.OnBuscarClick -> {
                viewModelScope.launch {
                    val currentState = _uiState.value
                    if (currentState.isButtonEnabled) {
                        _navigationEvent.send(
                            NavigationEvent.NavigateToSeleccionViaje(
                                origen = currentState.origen,
                                destino = currentState.destino,
                                fecha = currentState.fecha ?: 0L
                            )
                        )
                    }
                }
            }
        }
    }


    private fun actualizarEstadoBoton() {
        _uiState.update { currentState ->
            val isEnabled = currentState.origen.isNotBlank() &&
                    currentState.destino.isNotBlank() &&
                    currentState.fecha != null

            currentState.copy(isButtonEnabled = isEnabled)
        }
    }

    sealed class NavigationEvent {
        data class NavigateToSeleccionViaje(
            val origen: String,
            val destino: String,
            val fecha: Long
        ) : NavigationEvent()
    }
}
