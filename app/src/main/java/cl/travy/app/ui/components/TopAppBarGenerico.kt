package cl.travy.app.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import cl.travy.app.ui.theme.LightBlue
import cl.travy.app.ui.theme.LightGrey
import cl.travy.app.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarGenerico (titulo: String,
                       onNavegarAtras: (()-> Unit)?
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = titulo,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            if (onNavegarAtras != null){
                IconButton(onClick = onNavegarAtras) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver atrás",
                        tint = LightGrey
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = LightBlue,
            titleContentColor = White

        )
    )
}
