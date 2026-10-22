package com.example.comduocnotariapass.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.example.comduocnotariapass.model.Documento
import java.util.Locale
import java.util.UUID

// Límite de 10 MB por archivo declarado en la pantalla de adjuntos
const val TAMANO_MAXIMO_BYTES: Long = 10L * 1024L * 1024L

// Tipos de archivo permitidos: PDF, PNG o JPG
val MIME_PERMITIDOS = listOf("application/pdf", "image/png", "image/jpeg")

enum class ResultadoValidacion {
    VALIDO,
    TIPO_NO_PERMITIDO,
    EXCEDE_LIMITE
}

// Lee nombre, tipo y tamaño reales de un archivo elegido con el selector del sistema (SAF)
fun Context.leerDocumento(uri: Uri): Documento {
    var nombre = ""
    var tamano = 0L

    contentResolver.query(
        uri,
        arrayOf(OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE),
        null,
        null,
        null
    )?.use { cursor ->
        if (cursor.moveToFirst()) {
            val indiceNombre = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (indiceNombre >= 0) nombre = cursor.getString(indiceNombre).orEmpty()
            val indiceTamano = cursor.getColumnIndex(OpenableColumns.SIZE)
            if (indiceTamano >= 0 && !cursor.isNull(indiceTamano)) tamano = cursor.getLong(indiceTamano)
        }
    }

    if (nombre.isBlank()) nombre = uri.lastPathSegment ?: "documento"

    val mimeDelProveedor = contentResolver.getType(uri)
    val mime = if (mimeDelProveedor != null && mimeDelProveedor != "application/octet-stream") {
        mimeDelProveedor
    } else {
        mimePorExtension(nombre) ?: mimeDelProveedor ?: "application/octet-stream"
    }

    return Documento(
        id = UUID.randomUUID().toString(),
        nombre = nombre,
        mimeType = mime,
        tamanoBytes = tamano,
        uri = uri.toString()
    )
}

// Algunos proveedores no informan el MIME real, así que se deduce de la extensión
private fun mimePorExtension(nombre: String): String? {
    val minuscula = nombre.lowercase(Locale.getDefault())
    return when {
        minuscula.endsWith(".pdf") -> "application/pdf"
        minuscula.endsWith(".png") -> "image/png"
        minuscula.endsWith(".jpg") || minuscula.endsWith(".jpeg") -> "image/jpeg"
        else -> null
    }
}

fun validarDocumento(documento: Documento): ResultadoValidacion = when {
    documento.mimeType !in MIME_PERMITIDOS -> ResultadoValidacion.TIPO_NO_PERMITIDO
    documento.tamanoBytes > TAMANO_MAXIMO_BYTES -> ResultadoValidacion.EXCEDE_LIMITE
    else -> ResultadoValidacion.VALIDO
}

fun mensajeError(resultado: ResultadoValidacion): String = when (resultado) {
    ResultadoValidacion.VALIDO -> ""
    ResultadoValidacion.TIPO_NO_PERMITIDO -> "Tipo de archivo no permitido. Solo PDF, PNG o JPG."
    ResultadoValidacion.EXCEDE_LIMITE -> "El archivo supera el límite de 10 MB."
}

fun formatearTamano(bytes: Long): String = when {
    bytes <= 0L -> "Tamaño desconocido"
    bytes < 1024L -> "$bytes B"
    bytes < 1024L * 1024L -> "${bytes / 1024L} KB"
    else -> String.format(Locale.getDefault(), "%.1f MB", bytes / (1024.0 * 1024.0))
}

fun etiquetaTipo(mimeType: String): String = when (mimeType) {
    "application/pdf" -> "PDF"
    "image/png" -> "PNG"
    "image/jpeg" -> "JPG"
    else -> "Archivo"
}

// Detalle mostrado bajo el nombre del documento: "PDF · 240 KB"
fun detalleDocumento(documento: Documento): String =
    "${etiquetaTipo(documento.mimeType)} · ${formatearTamano(documento.tamanoBytes)}"
