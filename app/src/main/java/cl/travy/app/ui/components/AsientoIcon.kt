package cl.travy.app.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.travy.app.model.data.asiento.Asiento
import cl.travy.app.model.data.asiento.EstadoAsiento
import cl.travy.app.ui.theme.*
import java.util.Locale
import kotlin.text.lowercase
import androidx.compose.ui.tooling.preview.Preview
import cl.travy.app.model.data.asiento.PosicionAsiento
import cl.travy.app.model.data.asiento.TipoAsiento

@Composable
fun AsientoIcon(
    asiento: Asiento,
    tieneDatos: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (colorPrincipal, colorBorde) = when (asiento.estado) {
        EstadoAsiento.OCUPADO -> LightRed to DarkRed
        EstadoAsiento.SELECCIONADO -> LightBlue to DarkBlue
        EstadoAsiento.DISPONIBLE -> LightGreen to DarkGreen
    }
    val esClickable = asiento.estado != EstadoAsiento.OCUPADO

    Card(
        modifier = modifier
            .height(70.dp)
            .width(70.dp)
            .border(
                width = 6.dp,
                color = colorBorde,
                shape = RoundedCornerShape(corner = CornerSize(8.dp))
            )
            .clickable(enabled = esClickable, onClick = onClick),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        colors = CardDefaults.cardColors(containerColor = colorPrincipal)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.padding(2.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = asiento.numero.toString(),
                    color = DarkGrey,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = asiento.tipo.name.lowercase(Locale.getDefault())
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                    color = DarkGrey,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
            }


            if (tieneDatos) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Datos de pasajero completos",
                    tint = DarkGreen,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(3.dp)
                        .size(16.dp)
                )
            }
        }
    }
}

@Preview(
    name = "Asiento Disponible",
    backgroundColor = 0xFFFFFFFF,
    showBackground = true
)
@Composable
fun AsientoIconDisponiblePreview() {
    val asientoDePrueba = Asiento(
        numero = 12,
        tipo = TipoAsiento.SEMICAMA,
        estado = EstadoAsiento.DISPONIBLE,
        posicion = PosicionAsiento.VENTANA,
        precio = 5000.00
    )


    TravyAppTheme {
        AsientoIcon(
            asiento = asientoDePrueba,
            tieneDatos = false,
            onClick = { }
        )
    }
}