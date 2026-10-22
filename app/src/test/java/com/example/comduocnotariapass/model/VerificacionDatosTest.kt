package com.example.comduocnotariapass.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class VerificacionDatosTest {

    @Test
    fun datosPorDefectoEstanCompletos() {
        val datos = DatosSolicitante()
        assertTrue(datos.datosCompletos)
        assertEquals(RolSolicitante.PERSONA_NATURAL, datos.rol)
    }

    @Test
    fun nombreVacioIncompletaElFormulario() {
        val datos = DatosSolicitante(nombre = "   ")
        assertFalse(datos.nombreCompleto)
        assertFalse(datos.datosCompletos)
        assertTrue(datos.idCompleto)
    }

    @Test
    fun idVacioIncompletaElFormulario() {
        val datos = DatosSolicitante(id = "")
        assertTrue(datos.nombreCompleto)
        assertFalse(datos.idCompleto)
        assertFalse(datos.datosCompletos)
    }

    @Test
    fun continuarRequiereCamposCompletosYCheckbox() {
        val datos = DatosSolicitante()

        assertTrue(puedeContinuarVerificacion(datos, antecedentesRevisados = true))
        assertFalse(puedeContinuarVerificacion(datos, antecedentesRevisados = false))
        assertFalse(
            puedeContinuarVerificacion(
                datos.copy(nombre = ""),
                antecedentesRevisados = true
            )
        )
        assertFalse(
            puedeContinuarVerificacion(
                datos.copy(id = ""),
                antecedentesRevisados = true
            )
        )
    }

    @Test
    fun rolesTienenNombreLegible() {
        assertEquals("Persona natural", RolSolicitante.PERSONA_NATURAL.nombre)
        assertEquals("Apoderado", RolSolicitante.APODERADO.nombre)
        assertEquals("Mandatario", RolSolicitante.MANDATARIO.nombre)
        assertEquals("Representante legal", RolSolicitante.REPRESENTANTE_LEGAL.nombre)
        assertEquals("Persona jurídica", RolSolicitante.PERSONA_JURIDICA.nombre)
    }
}
