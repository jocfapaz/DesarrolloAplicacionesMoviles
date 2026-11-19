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
        val crear = gson.toJson(CrearTramiteRequest(1L, "2", "Juan", "111", "Titular"))
        assertTrue(crear.contains("\"usuario_id\""))
        assertTrue(crear.contains("\"formato_id\""))
        assertTrue(crear.contains("\"solicitante_nombre\""))
        assertFalse(crear.contains("usuarioId"))

        val documento = gson.toJson(DocumentoRequest("a.pdf", "application/pdf", 10L))
        assertTrue(documento.contains("\"mime_type\""))
        assertTrue(documento.contains("\"tamano_bytes\""))

        val envio = gson.toJson(EnviarRequest("NOTARIADA"))
        assertTrue(envio.contains("\"tipo_firma\""))

        println("OK: requests serializados con nombres snake_case")
    }
}
