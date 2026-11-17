package com.example.comduocnotariapass.remote

import com.example.comduocnotariapass.model.Documento
import com.example.comduocnotariapass.model.EstadoPersistido
import com.example.comduocnotariapass.model.FormatoTramite
import com.example.comduocnotariapass.model.ResultadoRevision
import com.example.comduocnotariapass.model.TramiteResumen
import com.google.gson.annotations.SerializedName

data class LoginRequest(val correo: String, val clave: String)

data class RegistroRequest(val nombre: String, val correo: String, val clave: String)

data class CrearTramiteRequest(
    val usuarioId: Long,
    val formatoId: String,
    val solicitanteNombre: String,
    val solicitanteId: String,
    val solicitanteRol: String
)

data class DocumentoRequest(
    val nombre: String,
    val mimeType: String,
    val tamanoBytes: Long
)

data class EnviarRequest(val tipoFirma: String)

data class ResolucionRequest(val resultado: String, val comentario: String? = null)

data class NotificacionRequest(val tramiteId: Long, val mensaje: String)

data class AuthResponse(
    @SerializedName("authToken") val authToken: String? = null,
    @SerializedName("token") val token: String? = null,
    @SerializedName("id") val id: Long = 0,
    @SerializedName("nombre") val nombre: String = "",
    @SerializedName("correo") val correo: String = ""
) {
    val tokenValido: String get() = authToken ?: token.orEmpty()
}

data class DocumentoDto(
    @SerializedName("id") val id: Long = 0,
    @SerializedName("nombre") val nombre: String = "",
    @SerializedName("mimeType") val mimeType: String = "",
    @SerializedName("tamanoBytes") val tamanoBytes: Long = 0,
    @SerializedName("uri") val uri: String? = null
)

data class FormatoDto(
    @SerializedName("id") val id: String = "",
    @SerializedName("nombre") val nombre: String = "",
    @SerializedName("descripcion") val descripcion: String = "",
    @SerializedName("documentosInclusos") val documentosInclusos: List<DocumentoDto> = emptyList()
)

data class TramiteDto(
    @SerializedName("id") val id: Long = 0,
    @SerializedName("usuarioId") val usuarioId: Long = 0,
    @SerializedName("codigo") val codigo: String = "",
    @SerializedName("formatoId") val formatoId: String = "",
    @SerializedName("formatoNombre") val formatoNombre: String = "",
    @SerializedName("fechaCreacion") val fechaCreacion: Long = 0,
    @SerializedName("estado") val estado: String = "BORRADOR",
    @SerializedName("resultado") val resultado: String? = null,
    @SerializedName("tipoFirma") val tipoFirma: String = "NOTARIADA",
    @SerializedName("solicitanteNombre") val solicitanteNombre: String = "",
    @SerializedName("solicitanteId") val solicitanteId: String = "",
    @SerializedName("solicitanteRol") val solicitanteRol: String = "PERSONA_NATURAL"
)

data class TramiteDetalleDto(
    @SerializedName("tramite") val tramite: TramiteDto? = null,
    @SerializedName("documentos") val documentos: List<DocumentoDto> = emptyList()
)

data class EstadoDto(
    @SerializedName("estado") val estado: String = "",
    @SerializedName("resultado") val resultado: String? = null,
    @SerializedName("codigo") val codigo: String = ""
)

data class NotariaDto(
    @SerializedName("id") val id: Long = 0,
    @SerializedName("nombre") val nombre: String = "",
    @SerializedName("comuna") val comuna: String = "",
    @SerializedName("direccion") val direccion: String = "",
    @SerializedName("lat") val lat: Double = 0.0,
    @SerializedName("lng") val lng: Double = 0.0
)

fun DocumentoDto.aDocumento(): Documento = Documento(
    id = id.toString(),
    nombre = nombre,
    mimeType = mimeType,
    tamanoBytes = tamanoBytes,
    uri = uri
)

fun FormatoDto.aFormato(): FormatoTramite = FormatoTramite(
    id = id,
    nombre = nombre,
    descripcion = descripcion,
    documentosInclusos = documentosInclusos.map { it.aDocumento() }
)

fun TramiteDto.aResumen(): TramiteResumen = TramiteResumen(
    id = id,
    titulo = formatoNombre,
    codigo = codigo,
    fechaCreacion = fechaCreacion,
    estado = runCatching { EstadoPersistido.valueOf(estado) }
        .getOrDefault(EstadoPersistido.BORRADOR),
    resultado = resultado?.let { valor ->
        runCatching { ResultadoRevision.valueOf(valor) }.getOrNull()
    }
)
