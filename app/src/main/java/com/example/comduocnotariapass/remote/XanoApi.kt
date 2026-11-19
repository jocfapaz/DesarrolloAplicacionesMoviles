package com.example.comduocnotariapass.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface XanoApi {

    @POST("auth/register")
    suspend fun registrar(@Body body: RegistroRequest): AuthResponse

    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): AuthResponse

    @GET("formato")
    suspend fun formatos(): List<FormatoDto>

    @GET("tramite")
    suspend fun tramites(@Query("usuario_id") usuarioId: Long): List<TramiteDto>

    @POST("tramite")
    suspend fun crear(@Body body: CrearTramiteRequest): TramiteDto

    @GET("tramite/{id}")
    suspend fun detalle(@Path("id") id: Long): TramiteDetalleDto

    @POST("tramite/{id}/documentos")
    suspend fun documento(
        @Path("id") id: Long,
        @Body body: DocumentoRequest
    ): DocumentoDto

    @POST("tramite/{id}/enviar")
    suspend fun enviar(@Path("id") id: Long, @Body body: EnviarRequest): TramiteDto

    @GET("tramite/{id}/estado")
    suspend fun estado(@Path("id") id: Long): EstadoDto

    @POST("tramite/{id}/resolucion")
    suspend fun resolucion(
        @Path("id") id: Long,
        @Body body: ResolucionRequest
    ): TramiteDto

    @POST("notificaciones")
    suspend fun notificacion(@Body body: NotificacionRequest): Any

    @GET("notaria")
    suspend fun notarias(@Query("comuna") comuna: String): List<NotariaDto>
}
