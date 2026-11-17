package com.example.comduocnotariapass.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class EstadoTramiteTest {

    @Test
    fun estadoInicial_enRevision() {
        println("INICIO: verificar estado inicial en revisión")
        val estado = EstadoTramite()

        assertEquals(3, estado.indiceActual)
        assertEquals("En revisión", estado.etiquetaEstado)
        assertEquals("Simular observación del notario", estado.textoBotonDemo())
        assertEquals(ResultadoRevision.OBSERVACIONES, estado.siguienteResultado())
        assertTrue(estado.esActual(PasoProgreso.EN_REVISION))
        assertFalse(estado.esTocable(PasoProgreso.RESULTADO))
        assertEquals("Pendiente", estado.momentoDe(PasoProgreso.RESULTADO))

        println("OK: indiceActual=3, etiqueta=En revisión, botón=Simular observación del notario")
    }

    @Test
    fun cambioDeResultado_actualizaProgresoYBoton() {
        println("INICIO: verificar cambio de resultado del notario")
        val observado = EstadoTramite(resultado = ResultadoRevision.OBSERVACIONES)

        assertEquals(4, observado.indiceActual)
        assertEquals("Con observaciones", observado.etiquetaEstado)
        assertTrue(observado.estaCompletado(PasoProgreso.EN_REVISION))
        assertTrue(observado.esTocable(PasoProgreso.RESULTADO))
        assertEquals("Simular finalización", observado.textoBotonDemo())
        assertEquals(ResultadoRevision.FINALIZADO, observado.siguienteResultado())

        val finalizado = EstadoTramite(resultado = ResultadoRevision.FINALIZADO)
        assertEquals("Simular observación", finalizado.textoBotonDemo())
        assertEquals("Hoy · 09:35", finalizado.momentoDe(PasoProgreso.RESULTADO))
        assertEquals(ResultadoRevision.OBSERVACIONES, finalizado.siguienteResultado())

        println("OK: observaciones avanzan al paso 4 y finalizado cicla a observaciones")
    }
}
