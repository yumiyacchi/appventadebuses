package cl.travy.app.ui.layout

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cl.travy.app.ui.components.TopAppBarGenerico


@Composable
fun LayoutPantallaBase(
    titulo: String,
    modifier: Modifier = Modifier,
    onNavegarAtras: (() -> Unit)? = null,
    contenidoPrincipal: @Composable (Modifier) -> Unit,
    bottomBar: @Composable () -> Unit = {}
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBarGenerico(
                titulo = titulo,
                onNavegarAtras = onNavegarAtras
            )
        },
        bottomBar = bottomBar
    ) { innerPadding ->
        contenidoPrincipal(Modifier.padding(innerPadding))
    }
}
