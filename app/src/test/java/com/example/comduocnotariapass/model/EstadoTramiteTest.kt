package com.example.comduocnotariapass.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class EstadoTramiteTest {

    @Test
    fun estadoPorDefectoSigueEnRevision() {
        val estado = EstadoTramite()

        assertEquals("TRX-2048", estado.id)
        assertEquals(null, estado.resultado)
        assertEquals("En revisión", estado.etiquetaEstado)
        assertTrue(estado.esActual(PasoProgreso.EN_REVISION))
        assertEquals(PasoProgreso.EN_REVISION.ordinal, estado.indiceActual)
    }

    @Test
    fun pasosAnterioresEstanCompletadosYPendientesNoSonTocables() {
        val estado = EstadoTramite()

        assertTrue(estado.estaCompletado(PasoProgreso.DOCUMENTO_CARGADO))
        assertTrue(estado.estaCompletado(PasoProgreso.ANTECEDENTES_VERIFICADOS))
        assertTrue(estado.estaCompletado(PasoProgreso.ENVIADO_AL_NOTARIO))
        assertFalse(estado.estaCompletado(PasoProgreso.EN_REVISION))
        assertFalse(estado.estaCompletado(PasoProgreso.RESULTADO))

        assertTrue(estado.esTocable(PasoProgreso.EN_REVISION))
        assertFalse(estado.esTocable(PasoProgreso.RESULTADO))
    }

    @Test
    fun simularResolucionAvanzaAlUltimoPasoConObservaciones() {
        val estado = EstadoTramite().copy(resultado = ResultadoRevision.OBSERVACIONES)

        assertEquals(PasoProgreso.RESULTADO.ordinal, estado.indiceActual)
        assertTrue(estado.esActual(PasoProgreso.RESULTADO))
        assertTrue(estado.estaCompletado(PasoProgreso.EN_REVISION))
        assertTrue(estado.esTocable(PasoProgreso.RESULTADO))
        assertEquals("Con observaciones", estado.etiquetaEstado)
        assertEquals("Observaciones", estado.tituloDe(PasoProgreso.RESULTADO))
    }

    @Test
    fun finalizadoCambiaElTituloDelUltimoPaso() {
        val estado = EstadoTramite().copy(resultado = ResultadoRevision.FINALIZADO)

        assertEquals("Finalizado", estado.etiquetaEstado)
        assertEquals("Finalizado", estado.tituloDe(PasoProgreso.RESULTADO))
        assertTrue(estado.esActual(PasoProgreso.RESULTADO))
    }

    @Test
    fun botonDemoAlternaEntreObservacionesYFinalizado() {
        val inicial = EstadoTramite()
        assertEquals("Simular observación del notario", inicial.textoBotonDemo())
        assertEquals(ResultadoRevision.OBSERVACIONES, inicial.siguienteResultado())

        val conObservaciones = inicial.copy(resultado = ResultadoRevision.OBSERVACIONES)
        assertEquals("Simular finalización", conObservaciones.textoBotonDemo())
        assertEquals(ResultadoRevision.FINALIZADO, conObservaciones.siguienteResultado())

        val finalizado = inicial.copy(resultado = ResultadoRevision.FINALIZADO)
        assertEquals("Simular observación", finalizado.textoBotonDemo())
        assertEquals(ResultadoRevision.OBSERVACIONES, finalizado.siguienteResultado())
    }

    @Test
    fun cadaPasoExponeTextoYDetalle() {
        PasoProgreso.entries.forEach { paso ->
            assertTrue(paso.titulo.isNotBlank())
            assertTrue(paso.descripcion.isNotBlank())
            assertTrue(paso.responsable.isNotBlank())
            assertTrue(paso.momento.isNotBlank())
        }
        assertEquals(5, PasoProgreso.entries.size)
    }
}
