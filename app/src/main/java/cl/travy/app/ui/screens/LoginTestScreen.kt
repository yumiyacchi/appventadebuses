package cl.travy.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
// ...otros imports
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun LoginTestScreen(
    onLogout: () -> Unit
) {
    val currentUser = Firebase.auth.currentUser
    val isLoggedIn = currentUser != null


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoggedIn) {
            Text(
                text = "¡Bienvenido!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Sesión iniciada como: ${currentUser?.email ?: "Usuario desconocido"}")
        } else {
            Text(text = "No hay ninguna sesión activa.")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "||||| CERRAR SESIÓN |||||",
            modifier = Modifier
                .padding(top = 16.dp)
                .clickable { onLogout() },
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}