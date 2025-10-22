package cl.travy.app.model.data

data class InfoPasajero(
    val numeroAsiento: Int,
    val nombre: String,
    val apellido: String,
    val genero: String,
    val email: String,
    val telefono: String
) {
    val estaCompleto: Boolean
        get() = nombre.isNotBlank() &&
                apellido.isNotBlank() &&
                genero.isNotBlank() &&
                email.isNotBlank() &&
                telefono.isNotBlank()
}
