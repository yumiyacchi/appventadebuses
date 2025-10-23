package cl.travy.app.model.data.asiento

enum class PosicionAsiento {
    VENTANA,
    PASILLO
}

enum class TipoAsiento {
    STANDARD,
    SEMICAMA,
    SALONCAMA
}

enum class EstadoAsiento {
    DISPONIBLE,
    OCUPADO,
    SELECCIONADO
}

data class Asiento(
    val numero: Int,
    val tipo: TipoAsiento,
    val posicion: PosicionAsiento,
    val estado: EstadoAsiento
)