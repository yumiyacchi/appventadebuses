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

object PrecioAsientoConfig {
    private val precios: Map<TipoAsiento, Double> = mapOf(
        TipoAsiento.STANDARD to 6000.00,
        TipoAsiento.SEMICAMA to 7500.00,
        TipoAsiento.SALONCAMA to 10000.00


    )

    fun obtenerPrecio(tipo: TipoAsiento): Double {
        return precios[tipo] ?: 0.0
    }
}


data class Asiento(
    val numero: Int,
    val tipo: TipoAsiento,
    val posicion: PosicionAsiento,
    val estado: EstadoAsiento,
    val precio: Double
)