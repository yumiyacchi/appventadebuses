package cl.travy.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import cl.travy.app.model.data.DetalleViajeState
import cl.travy.app.model.data.InfoPasajero
import cl.travy.app.model.data.SeleccionAsientoUiState
import cl.travy.app.model.data.Viaje
import cl.travy.app.ui.layout.LayoutPantallaBase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import cl.travy.app.model.data.asiento.TipoAsiento


@Composable
fun ElegirAsientoScreen(
    state: SeleccionAsientoUiState,
    onAsientoClick: (Int) -> Unit,
    onContinuarClick: () -> Unit,
    onNavigateBack: () -> Unit,
    onGuardarDatosPasajero: (InfoPasajero) -> Unit,
    onCancelarDialogo: () -> Unit
) {

    LayoutPantallaBase(
        titulo = "Elige tus Asientos",
        onNavegarAtras = onNavigateBack,
        contenidoPrincipal = { modifier ->
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                when (val estadoViaje = state.estadoViaje) {
                    is DetalleViajeState.Cargando -> {
                        CircularProgressIndicator()
                    }
                    is DetalleViajeState.Error -> {
                        Text(
                            text = estadoViaje.mensaje,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    is DetalleViajeState.Exitoso -> {
                        ContenidoPrincipal(
                            viaje = estadoViaje.viaje,
                            state = state,
                            onAsientoClick = onAsientoClick,
                            onContinuarClick = onContinuarClick
                        )
                    }
                }
            }
        }
    )


    if (state.asientoEnEdicion != null) {
        DialogoIngresoPasajero(
            numeroAsiento = state.asientoEnEdicion,
            onGuardar = onGuardarDatosPasajero,
            onCancelar = onCancelarDialogo
        )
    }
}



@Composable
private fun ContenidoPrincipal(
    viaje: Viaje,
    state: SeleccionAsientoUiState,
    onAsientoClick: (Int) -> Unit,
    onContinuarClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "${viaje.origen} - ${viaje.destino}", style = MaterialTheme.typography.headlineSmall)
        Text(text = viaje.fecha, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(24.dp))

        PlanoDeAsientos(
            totalAsientos = viaje.totalAsientos,
            asientosOcupados = viaje.asientosOcupados.toSet(),
            asientosSeleccionados = state.asientosSeleccionados,
            onAsientoClick = onAsientoClick
        )

        Spacer(Modifier.weight(1f))

        if (state.asientosSeleccionados.isNotEmpty()) {
            val todosLosDatosCompletos = state.asientosSeleccionados.all { asientoId ->
                state.pasajerosPorAsiento[asientoId]?.estaCompleto == true
            }

            Text("Asientos seleccionados: ${state.asientosSeleccionados.size}", style = MaterialTheme.typography.bodyLarge)
            Text("Total: $${"%.0f".format(state.precioTotal)}", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onContinuarClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = todosLosDatosCompletos
            ) {
                Text("Continuar")
            }
        }
    }
}

@Composable
private fun PlanoDeAsientos(
    totalAsientos: Int,
    asientosOcupados: Set<Int>,
    asientosSeleccionados: Set<Int>,
    onAsientoClick: (Int) -> Unit
) {
    val listaDeAsientos = (1..totalAsientos).toList()

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(listaDeAsientos) { numeroAsiento ->
            val color = when {
                numeroAsiento in asientosOcupados -> Color.DarkGray
                numeroAsiento in asientosSeleccionados -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.surfaceVariant
            }
            val textColor = when {
                numeroAsiento in asientosOcupados -> Color.White
                numeroAsiento in asientosSeleccionados -> MaterialTheme.colorScheme.onPrimary
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            }

            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .background(color, RoundedCornerShape(8.dp))
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
                    .clickable { onAsientoClick(numeroAsiento) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = numeroAsiento.toString(),
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
private fun DialogoIngresoPasajero(
    numeroAsiento: Int,
    onGuardar: (InfoPasajero) -> Unit,
    onCancelar: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    val estaCompleto = nombre.isNotBlank() && apellido.isNotBlank() && email.isNotBlank() && telefono.isNotBlank() && genero.isNotBlank()

    Dialog(onDismissRequest = onCancelar) {
        Card(shape = RoundedCornerShape(16.dp)) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(text = "Datos del Pasajero - Asiento $numeroAsiento", style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(16.dp))

                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
                OutlinedTextField(value = apellido, onValueChange = { apellido = it }, label = { Text("Apellido") })
                OutlinedTextField(value = genero, onValueChange = { genero = it }, label = { Text("Género") })
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
                OutlinedTextField(value = telefono, onValueChange = { telefono = it }, label = { Text("Teléfono") })
                Spacer(Modifier.height(24.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onCancelar) {
                        Text("Cancelar")
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val info = InfoPasajero(numeroAsiento, nombre, apellido, genero, email, telefono)
                            onGuardar(info)
                        },
                        enabled = estaCompleto
                    ) {
                        Text("Guardar")
                    }
                }
            }
        }
    }
}

@Preview(name = "1. Estado de Carga", showBackground = true)
@Composable
private fun ElegirAsientoScreenLoadingPreview() {

    val loadingState = SeleccionAsientoUiState(
        estadoViaje = DetalleViajeState.Cargando
    )
    MaterialTheme {
        ElegirAsientoScreen(
            state = loadingState,
            onAsientoClick = {},
            onContinuarClick = {},
            onNavigateBack = {},
            onGuardarDatosPasajero = {},
            onCancelarDialogo = {}
        )
    }
}


@Preview(name = "2. Contenido Exitoso", showBackground = true)
@Composable
private fun ElegirAsientoScreenSuccessPreview() {
    val successState = SeleccionAsientoUiState(
        estadoViaje = DetalleViajeState.Exitoso(
            Viaje(
                id = 1,
                origen = "Santiago",
                destino = "Valparaíso",
                fecha = "25 de Diciembre, 2025",
                horaSalida = "08:30",
                horaLlegada = "11:45",
                transportista = "Pullman Bus",
                tipoAsiento = TipoAsiento.SEMICAMA,
                precio = 7000.0,
                totalAsientos = 40,
                asientosOcupados = listOf(3, 4, 10, 11, 22)
            )
        ),
        asientosSeleccionados = setOf(1, 2),
        pasajerosPorAsiento = mapOf(
            1 to InfoPasajero(1, "Ana", "Rojas", "F", "ana@mail.com", "987654321")
        ),

    )
    MaterialTheme {
        ElegirAsientoScreen(
            state = successState,
            onAsientoClick = {},
            onContinuarClick = {},
            onNavigateBack = {},
            onGuardarDatosPasajero = {},
            onCancelarDialogo = {}
        )
    }
}

@Preview(name = "3. Diálogo Abierto", showBackground = true)
@Composable
private fun ElegirAsientoScreenDialogPreview() {
    val dialogState = SeleccionAsientoUiState(
        estadoViaje = DetalleViajeState.Exitoso(
            Viaje(
                id = 1,
                origen = "Santiago",
                destino = "Valparaíso",
                fecha = "25 de Diciembre, 2025",
                horaSalida = "12:30",
                horaLlegada = "18:00",
                transportista = "Buses Lentos",
                tipoAsiento = TipoAsiento.SALONCAMA,
                precio = 7000.0,
                totalAsientos = 40,
                asientosOcupados = listOf(3, 4, 10, 11, 22)
            )
        ),
        asientosSeleccionados = setOf(1, 2),
        pasajerosPorAsiento = mapOf(
            1 to InfoPasajero(1, "Ana", "Rojas", "F", "ana.rojas@gmail.com", "987654321")
        ),

        asientoEnEdicion = 2,

    )
    MaterialTheme {
        ElegirAsientoScreen(
            state = dialogState,
            onAsientoClick = {},
            onContinuarClick = {},
            onNavigateBack = {},
            onGuardarDatosPasajero = {},
            onCancelarDialogo = {}
        )
    }
}

@Preview(name = "4. Estado de Error", showBackground = true)
@Composable
private fun ElegirAsientoScreenErrorPreview() {

    val errorState = SeleccionAsientoUiState(
        estadoViaje = DetalleViajeState.Error("No se pudo cargar la información del viaje. Inténtalo de nuevo.")
    )
    MaterialTheme {
        ElegirAsientoScreen(
            state = errorState,
            onAsientoClick = {},
            onContinuarClick = {},
            onNavigateBack = {},
            onGuardarDatosPasajero = {},
            onCancelarDialogo = {}
        )
    }
}
