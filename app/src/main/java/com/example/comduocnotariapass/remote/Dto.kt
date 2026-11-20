package com.example.comduocnotariapass.remote

import com.example.comduocnotariapass.model.Documento
import com.example.comduocnotariapass.model.EstadoPersistido
import com.example.comduocnotariapass.model.FormatoTramite
import com.example.comduocnotariapass.model.ResultadoRevision
import com.example.comduocnotariapass.model.TramiteResumen
import com.google.gson.annotations.SerializedName

// ============================================================================
// MODELOS DE PETICIÓN (REQUESTS) - Datos enviados hacia la API
// ============================================================================

// Representa el cuerpo de la petición para el inicio de sesión de un usuario.
data class LoginRequest(
    @SerializedName("correo") val correo: String,
    @SerializedName("clave") val clave: String,
    @SerializedName("estado") val estado: String="123",
)


// Representa los datos necesarios para registrar un nuevo usuario junto con la información
// inicial de su trámite asociado.
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


// Datos requeridos para iniciar o crear una nueva solicitud de trámite notarial.
data class CrearTramiteRequest(
    @SerializedName("code") val codigo: String,
    @SerializedName("usuario_id") val usuarioId: Long,
    @SerializedName("formato_id") val formatoId: String,
    @SerializedName("formato_nombre") val formatoNombre: String,
    @SerializedName("fecha_creacion") val fechaCreacion: Long,
    @SerializedName("estado") val estado: String,
    @SerializedName("resultado") val resultado: String,
    @SerializedName("tipo_firma") val tipoFirma: String,
    @SerializedName("solicitante_nombre") val solicitanteNombre: String,
    @SerializedName("solicitante_id") val solicitanteId: String,
    @SerializedName("solicitante_rol") val solicitanteRol: String,
    @SerializedName("notaria_id") val notariaId: Long? = null,
    @SerializedName("observacion") val observacion: String? = null
)

data class ActualizarTramiteRequest(
    @SerializedName("code") val codigo: String,
    @SerializedName("usuario_id") val usuarioId: Long,
    @SerializedName("formato_id") val formatoId: String,
    @SerializedName("formato_nombre") val formatoNombre: String,
    @SerializedName("fecha_creacion") val fechaCreacion: Long,
    @SerializedName("estado") val estado: String,
    @SerializedName("resultado") val resultado: String,
    @SerializedName("tipo_firma") val tipoFirma: String,
    @SerializedName("solicitante_nombre") val solicitanteNombre: String,
    @SerializedName("solicitante_id") val solicitanteId: String,
    @SerializedName("solicitante_rol") val solicitanteRol: String,
    @SerializedName("notaria_id") val notariaId: Long? = null,
    @SerializedName("observacion") val observacion: String? = null
)

data class TramiteRemoto(
    val codigo: String,
    val usuarioId: Long,
    val formatoId: String,
    val formatoNombre: String,
    val fechaCreacion: Long,
    val estado: String,
    val resultado: String,
    val tipoFirma: String,
    val solicitanteNombre: String,
    val solicitanteId: String,
    val solicitanteRol: String,
    val notariaId: Long? = null,
    val observacion: String? = null
)


// Metadatos de un archivo adjunto que se envía al servidor.
data class DocumentoRequest(
    @SerializedName("nombre") val nombre: String,
    @SerializedName("mime_type") val mimeType: String,
    @SerializedName("tamano_bytes") val tamanoBytes: Long
)


// Petición para confirmar el envío de un trámite especificando la modalidad de firma.
data class EnviarRequest(
    @SerializedName("tipo_firma") val tipoFirma: String,
    @SerializedName("code") val codigo: String? = null,
    @SerializedName("formato_id") val formatoId: String? = null,
    @SerializedName("formato_nombre") val formatoNombre: String? = null,
    @SerializedName("fecha_creacion") val fechaCreacion: Long? = null,
    @SerializedName("estado") val estado: String? = null,
    @SerializedName("resultado") val resultado: String? = null,
    @SerializedName("solicitante_nombre") val solicitanteNombre: String? = null,
    @SerializedName("solicitante_id") val solicitanteId: String? = null,
    @SerializedName("solicitante_rol") val solicitanteRol: String? = null,
    @SerializedName("notaria_id") val notariaId: Long? = null,
    @SerializedName("observacion") val observacion: String? = null
)


// Petición utilizada por un revisor o notario para emitir el dictamen de un trámite.
data class ResolucionRequest(
    @SerializedName("resultado") val resultado: String,
    @SerializedName("comentario") val comentario: String? = null,
    @SerializedName("code") val codigo: String? = null,
    @SerializedName("formato_id") val formatoId: String? = null,
    @SerializedName("formato_nombre") val formatoNombre: String? = null,
    @SerializedName("fecha_creacion") val fechaCreacion: Long? = null,
    @SerializedName("estado") val estado: String? = null,
    @SerializedName("tipo_firma") val tipoFirma: String? = null,
    @SerializedName("solicitante_nombre") val solicitanteNombre: String? = null,
    @SerializedName("solicitante_id") val solicitanteId: String? = null,
    @SerializedName("solicitante_rol") val solicitanteRol: String? = null,
    @SerializedName("notaria_id") val notariaId: Long? = null,
    @SerializedName("observacion") val observacion: String? = null
)


// Estructura para enviar una notificación o mensaje asociado a un trámite específico.
data class NotificacionRequest(
    @SerializedName("tramite_id") val tramiteId: Long,
    @SerializedName("mensaje") val mensaje: String
)


// ============================================================================
// MODELOS DE RESPUESTA (DTOs) - Datos recibidos desde la API
// ============================================================================

//Respuesta recibida tras una autenticación exitosa.
data class AuthResponse(
    @SerializedName("authToken") val authToken: String? = null,
    @SerializedName("token") val token: String? = null,
    @SerializedName("id") val id: Long = 0,
    @SerializedName("nombre") val nombre: String? = null,
    @SerializedName("correo") val correo: String? = null
) {
    val tokenValido: String get() = authToken ?: token.orEmpty()
}


// Objeto de transferencia de datos (DTO) que representa un documento almacenado en el backend.
data class DocumentoDto(
    @SerializedName("id") val id: Long = 0,
    @SerializedName("nombre") val nombre: String? = null,
    @SerializedName("mime_type") val mimeType: String? = null,
    @SerializedName("tamano_bytes") val tamanoBytes: Long = 0,
    @SerializedName("uri") val uri: String? = null
)


// DTO que describe una plantilla o formato de trámite disponible en el sistema.
data class FormatoDto(
    @SerializedName("id") val id: String? = null,
    @SerializedName("nombre") val nombre: String? = null,
    @SerializedName("clave") val clave: String? = null,
    @SerializedName("activo") val activo: Boolean = true,
    @SerializedName("descripcion") val descripcion: String? = null,
    @SerializedName("documentos_inclusos") val documentosInclusos: List<DocumentoDto>? = null
)

// DTO con la información completa de un trámite notarial registrado.
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


// DTO compuesto que agrupa los datos principales de un trámite junto con sus archivos adjuntos.
data class TramiteDetalleDto(
    @SerializedName("tramite") val tramite: TramiteDto? = null,
    @SerializedName("documentos") val documentos: List<DocumentoDto>? = null
)


// DTO ligero utilizado para consultar actualizaciones rápidas del estado de un trámite.
data class EstadoDto(
    @SerializedName("estado") val estado: String? = null,
    @SerializedName("resultado") val resultado: String? = null,
    @SerializedName("codigo") val codigo: String? = null
)


// DTO que representa la información geográfica y de contacto de una notaría.
data class NotariaDto(
    @SerializedName("id") val id: Long = 0,
    @SerializedName("nombre") val nombre: String? = null,
    @SerializedName("comuna") val comuna: String? = null,
    @SerializedName("direccion") val direccion: String? = null,
    @SerializedName("lat") val lat: Double = 0.0,
    @SerializedName("lng") val lng: Double = 0.0
)

// ============================================================================
// FUNCIONES DE MAPEO (MAPPERS) - Conversión de DTOs a Modelos de Dominio
// ============================================================================

// Convierte un [DocumentoDto] proveniente de la API al modelo de dominio [Documento][cite: 1].
// Maneja valores nulos asignando cadenas vacías por defecto
fun DocumentoDto.aDocumento(): Documento = Documento(
    id = id.toString(),
    nombre = nombre.orEmpty(),
    mimeType = mimeType.orEmpty(),
    tamanoBytes = tamanoBytes,
    uri = uri
)

// Convierte un [FormatoDto] de red al modelo de dominio [FormatoTramite],
// transformando también su lista interna de documentos
fun FormatoDto.aFormato(): FormatoTramite = FormatoTramite(
    id = id.orEmpty(),
    nombre = nombre.orEmpty(),
    descripcion = descripcion.orEmpty(),
    documentosInclusos = documentosInclusos.orEmpty().map { it.aDocumento() },
    clave = clave.orEmpty()
)


/*
 - Transforma un [TramiteDto] en un modelo simplificado [TramiteResumen] para su visualización en listas[cite: 1].

 - Convierte de forma segura las cadenas de texto `estado` y `resultado` a sus respectivos `enum`
 - ([EstadoPersistido] y [ResultadoRevision]), asignando [EstadoPersistido.BORRADOR] o `null` en caso de
 - valores desconocidos o inválidos
*/
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
