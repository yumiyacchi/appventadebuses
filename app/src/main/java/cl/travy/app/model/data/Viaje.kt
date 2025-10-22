package cl.travy.app.model.data

import cl.travy.app.model.data.asiento.TipoAsiento


data class Viaje(
    // Identificadores ruta
    val id: Int,
    val origen: String,
    val destino: String,
    val fecha: String,
    // Horarios
    val horaSalida: String,
    val horaLlegada: String,
    // Identificador
    val transportista: String,
    //Detalles de los asientos como precio o el tipo
    val tipoAsiento: TipoAsiento,
    val precio: Double,


    val totalAsientos: Int,
    val asientosOcupados: List<Int> = emptyList(),

    ){
    val asientosDisponibles: Int
        get() = totalAsientos - asientosOcupados.size
}