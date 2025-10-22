package cl.travy.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cl.travy.app.model.data.ListaViajesState
import cl.travy.app.model.data.SeleccionViajeUiState
import cl.travy.app.ui.components.TarjetaViaje
import cl.travy.app.ui.layout.LayoutPantallaBase


@Composable
fun SeleccionViajeScreen(
    state: SeleccionViajeUiState,
    onViajeClick: (idViaje: Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    LayoutPantallaBase(
        titulo = "Selecciona tu Viaje",
        onNavegarAtras = onNavigateBack,
        contenidoPrincipal = { modifier ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "${state.origen} → ${state.destino}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 16.dp)
                )


                when (val estadoLista = state.estadoLista) {

                    is ListaViajesState.Cargando -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is ListaViajesState.Exitoso -> {
                        if (estadoLista.viajes.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No se encontraron viajes para esta ruta y fecha.",
                                    style = MaterialTheme.typography.bodyLarge,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(horizontal = 32.dp)
                                )
                            }
                        } else {
                            LazyColumn(
                                contentPadding = PaddingValues(bottom = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                items(estadoLista.viajes) { viaje ->
                                    TarjetaViaje(
                                        viaje = viaje,
                                        onClick = { onViajeClick(viaje.id) }
                                    )
                                }
                            }
                        }
                    }

                    is ListaViajesState.Error -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = estadoLista.mensaje,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 32.dp)
                            )
                        }
                    }
                }
            }
        }
    )
}
