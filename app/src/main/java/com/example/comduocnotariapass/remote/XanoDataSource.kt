package com.example.comduocnotariapass.remote

class XanoDataSource(private val api: XanoApi = XanoClient.api) {

    companion object {
        fun limpiarToken() {
            XanoClient.token = null
        }
    }

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


    suspend fun login(correo: String, clave: String): AuthResponse {
        val respuesta = api.login(LoginRequest(correo, clave))
        guardarToken(respuesta)
        return respuesta
    }

    suspend fun formatos(): List<FormatoDto> = api.formatos()

    suspend fun tramites(usuarioId: Long): List<TramiteDto> = api.tramites(usuarioId)

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

    suspend fun detalle(id: Long): TramiteDetalleDto = api.detalle(id)

    suspend fun documento(
        id: Long,
        nombre: String,
        mimeType: String,
        tamanoBytes: Long
    ): DocumentoDto = api.documento(id, DocumentoRequest(nombre, mimeType, tamanoBytes))

    suspend fun enviar(id: Long, tipoFirma: String): TramiteDto =
        api.enviar(id, EnviarRequest(tipoFirma))

    suspend fun estado(id: Long): EstadoDto = api.estado(id)

    suspend fun resolucion(id: Long, resultado: String, comentario: String? = null): TramiteDto =
        api.resolucion(id, ResolucionRequest(resultado, comentario))

    suspend fun notificacion(tramiteId: Long, mensaje: String): Any =
        api.notificacion(NotificacionRequest(tramiteId, mensaje))

    suspend fun notarias(comuna: String): List<NotariaDto> = api.notarias(comuna)

    private fun guardarToken(respuesta: AuthResponse) {
        respuesta.tokenValido.takeIf { it.isNotBlank() }?.let { XanoClient.token = it }
    }
}
