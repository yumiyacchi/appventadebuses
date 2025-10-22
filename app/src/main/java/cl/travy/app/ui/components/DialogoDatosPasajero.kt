package cl.travy.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.travy.app.model.data.InfoPasajero

@Composable
fun DialogoDatosPasajero(
    mostrarDialog: Boolean,
    pasajero: InfoPasajero?,
    onDatosChange: (InfoPasajero) -> Unit,
    onConfirmar: () -> Unit,
    onDismiss: () -> Unit
) {
    if (mostrarDialog && pasajero != null) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = "Datos del Pasajero (Asiento ${pasajero.numeroAsiento})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = pasajero.nombre,
                        onValueChange = { onDatosChange(pasajero.copy(nombre = it)) },
                        label = { Text("Nombre") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = pasajero.apellido,
                        onValueChange = { onDatosChange(pasajero.copy(apellido = it)) },
                        label = { Text("Apellido") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = pasajero.genero,
                        onValueChange = { onDatosChange(pasajero.copy(genero = it)) },
                        label = { Text("Género") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = pasajero.email,
                        onValueChange = { onDatosChange(pasajero.copy(email = it)) },
                        label = { Text("E-mail") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = pasajero.telefono,
                        onValueChange = { onDatosChange(pasajero.copy(telefono = it)) },
                        label = { Text("Teléfono") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = onConfirmar,
                    enabled = pasajero.estaCompleto // Usa la validación del data class
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancelar")
                }
            }
        )
    }
}
