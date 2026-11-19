package com.example.comduocnotariapass.remote

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/*
 - Objeto Singleton encargado de configurar y proveer la instancia de Retrofit
 - para comunicarse con el backend de Xano[cite: 3].

 - Gestiona de forma centralizada la URL base, el almacenamiento en memoria del token
 - de sesión y los interceptores HTTP para autenticación y depuración
 */

object XanoClient {


    // URL base de la instancia de API en Xano.
    private const val BASE_URL = "https://x8ki-letl-twmt.n7.xano.io/api:JSDg7Jzb/"


    // Token JWT de autenticación del usuario activo.
    // Si no es nulo, se adjunta automáticamente en la cabecera `Authorization` de cada petición.
    var token: String? = null


    //Instancia XanoApi
    // Se inicializa una única vez la primera vez que es invocada y se reutiliza en toda la app
    val api: XanoApi by lazy {


        // Interceptor que inyecta el token Bearer si existe una sesión activa.
        val autenticacion = Interceptor { cadena ->
            val solicitud = token?.let { valor ->
                cadena.request().newBuilder()
                    .addHeader("Authorization", "Bearer $valor")
                    .build()
            } ?: cadena.request()
            cadena.proceed(solicitud)
        }


        // Interceptor para registrar en Logcat el cuerpo completo de las peticiones y respuestas HTTP.
        val registro = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }


        // Construcción del cliente OkHttp con los interceptores configurados
        val cliente = OkHttpClient.Builder()
            .addInterceptor(autenticacion)
            .addInterceptor(registro)
            .build()


        // Creación de la instancia de Retrofit con soporte para serialización JSON mediante Gson
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(cliente)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(XanoApi::class.java)
    }
}
