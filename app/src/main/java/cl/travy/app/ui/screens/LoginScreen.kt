package cl.travy.app.ui.screens


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.travy.app.model.data.LoginUiState
import cl.travy.app.ui.components.TopAppBarGenerico
import cl.travy.app.ui.theme.TravyAppTheme
import cl.travy.app.viewmodel.AuthViewModel


@Composable
fun LoginScreen(
    uiState: LoginUiState,
    onLoginValueChange: (String, String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onBackClick: (() -> Unit)?

) {

    Scaffold(

        topBar = {
            TopAppBarGenerico(
                titulo = "Inicio de Sesión",

                onNavegarAtras = onBackClick
            )
        }
    ) { innerPadding ->

        LoginContent(
            modifier = Modifier.padding(innerPadding),
            username = uiState.usuario,
            password = uiState.contrasena,
            onUsernameChange = { newUser -> onLoginValueChange(newUser, uiState.contrasena) },
            onPasswordChange = { newPass -> onLoginValueChange(uiState.usuario, newPass) },
            onLoginClick = onLoginClick,
            onRegisterClick = onRegisterClick,
            isLoading = uiState.isLoading,
            error = uiState.error
        )
    }
}


@Composable
fun LoginContent(
    modifier: Modifier = Modifier,
    username: String,
    password: String,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    isLoading: Boolean,
    error: String?
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "¡Bienvenido de nuevo!",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            "Inicia sesión para acceder a tu cuenta",
            fontSize = 15.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(30.dp))
        UserInputField(value = username, onValueChange = onUsernameChange)
        Spacer(modifier = Modifier.height(15.dp))
        PasswordInputField(value = password, onValueChange = onPasswordChange)
        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }


        Spacer(modifier = Modifier.height(24.dp))

        BotonIniciarSesion(
            enabled = !isLoading,
            onClick = onLoginClick
        )




    }
}


@Composable
fun UserInputField(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        onValueChange = { onValueChange(it) },
        label = { Text("Introduzca su correo") },
        placeholder = { Text("ejemplo@ejemplo.com") },
        leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "icono de email") },
        shape = RoundedCornerShape(12.dp),
        singleLine = true
    )
}

@Composable
fun PasswordInputField(value: String, onValueChange: (String) -> Unit) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        onValueChange = { onValueChange(it) },
        label = { Text("Introduzca su contraseña") },
        leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "icono de contraseña") },
        trailingIcon = {
            val image = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, contentDescription = "Mostrar/Ocultar contraseña")
            }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        shape = RoundedCornerShape(12.dp),
        singleLine = true
    )
}



@Composable
fun BotonIniciarSesion(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .height(50.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
    ) {
        if (enabled) {
            Text(
                "Iniciar Sesión",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        } else {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = Color.White
            )
        }
    }
}

@Composable
fun TextoEnlace(
    modifier: Modifier = Modifier,
    onRegisterClick: () -> Unit
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text("¿No tienes una cuenta? ")
        Text(
            "Regístrate",
            Modifier.clickable(onClick = onRegisterClick),
            color = Color.Blue,
            fontWeight = FontWeight.Bold
        )
    }
}



@Preview(name = "Login sin botón Atrás", showBackground = true)
@Composable
fun LoginScreenPreview_Start() {
    TravyAppTheme {
        LoginScreen(
            uiState = LoginUiState(usuario = "test@user.com", contrasena = "12345"),
            onLoginValueChange = { _, _ -> },
            onLoginClick = { },
            onRegisterClick = { },
            onBackClick = null
        )
    }
}

@Preview(name = "Login CON botón Atrás", showBackground = true)
@Composable
fun LoginScreenPreview_WithBack() {
    TravyAppTheme {
        LoginScreen(
            uiState = LoginUiState(),
            onLoginValueChange = { _, _ -> },
            onLoginClick = { },
            onRegisterClick = { },
            onBackClick = { }
        )
    }
}

