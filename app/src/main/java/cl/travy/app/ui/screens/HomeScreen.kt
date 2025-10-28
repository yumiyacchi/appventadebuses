package cl.travy.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.travy.app.model.data.HomeUiState
import cl.travy.app.model.data.MenuItem
import cl.travy.app.ui.layout.LayoutPantallaBase
import cl.travy.app.ui.theme.LightBlue
import cl.travy.app.ui.theme.TravyAppTheme
import cl.travy.app.ui.theme.White



@Composable
fun HomeScreen(
    uiState: HomeUiState
) {

    LayoutPantallaBase(

        titulo = "Home",
        onNavegarAtras = null,
        contenidoPrincipal = { modifier ->
            MenuPrincipal(
                modifier = modifier,
                items = uiState.menuItems
            )
        }
    )
}


@Composable
fun MenuPrincipal(
    modifier: Modifier = Modifier,
    items: List<MenuItem>
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items) { menuItem ->
            BotonMenu(item = menuItem)
        }
    }
}

@Composable
fun BotonMenu(
    item: MenuItem,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = item.onClick,
        modifier = modifier.aspectRatio(1f),
        enabled = item.habilitado,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = LightBlue,
            contentColor = White
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = item.icono,
                contentDescription = item.texto,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.texto,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                maxLines = 2
            )
        }
    }
}


@Preview(showBackground = true, widthDp = 360)
@Composable
fun HomeScreenPreview() {
    val menuItemsDePrueba = listOf(
        MenuItem(texto = "Buscar Viaje", icono = Icons.Default.Search, onClick = {}, habilitado = true),
        MenuItem(texto = "Opciones", icono = Icons.Default.Settings, onClick = {}, habilitado = true)
    )
    val uiStateDePrueba = HomeUiState(menuItems = menuItemsDePrueba)

    TravyAppTheme {
        HomeScreen(uiState = uiStateDePrueba)
    }
}
