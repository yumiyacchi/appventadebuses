package cl.travy.app.model.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Carpenter
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings


data class HomeUiState(
    val menuItems: List<MenuItem> = emptyList()

)

fun getHomeMenuItems(
    onBuscarViajeClick: () -> Unit,
    onOpcionesClick: () -> Unit,

): List<MenuItem> {
    return listOf(
        MenuItem(
            texto = "Buscar Viaje",
            icono = Icons.Default.Search,
            onClick = onBuscarViajeClick,
            habilitado = true
        ),
        MenuItem(
            texto = "Proximamente",
            icono = Icons.Default.Carpenter,
            onClick = {},
            habilitado = false
        ),
        MenuItem(
            texto = "Proximamente",
            icono = Icons.Default.Carpenter,
            onClick = {},
            habilitado = false
        ),
        MenuItem(
            texto = "Opciones",
            icono = Icons.Default.Settings,
            onClick = onOpcionesClick,
            habilitado = true
        ),

    )
}