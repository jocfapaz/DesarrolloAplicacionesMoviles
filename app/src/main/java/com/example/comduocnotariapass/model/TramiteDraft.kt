package com.example.comduocnotariapass.model

// Documento que puede ir en un borrador de trámite.
// uri == null -> documento incluido en el formato (lo entrega la notaría).
// uri != null -> documento propio adjuntado por el solicitante.
data class Documento(
    val id: String,
    val nombre: String,
    val mimeType: String,
    val tamanoBytes: Long,
    val uri: String? = null
) {
    val esPropio: Boolean get() = uri != null
}

// Formato predefinido que el solicitante puede elegir al crear un trámite
data class FormatoTramite(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val documentosInclusos: List<Documento>
)

// Catálogo de formatos disponibles en "Selección de Trámite y Archivos"
object CatalogoTramites {

    val formatos: List<FormatoTramite> = listOf(
        FormatoTramite(
            id = "poder-simple",
            nombre = "Poder simple",
            descripcion = "Para representación legal ante un tercero.",
            documentosInclusos = listOf(
                Documento(
                    id = "formato-poder-simple",
                    nombre = "formato_poder_simple.pdf",
                    mimeType = "application/pdf",
                    tamanoBytes = 180 * 1024L
                )
            )
        ),
        FormatoTramite(
            id = "declaracion-jurada",
            nombre = "Declaración jurada simple",
            descripcion = "Declaración jurada simple de fortnightaria.",
            documentosInclusos = listOf(
                Documento(
                    id = "formato-declaracion-jurada",
                    nombre = "formato_declaracion_jurada.pdf",
                    mimeType = "application/pdf",
                    tamanoBytes = 120 * 1024L
                ),
                Documento(
                    id = "formato-declaracion-jurada-anexo",
                    nombre = "anexo_declaracion_jurada.pdf",
                    mimeType = "application/pdf",
                    tamanoBytes = 190 * 1024L
                )
            )
        ),
        FormatoTramite(
            id = "autorizacion-viaje",
            nombre = "Autorización de viaje",
            descripcion = "Autorización de viaje de menores.",
            documentosInclusos = listOf(
                Documento(
                    id = "formato-autorizacion-viaje",
                    nombre = "formato_autorizacion_viaje.pdf",
                    mimeType = "application/pdf",
                    tamanoBytes = 210 * 1024L
                )
            )
        )
    )

    val formatoPorDefecto: FormatoTramite = formatos.first { it.id == "declaracion-jurada" }

    fun buscar(id: String): FormatoTramite = formatos.firstOrNull { it.id == id } ?: formatoPorDefecto
}
