package com.example.comduocnotariapass.remote

import com.example.comduocnotariapass.model.EstadoPersistido
import com.example.comduocnotariapass.model.ResultadoRevision
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Test

class DtoMappingTest {

    @Test
    fun mapeoValido_yFallbackDeEstados() {
        println("INICIO: mapear DTO de trámite con estados válidos e inválidos")
        val valido = TramiteDto(
            id = 7L,
            codigo = "NX-1",
            formatoNombre = "Poder simple",
            fechaCreacion = 100L,
            estado = "FINALIZADO",
            resultado = "FINALIZADO"
        ).aResumen()

        assertEquals(7L, valido.id)
        assertEquals("Poder simple", valido.titulo)
        assertEquals("NX-1", valido.codigo)
        assertEquals(100L, valido.fechaCreacion)
        assertEquals(EstadoPersistido.FINALIZADO, valido.estado)
        assertEquals(ResultadoRevision.FINALIZADO, valido.resultado)

        val invalido = TramiteDto(estado = "FK-LADO", resultado = "FK-LADO").aResumen()
        assertEquals(EstadoPersistido.BORRADOR, invalido.estado)
        assertNull(invalido.resultado)

        println("OK: estados válidos mapeados y valores desconocidos caen a BORRADOR/null")
    }

    @Test
    fun mapeoFormato_documentosInclusos() {
        println("INICIO: mapear formato con documentos incluidos")
        val formato = FormatoDto(
            id = "x",
            nombre = "Formato de prueba",
            documentosInclusos = listOf(
                DocumentoDto(
                    id = 5L,
                    nombre = "a.pdf",
                    mimeType = "application/pdf",
                    tamanoBytes = 10L
                )
            )
        ).aFormato()

        assertEquals("x", formato.id)
        assertEquals("Formato de prueba", formato.nombre)
        val documento = formato.documentosInclusos.single()
        assertEquals("5", documento.id)
        assertEquals("a.pdf", documento.nombre)
        assertEquals("application/pdf", documento.mimeType)
        assertEquals(10L, documento.tamanoBytes)
        assertFalse(documento.esPropio)

        println("OK: DocumentoDto.id=5 se convirtió en id=5 y esPropio=false")
    }
}
