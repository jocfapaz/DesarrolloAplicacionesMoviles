package com.example.comduocnotariapass.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

/*
 - Interfaz de servicio de Retrofit que define los endpoints de la API REST alojada en Xano.

 - Todas las funciones están marcadas como `suspend` para ejecutarse de forma asíncrona
 - mediante Corrutinas de Kotlin.
*/

interface XanoApi {


    // ========================================================================
    // AUTENTICACIÓN
    // ========================================================================


    // Registra un nuevo usuario en el sistema y crea su registro inicial.
    @POST("auth/register")
    suspend fun registrar(@Body body: RegistroRequest): AuthResponse


    // Inicia sesión con las credenciales de un usuario existente.
    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): AuthResponse


    // ========================================================================
    // CATÁLOGOS Y CONSULTAS GENERALES
    // ========================================================================


    //Obtiene el listado completo de plantillas o formatos de trámites notariales disponibles.
    @GET("formato")
    suspend fun formatos(): List<FormatoDto>

    // Busca las notarías disponibles filtrando por una comuna específica.
    @GET("notaria")
    suspend fun notarias(@Query("comuna") comuna: String): List<NotariaDto>


    // ========================================================================
    // GESTIÓN DE TRÁMITES
    // ========================================================================


    // Recupera todos los trámites asociados a un usuario específico.
    @GET("tramite")
    suspend fun tramites(@Query("usuario_id") usuarioId: Long): List<TramiteDto>


    // Crea una nueva solicitud de trámite en estado inicial.
    @POST("tramite")
    suspend fun crear(@Body body: CrearTramiteRequest): TramiteDto


    // Actualiza todos los campos de un trámite existente en el grupo de edición de Xano.
    @PATCH
    suspend fun actualizar(@Url url: String, @Body body: ActualizarTramiteRequest): TramiteDto


    // Obtiene la información detallada de un trámite junto con su lista de documentos adjuntos.
    @GET("tramite/{id}")
    suspend fun detalle(@Path("id") id: Long): TramiteDetalleDto


    // Registra o adjunta los metadatos de un documento a un trámite existente.
    @POST("tramite/{id}/documentos")
    suspend fun documento(
        @Path("id") id: Long,
        @Body body: DocumentoRequest
    ): DocumentoDto


    // Envía formalmente un trámite a revisión notarial especificando el tipo de firma.
    @POST("tramite/{id}/enviar")
    suspend fun enviar(@Path("id") id: Long, @Body body: EnviarRequest): TramiteDto


    // Consulta rápida del estado actual y resultado de revisión de un trámite.
    @GET("tramite/{id}/estado")
    suspend fun estado(@Path("id") id: Long): EstadoDto


    // Registra la resolución final (aprobación o rechazo) de un trámite.
    @POST("tramite/{id}/resolucion")
    suspend fun resolucion(
        @Path("id") id: Long,
        @Body body: ResolucionRequest
    ): TramiteDto


    // ========================================================================
    // NOTIFICACIONES
    // ========================================================================


    //Envía una notificación o alerta vinculada a un trámite específico
    @POST("notificaciones")
    suspend fun notificacion(@Body body: NotificacionRequest): Any


}
