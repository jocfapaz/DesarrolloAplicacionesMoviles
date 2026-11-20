package com.example.comduocnotariapass.remote

import com.example.comduocnotariapass.model.EstadoPersistido
import com.example.comduocnotariapass.model.FormatoTramite
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class XanoPayloadTest {

    private val gson = Gson()

    @Test
    fun formatosReales_conDocumentosNulos_noLanzanExcepcion() {
        val json = """
            [
              {
                "id": 2,
                "nombre": "Declaración Jurada",
                "descripcion": "Declaración jurada de domicilio",
                "clave": "declaracion-jurada",
                "activo": true,
                "documentos_inclusos": null
              },
              {
                "id": 3,
                "nombre": "Autorización de Viaje",
                "descripcion": "Permiso notarial para que menores viajen al extranjero.",
                "clave": "autorizacion-viaje",
                "activo": true,
                "documentos_inclusos": null
              },
              {
                "id": 1,
                "nombre": "Poder simple",
                "descripcion": "Documento para delegar representación en trámites básicos.",
                "clave": "poder-simple",
                "activo": true,
                "documentos_inclusos": [
                  {
                    "nombre": "formato_poder_simple.pdf",
                    "mime_type": "application/pdf",
                    "tamano_bytes": 184320
                  }
                ]
              }
            ]
        """.trimIndent()

        val tipo = object : TypeToken<List<FormatoDto>>() {}.type
        val formatos: List<FormatoDto> = gson.fromJson(json, tipo)
        val convertidos: List<FormatoTramite> = formatos.map { it.aFormato() }

        assertEquals(3, convertidos.size)
        assertTrue(convertidos[0].documentosInclusos.isEmpty())
        assertTrue(convertidos[1].documentosInclusos.isEmpty())
        assertEquals("1", convertidos[2].id)
        assertEquals("poder-simple", convertidos[2].clave)
        val documento = convertidos[2].documentosInclusos.single()
        assertEquals("formato_poder_simple.pdf", documento.nombre)
        assertEquals("application/pdf", documento.mimeType)
        assertEquals(184320L, documento.tamanoBytes)

        println("OK: formatos reales de Xano mapeados sin NPE")
    }

    @Test
    fun tramitesReales_normalizanEstadoYResultado() {
        val json = """
            [
              {
                "id": 1,
                "code": "TRM-001",
                "usuario_id": 1,
                "formato_id": "1",
                "formato_nombre": "Poder Simple",
                "fecha_creacion": 1700000000000,
                "estado": "Borrador",
                "resultado": "",
                "tipo_firma": "SIMPLE",
                "solicitante_nombre": "Juan Pérez",
                "solicitante_id": "111111111111-1",
                "solicitante_rol": "Titular",
                "notaria_id": 1,
                "observacion": null
              },
              {
                "id": 2,
                "code": "TRM-002",
                "usuario_id": 1,
                "formato_id": "2",
                "formato_nombre": "Declaración Jurada",
                "fecha_creacion": 1700086400000,
                "estado": "EN_REVISION",
                "resultado": "",
                "tipo_firma": "AVANZADA",
                "solicitante_nombre": "Ana Soto",
                "solicitante_id": "22222222-2",
                "solicitante_rol": "Titular",
                "notaria_id": 1,
                "observacion": null
              },
              {
                "id": 3,
                "code": "TRM-003",
                "usuario_id": 1,
                "formato_id": "3",
                "formato_nombre": "Autorización de Viaje",
                "fecha_creacion": 1700172800000,
                "estado": "FINALIZADO",
                "resultado": "Aprobado",
                "tipo_firma": "NOTARIADA",
                "solicitante_nombre": "Carlos Gómez",
                "solicitante_id": "33333333-3",
                "solicitante_rol": "Representante",
                "notaria_id": 2,
                "observacion": null
              }
            ]
        """.trimIndent()

        val tipo = object : TypeToken<List<TramiteDto>>() {}.type
        val tramites: List<TramiteDto> = gson.fromJson(json, tipo)
        val resumenes = tramites.map { it.aResumen() }

        assertEquals(3, resumenes.size)
        assertEquals("TRM-001", resumenes[0].codigo)
        assertEquals("Poder Simple", resumenes[0].titulo)
        assertEquals(1700000000000L, resumenes[0].fechaCreacion)
        assertEquals(EstadoPersistido.BORRADOR, resumenes[0].estado)
        assertNull(resumenes[0].resultado)
        assertEquals(EstadoPersistido.EN_REVISION, resumenes[1].estado)
        assertEquals(EstadoPersistido.FINALIZADO, resumenes[2].estado)
        assertNull(resumenes[2].resultado)

        println("OK: tramites reales de Xano mapeados y estados normalizados")
    }

    @Test
    fun requestsSeSerializanEnSnakeCase() {
        val crear = gson.toJson(
            CrearTramiteRequest(
                codigo = "NX-123456",
                usuarioId = 1L,
                formatoId = "2",
                formatoNombre = "Declaración Jurada",
                fechaCreacion = 1700000000000L,
                estado = "BORRADOR",
                resultado = "",
                tipoFirma = "NOTARIADA",
                solicitanteNombre = "Juan",
                solicitanteId = "111",
                solicitanteRol = "PERSONA_NATURAL"
            )
        )
        assertTrue(crear.contains("\"code\":\"NX-123456\""))
        assertTrue(crear.contains("\"usuario_id\":1"))
        assertTrue(crear.contains("\"formato_id\":\"2\""))
        assertTrue(crear.contains("\"formato_nombre\":\"Declaración Jurada\""))
        assertTrue(crear.contains("\"fecha_creacion\":1700000000000"))
        assertTrue(crear.contains("\"estado\":\"BORRADOR\""))
        assertTrue(crear.contains("\"resultado\":\"\""))
        assertTrue(crear.contains("\"tipo_firma\":\"NOTARIADA\""))
        assertTrue(crear.contains("\"solicitante_nombre\":\"Juan\""))
        assertTrue(crear.contains("\"solicitante_id\":\"111\""))
        assertTrue(crear.contains("\"solicitante_rol\":\"PERSONA_NATURAL\""))
        assertFalse(crear.contains("usuarioId"))
        assertFalse(crear.contains("fechaCreacion"))

        val documento = gson.toJson(DocumentoRequest("a.pdf", "application/pdf", 10L))
        assertTrue(documento.contains("\"mime_type\""))
        assertTrue(documento.contains("\"tamano_bytes\""))

        val envio = gson.toJson(
            EnviarRequest(
                tipoFirma = "NOTARIADA",
                codigo = "NX-123456",
                estado = "EN_REVISION",
                fechaCreacion = 1700000000000L
            )
        )
        assertTrue(envio.contains("\"tipo_firma\":\"NOTARIADA\""))
        assertTrue(envio.contains("\"code\":\"NX-123456\""))
        assertTrue(envio.contains("\"estado\":\"EN_REVISION\""))
        assertTrue(envio.contains("\"fecha_creacion\":1700000000000"))

        println("OK: requests serializados con nombres snake_case")
    }

    @Test
    fun resolucionIncluyeTodosLosCamposDelTramite() {
        val json = gson.toJson(
            ResolucionRequest(
                resultado = "FINALIZADO",
                comentario = "Aprobado",
                codigo = "NX-654321",
                formatoId = "3",
                formatoNombre = "Autorización de Viaje",
                fechaCreacion = 1700000000000L,
                estado = "FINALIZADO",
                tipoFirma = "AVANZADA",
                solicitanteNombre = "Ana",
                solicitanteId = "USR-4",
                solicitanteRol = "PERSONA_NATURAL"
            )
        )

        assertTrue(json.contains("\"resultado\":\"FINALIZADO\""))
        assertTrue(json.contains("\"code\":\"NX-654321\""))
        assertTrue(json.contains("\"fecha_creacion\":1700000000000"))
        assertTrue(json.contains("\"estado\":\"FINALIZADO\""))
        assertTrue(json.contains("\"tipo_firma\":\"AVANZADA\""))
        assertTrue(json.contains("\"solicitante_id\":\"USR-4\""))
        assertFalse(json.contains("solicitanteId"))

        println("OK: resolución serializada con todos los campos del trámite")
    }

    @Test
    fun patchIncluyeTodosLosCamposYResultadoVacio() {
        val json = gson.toJson(
            ActualizarTramiteRequest(
                codigo = "NX-777888",
                usuarioId = 4L,
                formatoId = "2",
                formatoNombre = "Declaración Jurada",
                fechaCreacion = 1700000000000L,
                estado = "EN_REVISION",
                resultado = "",
                tipoFirma = "AVANZADA",
                solicitanteNombre = "Isidora",
                solicitanteId = "USR-CAMBIADO",
                solicitanteRol = "APODERADO"
            )
        )

        assertTrue(json.contains("\"code\":\"NX-777888\""))
        assertTrue(json.contains("\"usuario_id\":4"))
        assertTrue(json.contains("\"formato_id\":\"2\""))
        assertTrue(json.contains("\"formato_nombre\":\"Declaración Jurada\""))
        assertTrue(json.contains("\"fecha_creacion\":1700000000000"))
        assertTrue(json.contains("\"estado\":\"EN_REVISION\""))
        assertTrue(json.contains("\"resultado\":\"\""))
        assertTrue(json.contains("\"tipo_firma\":\"AVANZADA\""))
        assertTrue(json.contains("\"solicitante_nombre\":\"Isidora\""))
        assertTrue(json.contains("\"solicitante_id\":\"USR-CAMBIADO\""))
        assertTrue(json.contains("\"solicitante_rol\":\"APODERADO\""))
        assertFalse(json.contains("solicitanteId"))
        assertFalse(json.contains("tramite_id"))

        println("OK: PATCH serializado con todos los campos y resultado explícito")
    }
}
