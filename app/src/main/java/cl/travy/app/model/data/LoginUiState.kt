package cl.travy.app.model.data

data class LoginUiState(
    val usuario: String = "",
    val contrasena: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)