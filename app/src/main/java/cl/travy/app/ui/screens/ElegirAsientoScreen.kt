package cl.travy.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.travy.app.model.data.DetalleViajeState
import cl.travy.app.model.data.InfoPasajero
import cl.travy.app.model.data.SeleccionAsientoUiState
import cl.travy.app.model.data.Viaje
import cl.travy.app.model.data.asiento.Asiento
import cl.travy.app.model.data.asiento.EstadoAsiento
import cl.travy.app.model.data.asiento.PosicionAsiento
import cl.travy.app.model.data.asiento.TipoAsiento
import cl.travy.app.ui.components.AsientoIcon
import cl.travy.app.ui.components.DialogoDatosPasajero
import cl.travy.app.ui.layout.LayoutPantallaBase
import cl.travy.app.ui.theme.DarkBlue
import cl.travy.app.ui.theme.LightBlue
import cl.travy.app.ui.theme.TravyAppTheme

@Composable
fun ElegirAsientoScreen(
    state: SeleccionAsientoUiState,
    onAsientoClick: (Asiento) -> Unit,
    onContinuarClick: () -> Unit,
    onNavigateBack: () -> Unit,
    onGuardarDatosPasajero: (InfoPasajero) -> Unit,
    onCancelarDialogo: () -> Unit,
    onDatosPasajeroChange: (InfoPasajero) -> Unit
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

    val pasajeroEnEdicion = state.asientoEnEdicion?.let { asientoId ->
        state.pasajerosPorAsiento[asientoId] ?: InfoPasajero(
            numeroAsiento = asientoId,
            nombre = "",
            apellido = "",
            genero = "",
            email = "",
            telefono = "")
    }

    DialogoDatosPasajero(
        mostrarDialog = state.asientoEnEdicion != null,
        pasajero = pasajeroEnEdicion,
        onDatosChange = onDatosPasajeroChange,
        onConfirmar = {
            pasajeroEnEdicion?.let { onGuardarDatosPasajero(it) }
        },
        onDismiss = onCancelarDialogo
    )
}

@Composable
private fun ContenidoPrincipal(
    viaje: Viaje,
    state: SeleccionAsientoUiState,
    onAsientoClick: (Asiento) -> Unit,
    onContinuarClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${viaje.origen} - ${viaje.destino}",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(text = viaje.fecha, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(24.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(2.dp, DarkBlue)
        ) {
            PlanoDeAsientos(
                viaje = viaje,
                pasajerosPorAsiento = state.pasajerosPorAsiento,
                asientosSeleccionados = state.asientosSeleccionados,
                onAsientoClick = onAsientoClick
            )
        }

        Spacer(Modifier.weight(1f))

        if (state.asientosSeleccionados.isNotEmpty()) {
            val todosLosDatosCompletos = state.asientosSeleccionados.all { asientoId ->
                state.pasajerosPorAsiento[asientoId]?.estaCompleto == true
            }

            val asientosSeleccionadosCompletos = remember(state.asientosSeleccionados, viaje) {
                state.asientosSeleccionados.map { numero ->
                    Asiento(
                        numero = numero,
                        tipo = viaje.tipoAsiento,
                        posicion = if (numero % 4 == 1 || numero % 4 == 0) PosicionAsiento.VENTANA else PosicionAsiento.PASILLO, // Lógica de ejemplo
                        estado = EstadoAsiento.SELECCIONADO,
                        precio = viaje.precio
                    )
                }.sortedBy { it.numero }
            }

            ResumenDeCompra(
                asientosSeleccionados = asientosSeleccionadosCompletos,
                precioTotal = state.precioTotal,
                onAsientoClickParaQuitar = onAsientoClick
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onContinuarClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = todosLosDatosCompletos
            ) {
                Text("Continuar compra")
            }
        }
    }
}

@Composable
private fun PlanoDeAsientos(
    viaje: Viaje,
    asientosSeleccionados: Set<Int>,
    pasajerosPorAsiento: Map<Int, InfoPasajero>,
    onAsientoClick: (Asiento) -> Unit
) {
    val listaDeAsientos = remember(viaje, asientosSeleccionados) {
        (1..viaje.totalAsientos).map { numeroAsiento ->
            val estado = when {
                numeroAsiento in viaje.asientosOcupados -> EstadoAsiento.OCUPADO
                numeroAsiento in asientosSeleccionados -> EstadoAsiento.SELECCIONADO
                else -> EstadoAsiento.DISPONIBLE
            }
            Asiento(
                numero = numeroAsiento,
                tipo = viaje.tipoAsiento,
                posicion = if (numeroAsiento % 4 == 1 || numeroAsiento % 4 == 0) PosicionAsiento.VENTANA else PosicionAsiento.PASILLO,
                estado = estado,
                precio = viaje.precio
            )
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = Modifier.padding(16.dp)
            .height(700.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(listaDeAsientos, key = { it.numero }) { asiento ->
            val tieneDatos = pasajerosPorAsiento[asiento.numero]?.estaCompleto == true
            AsientoIcon(
                asiento = asiento,
                tieneDatos = tieneDatos,
                onClick = { onAsientoClick(asiento) }
            )
        }
    }
}

@Composable
private fun ResumenDeCompra(
    asientosSeleccionados: List<Asiento>,
    precioTotal: Double,
    onAsientoClickParaQuitar: (Asiento) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(vertical = 8.dp)) {
        Text(
            text = "Asientos Elegidos",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LazyRow(
                modifier = Modifier.weight(1f, fill = false),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(asientosSeleccionados, key = { it.numero }) { asiento ->
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(LightBlue, RoundedCornerShape(8.dp))
                            .border(1.dp, DarkBlue, RoundedCornerShape(8.dp))
                            .clickable { onAsientoClickParaQuitar(asiento) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = asiento.numero.toString(),
                            color = DarkBlue,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "$${"%.0f".format(precioTotal)}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}


@Preview(name = "1. Estado de Carga", showBackground = true)
@Composable
private fun ElegirAsientoScreenLoadingPreview() {
    val loadingState = SeleccionAsientoUiState(
        estadoViaje = DetalleViajeState.Cargando
    )
    TravyAppTheme {
        ElegirAsientoScreen(
            state = loadingState, onAsientoClick = {}, onContinuarClick = {},
            onNavigateBack = {}, onGuardarDatosPasajero = {}, onCancelarDialogo = {},
            onDatosPasajeroChange = {}
        )
    }
}

@Preview(name = "2. Contenido Exitoso", showBackground = true)
@Composable
private fun ElegirAsientoScreenSuccessPreview() {
    val successState = SeleccionAsientoUiState(
        estadoViaje = DetalleViajeState.Exitoso(
            Viaje(
                id = 1, origen = "Santiago", destino = "Valparaíso", fecha = "25 de Diciembre, 2025",
                horaSalida = "08:30", horaLlegada = "11:45", transportista = "Pullman Bus",
                tipoAsiento = TipoAsiento.SEMICAMA, precio = 7000.0, totalAsientos = 40,
                asientosOcupados = listOf(3, 4, 10, 11, 22)
            )
        ),
        asientosSeleccionados = setOf(1, 2, 15),
        pasajerosPorAsiento = mapOf(
            1 to InfoPasajero(1, "Ana", "Rojas", "F", "ana@mail.com", "987654321")
        )
    )
    TravyAppTheme {
        ElegirAsientoScreen(
            state = successState, onAsientoClick = {}, onContinuarClick = {},
            onNavigateBack = {}, onGuardarDatosPasajero = {}, onCancelarDialogo = {},
            onDatosPasajeroChange = {}
        )
    }
}

@Preview(name = "3. Diálogo Abierto", showBackground = true)
@Composable
private fun ElegirAsientoScreenDialogPreview() {
    val dialogState = SeleccionAsientoUiState(
        estadoViaje = DetalleViajeState.Exitoso(
            Viaje(
                id = 1, origen = "Santiago", destino = "Valparaíso", fecha = "25 de Diciembre, 2025",
                horaSalida = "12:30", horaLlegada = "18:00", transportista = "Buses Lentos",
                tipoAsiento = TipoAsiento.SALONCAMA, precio = 7000.0, totalAsientos = 40,
                asientosOcupados = listOf(3, 4, 10, 11, 22)
            )
        ),
        asientosSeleccionados = setOf(1, 2),
        pasajerosPorAsiento = mapOf(
            1 to InfoPasajero(1, "Ana", "Rojas", "F", "ana@mail.com", "987654321")
        ),
        asientoEnEdicion = 2,
    )
    TravyAppTheme {
        ElegirAsientoScreen(
            state = dialogState, onAsientoClick = {}, onContinuarClick = {},
            onNavigateBack = {}, onGuardarDatosPasajero = {}, onCancelarDialogo = {},
            onDatosPasajeroChange = {}
        )
    }
}

@Preview(name = "4. Estado de Error", showBackground = true)
@Composable
private fun ElegirAsientoScreenErrorPreview() {
    val errorState = SeleccionAsientoUiState(
        estadoViaje = DetalleViajeState.Error("No se pudo cargar la información del viaje. Inténtalo de nuevo.")
    )
    TravyAppTheme {
        ElegirAsientoScreen(
            state = errorState, onAsientoClick = {}, onContinuarClick = {},
            onNavigateBack = {}, onGuardarDatosPasajero = {}, onCancelarDialogo = {},
            onDatosPasajeroChange = {}
        )
    }
}
