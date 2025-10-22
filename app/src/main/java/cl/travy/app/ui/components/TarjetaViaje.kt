package cl.travy.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.travy.app.model.data.asiento.TipoAsiento
import cl.travy.app.model.data.Viaje
import cl.travy.app.ui.theme.TravyAppTheme

@Composable
fun TarjetaViaje(
    viaje: Viaje,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = viaje.transportista,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$${"%,.0f".format(viaje.precio)} CLP",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Info de Horarios
                InfoViaje(titulo = "Sale", valor = viaje.horaSalida)
                InfoViaje(titulo = "Llega", valor = viaje.horaLlegada)

                InfoViaje(titulo = "Asiento", valor = viaje.tipoAsiento.name)
            }

            Text(
                text = "${viaje.asientosDisponibles} asientos disponibles",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun InfoViaje(titulo: String, valor: String, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier
    ) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}



@Preview(showBackground = true, widthDp = 360)
@Composable
private fun TarjetaViajePreview() {
    TravyAppTheme {
        val viajeDeEjemplo = Viaje(
            id = 1,
            origen = "Santiago",
            destino = "Concepción",
            fecha = "25 de octubre, 2025",
            horaSalida = "09:30",
            horaLlegada = "15:30",
            transportista = "TUR-BUS",
            tipoAsiento = TipoAsiento.SEMICAMA,
            precio = 22000.0,
            totalAsientos = 44,
            asientosOcupados = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            TarjetaViaje(viaje = viajeDeEjemplo, onClick = {})
        }
    }
}
