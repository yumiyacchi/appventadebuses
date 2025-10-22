package cl.travy.app.model.data


sealed class ListaViajesState {
    object Cargando : ListaViajesState()
    data class Exitoso(val viajes: List<Viaje>) : ListaViajesState()
    data class Error(val mensaje: String) : ListaViajesState()
}


data class SeleccionViajeUiState(

    val origen: String = "",
    val destino: String = "",
    val fechaFormateada: String = "",


    val estadoLista: ListaViajesState = ListaViajesState.Cargando
)
