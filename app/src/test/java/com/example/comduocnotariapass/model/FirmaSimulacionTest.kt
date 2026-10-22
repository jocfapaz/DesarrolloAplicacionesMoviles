package com.example.comduocnotariapass.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FirmaSimulacionTest {

    @Test
    fun existenTresTiposDeFirma() {
        assertEquals(3, TipoFirma.entries.size)
        assertEquals(
            listOf(TipoFirma.SIMPLE, TipoFirma.AVANZADA, TipoFirma.NOTARIADA),
            TipoFirma.entries
        )
    }

    @Test
    fun firmaSimpleSimulaSoloEToken() {
        assertTrue(TipoFirma.SIMPLE.simula(MecanismoSimulado.E_TOKEN))
        assertFalse(TipoFirma.SIMPLE.simula(MecanismoSimulado.CERTIFICADO))
        assertFalse(TipoFirma.SIMPLE.simula(MecanismoSimulado.BIOMETRIA))
    }

    @Test
    fun firmaAvanzadaSimulaSoloCertificado() {
        assertFalse(TipoFirma.AVANZADA.simula(MecanismoSimulado.E_TOKEN))
        assertTrue(TipoFirma.AVANZADA.simula(MecanismoSimulado.CERTIFICADO))
        assertFalse(TipoFirma.AVANZADA.simula(MecanismoSimulado.BIOMETRIA))
    }

    @Test
    fun firmaNotariadaSimulaCertificadoYBiometria() {
        assertFalse(TipoFirma.NOTARIADA.simula(MecanismoSimulado.E_TOKEN))
        assertTrue(TipoFirma.NOTARIADA.simula(MecanismoSimulado.CERTIFICADO))
        assertTrue(TipoFirma.NOTARIADA.simula(MecanismoSimulado.BIOMETRIA))
    }

    @Test
    fun todosLosMecanismosTienenTextosAcademicos() {
        MecanismoSimulado.entries.forEach { mecanismo ->
            assertTrue(mecanismo.etiqueta.isNotBlank())
            assertTrue(mecanismo.titulo.isNotBlank())
            assertTrue(mecanismo.descripcion.isNotBlank())
        }
    }

    @Test
    fun cadaTipoDeFirmaTieneNombreSubtituloYMecanismos() {
        TipoFirma.entries.forEach { tipo ->
            assertTrue(tipo.nombre.isNotBlank())
            assertTrue(tipo.subtitulo.isNotBlank())
            assertTrue(tipo.mecanismos.isNotEmpty())
        }
    }
}
