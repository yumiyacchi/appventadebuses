package cl.travy.app.ui.components


import android.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.travy.app.model.data.InfoPasajero
import cl.travy.app.ui.theme.DarkBlue
import cl.travy.app.ui.theme.LightBlue
import cl.travy.app.viewmodel.MetodoPago


@Composable
fun ResumenVenta(precioTotal: Double,
                 metodoPago: MetodoPago,
                 cantidadAsientos: Int,
                 modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(contentColor = Color.Black),
        shape = MaterialTheme.shapes.large
    )
    {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Resumen de tu compra",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            HorizontalDivider()
            InfoRow(label = "Asientos Comprados: ", value = "$cantidadAsientos")
            InfoRow(label = "Metodo de Pago", value = metodoPago.nombreMetodoPago)
            HorizontalDivider()
            InfoRow(
                label = "Total Pagado: ",
                value = "$${"%,.0f".format(precioTotal)}"
            )
        }
    }
}


@Composable
private fun InfoRow(label: String, value: String, isTotal: Boolean = false) {
    val valueStyle = if (isTotal) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyLarge
    val valueWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal
    val valueColor = if (isTotal) LightBlue else DarkBlue

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Text(text = value, style = valueStyle, fontWeight = valueWeight, color = valueColor)
    }

}

@Composable
fun CardViajePorPasajero(numeroAsiento: Int,
                         pasajero: InfoPasajero,
                         modifier: Modifier = Modifier
){
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, DarkBlue)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Asiento #$numeroAsiento",
                style = MaterialTheme.typography.titleMedium,
                color = DarkBlue
            )
            Spacer(modifier = Modifier.height(2.dp))
            HorizontalDivider()
//            Spacer(modifier = modifier.height(2.dp))
            Text(
                text = "Nombre: ${pasajero.nombre} ${pasajero.apellido}",
                style = MaterialTheme.typography.bodyMedium
            )
//            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "E-Mail: ${pasajero.email}",
                style = MaterialTheme.typography.bodyMedium
            )
//            Spacer(modifier.height(2.dp))
            Text(
                text = "Telefono: ${pasajero.telefono}",
                style = MaterialTheme.typography.bodyMedium
            )
//            Spacer(modifier.height(2.dp))
            Text(
                text = "Genero: ${pasajero.genero}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }

}


@Preview(showBackground = true)
@Composable
private fun CardViajePorPasajeroPreview() {
    MaterialTheme {
        CardViajePorPasajero(
            numeroAsiento = 25,
            pasajero = InfoPasajero(
                numeroAsiento = 20,
                nombre = "Juan",
                apellido = "Perez",
                email = "juan@perez.com",
                telefono = "12345678910",
                genero = "Hombre"
                ),

            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ResumenVentaPreview() {

    val metodoDePrueba = MetodoPago(
        id = "1",
        nombreMetodoPago = "Tarjeta de Crédito",
        icono = Icons.Default.CreditCard
    )
    MaterialTheme {
        ResumenVenta(
            precioTotal = 25500.0,
            metodoPago = metodoDePrueba,
            cantidadAsientos = 3,
            modifier = Modifier.padding(16.dp)
        )
    }
}