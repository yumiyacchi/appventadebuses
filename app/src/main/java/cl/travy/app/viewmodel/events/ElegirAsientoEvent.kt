package cl.travy.app.viewmodel.events

import cl.travy.app.model.data.asiento.Asiento

sealed class ElegirAsientoEvent {
    data class OnAsientoClick(val asiento: Asiento) : ElegirAsientoEvent()
    object OnContinuarClick : ElegirAsientoEvent()
}
