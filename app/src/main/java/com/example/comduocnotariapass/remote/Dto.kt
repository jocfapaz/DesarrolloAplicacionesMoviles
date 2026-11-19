package com.example.comduocnotariapass.remote

import com.example.comduocnotariapass.model.Documento
import com.example.comduocnotariapass.model.EstadoPersistido
import com.example.comduocnotariapass.model.FormatoTramite
import com.example.comduocnotariapass.model.ResultadoRevision
import com.example.comduocnotariapass.model.TramiteResumen
import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("correo") val correo: String,
    @SerializedName("clave") val clave: String,
    @SerializedName("estado") val estado: String="123",
)



data class RegistroRequest(
    @SerializedName("correo") val correo: String,
    @SerializedName("clave") val clave: String,
    @SerializedName("code") val code: String,
    @SerializedName("usuario_id") val usuarioId: Int,
    @SerializedName("formato_id") val formatoId: String,
    @SerializedName("formato_nombre") val formatoNombre: String,
    @SerializedName("fecha_creacion") val fechaCreacion: String,
    @SerializedName("estado") val estado: String,
    @SerializedName("resultado") val resultado: String,
    @SerializedName("tipo_firma") val tipoFirma: String,
    @SerializedName("solicitante_nombre") val solicitanteNombre: String,
    @SerializedName("solicitante_id") val solicitanteId: String,
    @SerializedName("solicitante_rol") val solicitanteRol: String,
    @SerializedName("notaria_id") val notariaId: Int,
    @SerializedName("observacion") val observacion: String?
)

data class CrearTramiteRequest(
    @SerializedName("usuario_id") val usuarioId: Long,
    @SerializedName("formato_id") val formatoId: String,
    @SerializedName("solicitante_nombre") val solicitanteNombre: String,
    @SerializedName("solicitante_id") val solicitanteId: String,
    @SerializedName("solicitante_rol") val solicitanteRol: String
)

data class DocumentoRequest(
    @SerializedName("nombre") val nombre: String,
    @SerializedName("mime_type") val mimeType: String,
    @SerializedName("tamano_bytes") val tamanoBytes: Long
)

data class EnviarRequest(@SerializedName("tipo_firma") val tipoFirma: String)

data class ResolucionRequest(
    @SerializedName("resultado") val resultado: String,
    @SerializedName("comentario") val comentario: String? = null
)

data class NotificacionRequest(
    @SerializedName("tramite_id") val tramiteId: Long,
    @SerializedName("mensaje") val mensaje: String
)

data class AuthResponse(
    @SerializedName("authToken") val authToken: String? = null,
    @SerializedName("token") val token: String? = null,
    @SerializedName("id") val id: Long = 0,
    @SerializedName("nombre") val nombre: String? = null,
    @SerializedName("correo") val correo: String? = null
) {
    val tokenValido: String get() = authToken ?: token.orEmpty()
}

data class DocumentoDto(
    @SerializedName("id") val id: Long = 0,
    @SerializedName("nombre") val nombre: String? = null,
    @SerializedName("mime_type") val mimeType: String? = null,
    @SerializedName("tamano_bytes") val tamanoBytes: Long = 0,
    @SerializedName("uri") val uri: String? = null
)

data class FormatoDto(
    @SerializedName("id") val id: String? = null,
    @SerializedName("nombre") val nombre: String? = null,
    @SerializedName("clave") val clave: String? = null,
    @SerializedName("activo") val activo: Boolean = true,
    @SerializedName("descripcion") val descripcion: String? = null,
    @SerializedName("documentos_inclusos") val documentosInclusos: List<DocumentoDto>? = null
)

data class TramiteDto(
    @SerializedName("id") val id: Long = 0,
    @SerializedName("usuario_id") val usuarioId: Long = 0,
    @SerializedName("code") val codigo: String? = null,
    @SerializedName("formato_id") val formatoId: String? = null,
    @SerializedName("formato_nombre") val formatoNombre: String? = null,
    @SerializedName("fecha_creacion") val fechaCreacion: Long = 0,
    @SerializedName("estado") val estado: String? = null,
    @SerializedName("resultado") val resultado: String? = null,
    @SerializedName("tipo_firma") val tipoFirma: String? = null,
    @SerializedName("solicitante_nombre") val solicitanteNombre: String? = null,
    @SerializedName("solicitante_id") val solicitanteId: String? = null,
    @SerializedName("solicitante_rol") val solicitanteRol: String? = null,
    @SerializedName("notaria_id") val notariaId: Long? = null,
    @SerializedName("observacion") val observacion: String? = null
)

data class TramiteDetalleDto(
    @SerializedName("tramite") val tramite: TramiteDto? = null,
    @SerializedName("documentos") val documentos: List<DocumentoDto>? = null
)

data class EstadoDto(
    @SerializedName("estado") val estado: String? = null,
    @SerializedName("resultado") val resultado: String? = null,
    @SerializedName("codigo") val codigo: String? = null
)

data class NotariaDto(
    @SerializedName("id") val id: Long = 0,
    @SerializedName("nombre") val nombre: String? = null,
    @SerializedName("comuna") val comuna: String? = null,
    @SerializedName("direccion") val direccion: String? = null,
    @SerializedName("lat") val lat: Double = 0.0,
    @SerializedName("lng") val lng: Double = 0.0
)

fun DocumentoDto.aDocumento(): Documento = Documento(
    id = id.toString(),
    nombre = nombre.orEmpty(),
    mimeType = mimeType.orEmpty(),
    tamanoBytes = tamanoBytes,
    uri = uri
)

fun FormatoDto.aFormato(): FormatoTramite = FormatoTramite(
    id = id.orEmpty(),
    nombre = nombre.orEmpty(),
    descripcion = descripcion.orEmpty(),
    documentosInclusos = documentosInclusos.orEmpty().map { it.aDocumento() },
    clave = clave.orEmpty()
)

fun TramiteDto.aResumen(): TramiteResumen = TramiteResumen(
    id = id,
    titulo = formatoNombre.orEmpty(),
    codigo = codigo.orEmpty(),
    fechaCreacion = fechaCreacion,
    estado = runCatching { EstadoPersistido.valueOf(estado.orEmpty().uppercase()) }
        .getOrDefault(EstadoPersistido.BORRADOR),
    resultado = resultado?.let { valor ->
        runCatching { ResultadoRevision.valueOf(valor.uppercase()) }.getOrNull()
    }
)
