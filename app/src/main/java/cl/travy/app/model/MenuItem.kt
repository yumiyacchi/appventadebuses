package cl.travy.app.model.data

import androidx.compose.ui.graphics.vector.ImageVector

data class MenuItem(
    val texto: String,
    val icono: ImageVector,
    val onClick: ()  -> Unit,
    val habilitado: Boolean = true
)