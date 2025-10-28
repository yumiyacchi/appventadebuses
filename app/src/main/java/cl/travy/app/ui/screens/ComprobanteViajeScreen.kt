package cl.travy.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.travy.app.model.data.InfoPasajero
import cl.travy.app.ui.components.CardViajePorPasajero
import cl.travy.app.ui.components.ResumenVenta
import cl.travy.app.ui.layout.LayoutPantallaBase
import cl.travy.app.viewmodel.MetodoPago
import cl.travy.app.ui.theme.TravyAppTheme
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ComprobanteViajeScreen(
    // 1. AÑADIMOS LOS PARÁMETROS NECESARIOS
    pasajerosPorAsiento: Map<Int, InfoPasajero>,
    precioTotal: Double,
    metodoPago: MetodoPago,
    onVolverAlInicio: () -> Unit
) {

    LayoutPantallaBase(
        titulo = "Comprobante de Venta",
        onNavegarAtras = null, // Correcto, no se puede volver.
        contenidoPrincipal = { modifier ->
            Column(
                // Ajustamos el padding y el Arrangement
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp), // Padding vertical se maneja con Spacers
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(16.dp))

                // 2. INSERTAMOS EL COMPONENTE ResumenVenta
                ResumenVenta(
                    precioTotal = precioTotal,
                    metodoPago = metodoPago,
                    cantidadAsientos = pasajerosPorAsiento.size
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Detalle de Pasajeros",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.Start) // Alineado a la izquierda
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 3. INSERTAMOS EL LazyVerticalGrid CON CardViajePorPasajero
                LazyVerticalGrid(
                    columns = GridCells.Fixed(1), // Una card por fila
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    // Modifier.weight(1f) hace que el Grid ocupe todo el espacio disponible,
                    // empujando el botón "Finalizar" hacia abajo.
                    modifier = Modifier.weight(1f)
                ) {
                    // La clave (key) ayuda a Compose a optimizar el rendimiento de la lista
                    items(pasajerosPorAsiento.entries.toList(), key = { it.key }) { (numeroAsiento, pasajero) ->
                        CardViajePorPasajero(
                            numeroAsiento = numeroAsiento,
                            pasajero = pasajero
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 4. BOTÓN PARA VOLVER AL INICIO
                Button(
                    onClick = onVolverAlInicio,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text("Volver al Inicio", style = MaterialTheme.typography.titleMedium)
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    )
}

@Preview(
    showBackground = true,
    showSystemUi = true, // Muestra la barra de estado y de navegación
    name = "Comprobante de Compra"
)
@Composable
private fun ComprobanteViajeScreenPreview() {
    // 1. --- CREACIÓN DE DATOS DE EJEMPLO ---

    // Creamos una lista de pasajeros de ejemplo
    val pasajerosDePrueba = mapOf(
        21 to InfoPasajero(
            numeroAsiento = 21,
            nombre = "Ana",
            apellido = "González",
            email = "ana.gonzalez@email.com",
            telefono = "987654321",
            genero = "Mujer"
        ),
        22 to InfoPasajero(
            numeroAsiento = 22,
            nombre = "Carlos",
            apellido = "Fuentes",
            email = "carlos.f@email.com",
            telefono = "912345678",
            genero = "Hombre"
        ),
        25 to InfoPasajero(
            numeroAsiento = 25,
            nombre = "Elena",
            apellido = "Soto",
            email = "elena.soto@email.com",
            telefono = "955554444",
            genero = "No especificado"
        )
    )

    // Creamos un método de pago de ejemplo
    val metodoDePrueba = MetodoPago(
        id = "TC-123",
        nombreMetodoPago = "Tarjeta de Crédito",
        icono = androidx.compose.material.icons.Icons.Default.CreditCard // Necesitas un ícono de placeholder
    )

    // 2. --- LLAMADA AL COMPOSABLE CON LOS DATOS CREADOS ---
    TravyAppTheme { // Usa el tema de tu aplicación para una previsualización precisa
        ComprobanteViajeScreen(
            pasajerosPorAsiento = pasajerosDePrueba,
            precioTotal = 47500.0,
            metodoPago = metodoDePrueba,
            onVolverAlInicio = { } // En la preview, la acción del botón no hace nada
        )
    }
}
