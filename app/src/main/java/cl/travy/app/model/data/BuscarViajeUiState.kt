package cl.travy.app.model.data

data class BuscarViajeUiState(
    val origen: String = "",
    val destino: String = "",
    val fecha: Long? = null,
    val isLoading: Boolean = false,
    val isButtonEnabled: Boolean = false
)

