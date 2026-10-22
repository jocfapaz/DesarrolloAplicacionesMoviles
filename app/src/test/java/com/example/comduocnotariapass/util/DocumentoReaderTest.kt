package com.example.comduocnotariapass.util

import com.example.comduocnotariapass.model.CatalogoTramites
import com.example.comduocnotariapass.model.Documento
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DocumentoReaderTest {

    private fun documento(mime: String, tamano: Long) = Documento(
        id = "doc-1",
        nombre = "archivo.pdf",
        mimeType = mime,
        tamanoBytes = tamano,
        uri = "content://demo/archivo.pdf"
    )

    @Test
    fun pdfDentroDelLimiteEsValido() {
        val resultado = validarDocumento(documento("application/pdf", 240L * 1024L))
        assertEquals(ResultadoValidacion.VALIDO, resultado)
    }

    @Test
    fun pngYJpgSonValidos() {
        assertEquals(
            ResultadoValidacion.VALIDO,
            validarDocumento(documento("image/png", 100L * 1024L))
        )
        assertEquals(
            ResultadoValidacion.VALIDO,
            validarDocumento(documento("image/jpeg", 100L * 1024L))
        )
    }

    @Test
    fun tipoNoPermitidoEsRechazado() {
        val resultado = validarDocumento(documento("text/plain", 10L * 1024L))
        assertEquals(ResultadoValidacion.TIPO_NO_PERMITIDO, resultado)
        assertTrue(mensajeError(resultado).contains("PDF, PNG o JPG"))
    }

    @Test
    fun archivoSobreDiezMegabytesEsRechazado() {
        val resultado = validarDocumento(documento("application/pdf", TAMANO_MAXIMO_BYTES + 1))
        assertEquals(ResultadoValidacion.EXCEDE_LIMITE, resultado)
        assertTrue(mensajeError(resultado).contains("10 MB"))
    }

    @Test
    fun archivoEnElLimiteExactoEsValido() {
        val resultado = validarDocumento(documento("application/pdf", TAMANO_MAXIMO_BYTES))
        assertEquals(ResultadoValidacion.VALIDO, resultado)
    }

    @Test
    fun tamanoSeFormateaEnKilobytesYMegabytes() {
        assertEquals("240 KB", formatearTamano(240L * 1024L))
        assertEquals("Tamaño desconocido", formatearTamano(0L))
        assertTrue(Regex("""\d+[.,]\d MB""").matches(formatearTamano(1536L * 1024L)))
    }

    @Test
    fun detalleMuestraTipoYTamano() {
        assertEquals(
            "PDF · 240 KB",
            detalleDocumento(documento("application/pdf", 240L * 1024L))
        )
    }

    @Test
    fun catalogoTieneLosFormatosPredefinidos() {
        assertEquals(3, CatalogoTramites.formatos.size)
        assertEquals("Poder simple", CatalogoTramites.buscar("poder-simple").nombre)
        assertEquals(
            "Declaración jurada simple",
            CatalogoTramites.formatoPorDefecto.nombre
        )
        assertTrue(CatalogoTramites.formatos.all { it.documentosInclusos.isNotEmpty() })
    }
}
