package com.example.comduocnotariapass.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class ReglasDeNegocioModelTest {

    @Test
    fun reglasDeContinuacionYFirma() {
        println("INICIO: verificar reglas de continuación y mecanismos de firma")
        assertTrue(puedeContinuarVerificacion(DatosSolicitante(), true))
        assertFalse(puedeContinuarVerificacion(DatosSolicitante(), false))
        assertFalse(puedeContinuarVerificacion(DatosSolicitante(nombre = "  ", id = "X"), true))

        assertTrue(TipoFirma.NOTARIADA.simula(MecanismoSimulado.BIOMETRIA))
        assertFalse(TipoFirma.NOTARIADA.simula(MecanismoSimulado.E_TOKEN))
        assertEquals(listOf(MecanismoSimulado.E_TOKEN), TipoFirma.SIMPLE.mecanismos)

        assertSame(CatalogoTramites.formatoPorDefecto, CatalogoTramites.buscar("no-existe"))
        assertEquals(2, CatalogoTramites.buscar("declaracion-jurada").documentosInclusos.size)
        assertTrue(
            Documento(
                id = "1",
                nombre = "a.pdf",
                mimeType = "application/pdf",
                tamanoBytes = 1L,
                uri = "content://x"
            ).esPropio
        )

        println("OK: continuación requiere datos y revisión; NOTARIADA simula certificado y biometría")
    }
}
