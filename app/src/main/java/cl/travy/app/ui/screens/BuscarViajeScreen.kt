package cl.travy.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.travy.app.ui.layout.LayoutPantallaBase
import cl.travy.app.viewmodel.BuscarViajeViewModel
import cl.travy.app.viewmodel.events.BuscarViajeEvent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuscarViajesScreen(

    onSearchClicked: (origen: String, destino: String, fecha: Long) -> Unit,
    onNavigateBack: () -> Unit,

    viewModel: BuscarViajeViewModel = viewModel()
) {

    val state by viewModel.uiState.collectAsState()

    var mostrarDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is BuscarViajeViewModel.NavigationEvent.NavigateToSeleccionViaje -> {
                    onSearchClicked(event.origen, event.destino, event.fecha)
                }
            }
        }
    }

    LayoutPantallaBase(
        titulo = "Buscar Viaje",
        onNavegarAtras = onNavigateBack,
        contenidoPrincipal = { modifier ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                OutlinedTextField(
                    value = state.origen,
                    // Llamamos al método público del ViewModel
                    onValueChange = { viewModel.onEvent(BuscarViajeEvent.OnOrigenChange(it)) },
                    label = { Text("Origen") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                IconButton(
                    onClick = {
                        focusManager.clearFocus()
                        val origenTemporal = state.origen
                        viewModel.onEvent(BuscarViajeEvent.OnOrigenChange(state.destino))
                        viewModel.onEvent(BuscarViajeEvent.OnDestinoChange(origenTemporal))
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Icon(
                        imageVector = Icons.Default.SwapVert,
                        contentDescription = "Intercambiar Origen y Destino"
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = state.destino,
                    onValueChange = { viewModel.onEvent(BuscarViajeEvent.OnDestinoChange(it)) },
                    label = { Text("Destino") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            focusManager.clearFocus()
                            mostrarDatePicker = true
                        }
                ) {
                    OutlinedTextField(
                        value = state.fecha?.let {
                            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(it))
                        } ?: "Seleccione una fecha", // Texto por defecto mejorado
                        onValueChange = {},
                        label = { Text("Fecha del viaje") },
                        readOnly = true,
                        trailingIcon = {
                            Icon(Icons.Default.CalendarMonth, "Seleccionar fecha")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = false,
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledBorderColor = MaterialTheme.colorScheme.outline,
                            disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { viewModel.onEvent(BuscarViajeEvent.OnBuscarClick) },
                    enabled = state.isButtonEnabled, // El ViewModel ya maneja el estado de carga
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text("Buscar")
                }
            }
        }
    )

    if (mostrarDatePicker) {
        DatePickerDialog(
            onDismissRequest = { mostrarDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarDatePicker = false
                        datePickerState.selectedDateMillis?.let { fechaSeleccionadaMillis ->
                            viewModel.onEvent(BuscarViajeEvent.OnFechaChange(fechaSeleccionadaMillis))
                        }
                    }
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDatePicker = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
