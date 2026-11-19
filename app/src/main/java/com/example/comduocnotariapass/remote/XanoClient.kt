package com.example.comduocnotariapass.remote

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object XanoClient {

    private const val BASE_URL = "https://x8ki-letl-twmt.n7.xano.io/api:JSDg7Jzb/"

    var token: String? = null

    val api: XanoApi by lazy {
        val autenticacion = Interceptor { cadena ->
            val solicitud = token?.let { valor ->
                cadena.request().newBuilder()
                    .addHeader("Authorization", "Bearer $valor")
                    .build()
            } ?: cadena.request()
            cadena.proceed(solicitud)
        }

        val registro = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val cliente = OkHttpClient.Builder()
            .addInterceptor(autenticacion)
            .addInterceptor(registro)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(cliente)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(XanoApi::class.java)
    }
}
