package cl.travy.app.viewmodel.events

sealed class BuscarViajeEvent {
    data class OnOrigenChange(val origen: String) : BuscarViajeEvent()
    data class OnDestinoChange(val destino: String) : BuscarViajeEvent()
    data class OnFechaChange(val fecha: Long?) : BuscarViajeEvent()
    object OnBuscarClick : BuscarViajeEvent()
}