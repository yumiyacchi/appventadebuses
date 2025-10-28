package cl.travy.app.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.travy.app.ui.layout.LayoutPantallaBase
import cl.travy.app.viewmodel.MetodoPago
import cl.travy.app.viewmodel.PagoUiState
import cl.travy.app.ui.theme.LightBlue
import cl.travy.app.ui.theme.DarkBlue


@Composable
fun PagoScreen(
    state: PagoUiState,
    onMetodoSeleccionado: (MetodoPago) -> Unit,
    onConfirmarPago: () -> Unit,
    onNavigateBack: () -> Unit
) {
    LayoutPantallaBase(
        titulo = "Pago",
        onNavegarAtras = onNavigateBack,
        contenidoPrincipal = { modifier ->
            ContenidoPantallaPago(
                modifier = modifier,
                state = state,
                onMetodoSeleccionado = onMetodoSeleccionado,
                onConfirmarPago = onConfirmarPago
            )
        }
    )
}

@Composable
private fun ContenidoPantallaPago(
    modifier: Modifier = Modifier,
    state: PagoUiState,
    onMetodoSeleccionado: (MetodoPago) -> Unit,
    onConfirmarPago: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Total a Pagar",
            style = MaterialTheme.typography.titleLarge,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "$${"%,.0f".format(state.precioTotal)}",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = DarkBlue
        )

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            state.metodosDisponibles.forEach { metodo ->
                MetodoDePagoFila(
                    texto = metodo.nombreMetodoPago,
                    icono = metodo.icono,
                    seleccionado = (state.metodoSeleccionado == metodo),
                    onClick = { onMetodoSeleccionado(metodo) }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        BotonPagar(
            modifier = Modifier.padding(bottom = 32.dp),
            onClick = onConfirmarPago,
            enabled = state.metodoSeleccionado != null
        )
    }
}


@Composable
private fun MetodoDePagoFila(
    texto: String,
    icono: ImageVector,
    seleccionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (seleccionado) MaterialTheme.colorScheme.primary else Color.LightGray
    val borderWidth = if (seleccionado) 2.dp else 1.dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = borderWidth,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icono, contentDescription = texto, tint = if (seleccionado) MaterialTheme.colorScheme.primary else Color.Gray)
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = texto,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            RadioButton(
                selected = seleccionado,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}

@Composable
private fun BotonPagar(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(containerColor = LightBlue),
        modifier = modifier
            .width(280.dp)
            .height(60.dp)
    ) {
        Text(
            text = "Pagar",
            color = Color.White,
            fontSize = 18.sp
        )
    }
}


@Preview(showBackground = true, name = "PagoScreen Preview")
@Composable
private fun PagoScreenPreview() {
    val metodos = listOf(
        MetodoPago("tarjeta", "Tarjeta de Crédito", Icons.Default.CreditCard),
        MetodoPago("qr", "Código QR", Icons.Default.QrCode)
    )
    MaterialTheme {
        PagoScreen(
            state = PagoUiState(
                precioTotal = 45500.0,
                metodosDisponibles = metodos,
                metodoSeleccionado = metodos[0] // Pre-seleccionar el primer método para el preview
            ),
            onMetodoSeleccionado = {},
            onConfirmarPago = {},
            onNavigateBack = {}
        )
    }
}

@Preview(showBackground = true, name = "PagoScreen sin selección")
@Composable
private fun PagoScreenSinSeleccionPreview() {
    val metodos = listOf(
        MetodoPago("tarjeta", "Tarjeta de Crédito", Icons.Default.CreditCard),
        MetodoPago("qr", "Código QR", Icons.Default.QrCode)
    )
    MaterialTheme {
        PagoScreen(
            state = PagoUiState(
                precioTotal = 12300.0,
                metodosDisponibles = metodos,
                metodoSeleccionado = null // Ningún método seleccionado
            ),
            onMetodoSeleccionado = {},
            onConfirmarPago = {},
            onNavigateBack = {}
        )
    }
}
