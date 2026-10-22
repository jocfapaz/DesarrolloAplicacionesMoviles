package com.example.comduocnotariapass.model

enum class EstadoPersistido(val etiqueta: String) {
    BORRADOR("Borrador"),
    EN_REVISION("En revisión"),
    OBSERVACIONES("Con observaciones"),
    FINALIZADO("Finalizado")
}

data class TramiteResumen(
    val id: Long,
    val titulo: String,
    val codigo: String,
    val fechaCreacion: Long,
    val estado: EstadoPersistido,
    val resultado: ResultadoRevision?
)
