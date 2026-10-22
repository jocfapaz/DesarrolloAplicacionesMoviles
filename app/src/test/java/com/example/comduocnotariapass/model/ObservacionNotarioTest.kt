package com.example.comduocnotariapass.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ObservacionNotarioTest {

    @Test
    fun observacionDemoUsaLosDocumentosDelFormato() {
        val formato = CatalogoTramites.formatoPorDefecto
        val observacion = observacionDemo(formato)

        assertEquals(3, observacion.comentarios.size)
        assertEquals(2, observacion.documentosObservados.size)
        assertTrue(observacion.estaObservado(formato.documentosInclusos[0]))
        assertTrue(observacion.estaObservado(formato.documentosInclusos[1]))
    }

    @Test
    fun comentarioIncluyeRevisionDeFechaDelDocumento() {
        val formato = CatalogoTramites.formatoPorDefecto
        val observacion = observacionDemo(formato)

        val comentarioFecha = observacion.comentarios.firstOrNull { it.texto.contains("fecha") }
        assertTrue(comentarioFecha != null)
        assertEquals(formato.documentosInclusos[0].nombre, comentarioFecha?.documento)
    }

    @Test
    fun formatoConUnSoloDocumentoTieneDosComentarios() {
        val formato = CatalogoTramites.buscar("poder-simple")
        val observacion = observacionDemo(formato)

        assertEquals(2, observacion.comentarios.size)
        assertEquals(1, observacion.documentosObservados.size)
    }

    @Test
    fun documentosPropiosNoQuedanObservados() {
        val formato = CatalogoTramites.formatoPorDefecto
        val observacion = observacionDemo(formato)
        val propio = Documento(
            id = "nuevo",
            nombre = "anexo_v3.pdf",
            mimeType = "application/pdf",
            tamanoBytes = 1024L,
            uri = "content://demo/anexo_v3.pdf"
        )

        assertFalse(observacion.estaObservado(propio))
    }

    @Test
    fun todosLosComentariosTienenAutorMomentoYTexto() {
        val observacion = observacionDemo(CatalogoTramites.formatoPorDefecto)

        observacion.comentarios.forEach { comentario ->
            assertTrue(comentario.autor.isNotBlank())
            assertTrue(comentario.momento.isNotBlank())
            assertTrue(comentario.texto.isNotBlank())
        }
    }
}
