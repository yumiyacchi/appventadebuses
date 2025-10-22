package cl.travy.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.travy.app.model.data.LoginUiState
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private var loggedInUserDocumentId: String? = null

    fun onLoginValueChange(user: String, pass: String) {
        _uiState.update { currentState ->
            currentState.copy(usuario = user, contrasena = pass)
        }
    }


    fun login(onLoginSuccess: () -> Unit) {
        _uiState.update { it.copy(isLoading = true, error = null) }

        val userCajeroIngresado = _uiState.value.usuario.trim()
        val passIngresada = _uiState.value.contrasena.trim()

        if (userCajeroIngresado.isBlank() || passIngresada.isBlank()) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    error = "El usuario y la contraseña son obligatorios."
                )
            }
            return
        }

        viewModelScope.launch {
            try {
                val db = Firebase.firestore

                val querySnapshot = db.collection("users")
                    .whereEqualTo("email", userCajeroIngresado)
                    .limit(1)
                    .get()
                    .await()

                if (querySnapshot.isEmpty) {
                    _uiState.update { it.copy(isLoading = false, error = "Usuario no encontrado.") }
                    return@launch
                }

                val document = querySnapshot.documents.first()
                val passGuardada = document.getString("pass")

                if (passIngresada == passGuardada) {
                    _uiState.update { it.copy(isLoading = false) }

                    loggedInUserDocumentId = document.id


                    db.collection("users").document(loggedInUserDocumentId!!)
                        .update("loggeado", true)
                        .addOnSuccessListener {
                            println("Usuario '$userCajeroIngresado' marcado como loggeado.")
                        }
                        .addOnFailureListener { e ->
                            println("Error al actualizar el estado 'loggeado': ${e.message}")
                        }


                    onLoginSuccess()

                } else {
                    _uiState.update { it.copy(isLoading = false, error = "Contraseña incorrecta.") }
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Error de conexión: ${e.message}"
                    )
                }
            }
        }
    }


    fun logout(onLogoutSuccess: () -> Unit) {
        viewModelScope.launch {
            if (loggedInUserDocumentId != null) {
                try {
                    Firebase.firestore.collection("users").document(loggedInUserDocumentId!!)
                        .update("loggeado", false)
                        .await()
                    println("Usuario con DocID '$loggedInUserDocumentId' marcado como deslogueado.")
                } catch (e: Exception) {
                    println("Error al actualizar 'loggeado' a false: ${e.message}")
                } finally {
                    loggedInUserDocumentId = null
                    onLogoutSuccess()
                }
            } else {
                println("No había un DocumentID de usuario guardado, procediendo a desloguear solo en la UI.")
                onLogoutSuccess()
            }
        }
    }
}
