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

// Calidad o rol del solicitante dentro del trámite
enum class RolSolicitante(val nombre: String) {
    PERSONA_NATURAL("Persona natural"),
    APODERADO("Apoderado"),
    MANDATARIO("Mandatario"),
    REPRESENTANTE_LEGAL("Representante legal"),
    PERSONA_JURIDICA("Persona jurídica")
}

// Datos que el solicitante verifica antes de continuar con el trámite
data class DatosSolicitante(
    val nombre: String = "Persona Demo",
    val id: String = "ID-DEMO-001",
    val rol: RolSolicitante = RolSolicitante.PERSONA_NATURAL
) {
    val nombreCompleto: Boolean get() = nombre.isNotBlank()
    val idCompleto: Boolean get() = id.isNotBlank()
    val datosCompletos: Boolean get() = nombreCompleto && idCompleto
}

// El botón "Continuar" se habilita solo con los campos obligatorios completos
// y con la confirmación de que los antecedentes fueron revisados
fun puedeContinuarVerificacion(datos: DatosSolicitante, antecedentesRevisados: Boolean): Boolean =
    datos.datosCompletos && antecedentesRevisados

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
