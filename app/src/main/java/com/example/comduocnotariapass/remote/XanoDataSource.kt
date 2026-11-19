package com.example.comduocnotariapass.remote

/*
 - Fuente de datos remota (Data Source) que encapsula las llamadas a [XanoApi].

 - Actúa como capa intermedia entre los repositorios y el cliente Retrofit, encargándose de
 - construir los objetos `Request` y gestionar la persistencia del token de autenticación.
*/

class XanoDataSource(private val api: XanoApi = XanoClient.api) {

    companion object {
        // Elimina el token de sesión almacenado en [XanoClient], cerrando la sesión activa a nivel de red.
        fun limpiarToken() {
            XanoClient.token = null
        }
    }


    // ========================================================================
    // AUTENTICACIÓN
    // ========================================================================


    // Registra un nuevo usuario en la plataforma y almacena automáticamente su token de sesión.
    //Inicializa los campos no requeridos durante el registro con valores por defecto.
    suspend fun registrar(nombre: String, correo: String, clave: String): AuthResponse {
        val request = RegistroRequest(
            code = "",
            usuarioId = 0,
            formatoId = "",
            formatoNombre = "",
            fechaCreacion = "",
            estado = "ACTIVO",
            resultado = "",
            tipoFirma = "",
            solicitanteNombre = nombre,
            solicitanteId = "",
            solicitanteRol = "",
            notariaId = 0,
            observacion = null,
            correo = correo,
            clave = clave
        )

        val respuesta = api.registrar(request)
        guardarToken(respuesta)
        return respuesta
    }


    // Autentica a un usuario existente y guarda su token para futuras peticiones.
    suspend fun login(correo: String, clave: String): AuthResponse {
        val respuesta = api.login(LoginRequest(correo, clave))
        guardarToken(respuesta)
        return respuesta
    }


    // ========================================================================
    // CONSULTAS Y CATÁLOGOS
    // ========================================================================


    // Obtiene la lista de formatos de trámites notariales disponibles.
    suspend fun formatos(): List<FormatoDto> = api.formatos()


    // Consulta las notarías disponibles en una comuna específica.
    suspend fun notarias(comuna: String): List<NotariaDto> = api.notarias(comuna)


    // ========================================================================
    // GESTIÓN DE TRÁMITES
    // ========================================================================


    // Obtiene todos los trámites creados por un usuario.
    suspend fun tramites(usuarioId: Long): List<TramiteDto> = api.tramites(usuarioId)


    // Crea un nuevo trámite notarial en el sistema.
    suspend fun crear(
        usuarioId: Long,
        formatoId: String,
        solicitanteNombre: String,
        solicitanteId: String,
        solicitanteRol: String
    ): TramiteDto = api.crear(
        CrearTramiteRequest(
            usuarioId = usuarioId,
            formatoId = formatoId,
            solicitanteNombre = solicitanteNombre,
            solicitanteId = solicitanteId,
            solicitanteRol = solicitanteRol
        )
    )


    // Recupera el detalle completo de un trámite incluyendo sus documentos adjuntos.
    suspend fun detalle(id: Long): TramiteDetalleDto = api.detalle(id)


    // Registra los metadatos de un archivo adjunto dentro de un trámite.
    suspend fun documento(
        id: Long,
        nombre: String,
        mimeType: String,
        tamanoBytes: Long
    ): DocumentoDto = api.documento(id, DocumentoRequest(nombre, mimeType, tamanoBytes))


    // Envía un trámite a revisión indicando el tipo de firma elegido.
    suspend fun enviar(id: Long, tipoFirma: String): TramiteDto =
        api.enviar(id, EnviarRequest(tipoFirma))


    // Consulta el estado actual de un trámite.
    suspend fun estado(id: Long): EstadoDto = api.estado(id)


    // Registra el dictamen o resolución final de un trámite.
    suspend fun resolucion(id: Long, resultado: String, comentario: String? = null): TramiteDto =
        api.resolucion(id, ResolucionRequest(resultado, comentario))


    // Envía una notificación vinculada a un trámite.
    suspend fun notificacion(tramiteId: Long, mensaje: String): Any =
        api.notificacion(NotificacionRequest(tramiteId, mensaje))


    // ========================================================================
    // MÉTODOS AUXILIARES PRIVADOS
    // ========================================================================


    // Extrae el token válido de [AuthResponse] y, si no está vacío, lo asigna a [XanoClient.token].
    private fun guardarToken(respuesta: AuthResponse) {
        respuesta.tokenValido.takeIf { it.isNotBlank() }?.let { XanoClient.token = it }
    }
}
