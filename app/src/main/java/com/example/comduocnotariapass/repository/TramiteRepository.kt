package com.example.comduocnotariapass.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.comduocnotariapass.model.CatalogoTramites
import com.example.comduocnotariapass.model.DatosSolicitante
import com.example.comduocnotariapass.model.Documento
import com.example.comduocnotariapass.model.EstadoPersistido
import com.example.comduocnotariapass.model.EstadoTramite
import com.example.comduocnotariapass.model.FormatoTramite
import com.example.comduocnotariapass.model.ResultadoRevision
import com.example.comduocnotariapass.model.RolSolicitante
import com.example.comduocnotariapass.model.TipoFirma
import com.example.comduocnotariapass.model.TramiteResumen
import com.example.comduocnotariapass.remote.XanoDataSource
import com.example.comduocnotariapass.remote.aFormato
import com.example.comduocnotariapass.remote.aResumen
import com.example.comduocnotariapass.repository.local.DocumentoTramiteEntity
import com.example.comduocnotariapass.repository.local.TramiteConDocumentos
import com.example.comduocnotariapass.repository.local.TramiteDao
import com.example.comduocnotariapass.repository.local.TramiteEntity
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class TramiteRepository(
    private val tramiteDao: TramiteDao,
    private val remote: XanoDataSource = XanoDataSource()
) {

    private val mutex = Mutex()

    private var usuarioActualId: Long? = null
    private var tramiteActualId: Long? = null
    private var tramiteRemotoId: Long? = null
    private var documentosEnviados: Set<String> = emptySet()
    private var codigoActual: String? = null
    private var fechaCreacionActual: Long? = null
    private var nombreUsuarioActual: String = "Persona Demo"
    private var idUsuarioActual: String = "ID-DEMO-001"

    var formatos by mutableStateOf(CatalogoTramites.formatos)
        private set

    var formatoSeleccionado by mutableStateOf(CatalogoTramites.formatoPorDefecto)
        private set

    var documentosPropios by mutableStateOf<List<Documento>>(emptyList())
        private set

    var datosSolicitante by mutableStateOf(DatosSolicitante())
        private set

    var antecedentesRevisados by mutableStateOf(false)
        private set

    var tipoFirmaSeleccionado by mutableStateOf(TipoFirma.NOTARIADA)
        private set

    var estadoTramite by mutableStateOf(EstadoTramite())
        private set

    var estadoPersistido by mutableStateOf(EstadoPersistido.BORRADOR)
        private set

    var tramites by mutableStateOf<List<TramiteResumen>>(emptyList())
        private set

    suspend fun iniciarSesion(usuario: UsuarioSesion) {
        usuarioActualId = usuario.id.takeIf { it > 0 }
        nombreUsuarioActual = usuario.nombre
        idUsuarioActual = if (usuario.id > 0) "USR-${usuario.id}" else "ID-DEMO-001"
        limpiarBorrador()
        cargarFormatosRemotos()
    }

    fun limpiarSesion() {
        usuarioActualId = null
        tramiteRemotoId = null
        documentosEnviados = emptySet()
        nombreUsuarioActual = "Persona Demo"
        idUsuarioActual = "ID-DEMO-001"
        tramites = emptyList()
        limpiarBorrador()
    }

    fun seleccionarFormato(formato: FormatoTramite) {
        formatoSeleccionado = formato
    }

    fun agregarDocumentoPropio(documento: Documento) {
        documentosPropios = documentosPropios + documento
    }

    fun quitarDocumentoPropio(id: String) {
        documentosPropios = documentosPropios.filterNot { it.id == id }
    }

    fun actualizarDatosSolicitante(nombre: String, id: String, rol: RolSolicitante) {
        datosSolicitante = datosSolicitante.copy(nombre = nombre, id = id, rol = rol)
    }

    fun marcarAntecedentesRevisados(valor: Boolean) {
        antecedentesRevisados = valor
    }

    fun limpiarBorrador() {
        formatoSeleccionado = formatos.firstOrNull { it.clave == "declaracion-jurada" }
            ?: formatos.firstOrNull { it.id == "declaracion-jurada" }
            ?: formatos.firstOrNull()
            ?: CatalogoTramites.formatoPorDefecto
        documentosPropios = emptyList()
        datosSolicitante = DatosSolicitante(nombre = nombreUsuarioActual, id = idUsuarioActual)
        antecedentesRevisados = false
        tipoFirmaSeleccionado = TipoFirma.NOTARIADA
        estadoTramite = EstadoTramite()
        estadoPersistido = EstadoPersistido.BORRADOR
        tramiteActualId = null
        tramiteRemotoId = null
        documentosEnviados = emptySet()
        codigoActual = null
        fechaCreacionActual = null
    }

    fun seleccionarTipoFirma(tipo: TipoFirma) {
        tipoFirmaSeleccionado = tipo
    }

    fun marcarTramiteObservado() {
        estadoTramite = EstadoTramite(
            id = codigoActual ?: estadoTramite.id,
            resultado = ResultadoRevision.OBSERVACIONES
        )
        estadoPersistido = EstadoPersistido.OBSERVACIONES
    }

    suspend fun guardarBorrador() = mutex.withLock {
        guardarBorradorInterno()
    }

    suspend fun cargarTramites() = mutex.withLock {
        actualizarListaInterno()
    }

    suspend fun cargarTramite(id: Long): TramiteResumen? = mutex.withLock {
        val usuarioId = usuarioActualId ?: return@withLock null
        val data = tramiteDao.obtenerPorId(id) ?: return@withLock null
        if (data.tramite.usuarioId != usuarioId) return@withLock null

        val detalle = runCatching { remote.detalle(id) }.getOrNull()
        aplicarTramite(data)
        detalle?.tramite?.let { dto ->
            tramiteRemotoId = dto.id.takeIf { valor -> valor > 0 } ?: tramiteRemotoId
            if (!dto.estado.isNullOrBlank()) {
                estadoPersistido = valorEnum(dto.estado, estadoPersistido)
            }
            dto.resultado?.let { valor ->
                estadoTramite = estadoTramite.copy(
                    resultado = runCatching { ResultadoRevision.valueOf(valor.uppercase()) }.getOrNull()
                )
            }
        }
        detalle?.tramite?.takeIf { !it.codigo.isNullOrBlank() }?.aResumen() ?: data.aResumen()
    }

    suspend fun enviarAlNotario() = mutex.withLock {
        estadoPersistido = EstadoPersistido.EN_REVISION
        estadoTramite = EstadoTramite(id = codigoActual ?: generarCodigo())
        guardarBorradorInterno()

        tramiteRemotoId?.let { id ->
            runCatching { remote.enviar(id, tipoFirmaSeleccionado.name) }
            runCatching {
                remote.notificacion(id, "El trámite ${codigoActual.orEmpty()} fue enviado al notario.")
            }
        }
        actualizarListaInterno()
    }

    suspend fun simularResolucion() = mutex.withLock {
        val estadoRemoto = tramiteRemotoId?.let { id -> runCatching { remote.estado(id) }.getOrNull() }
        val resultado = estadoRemoto?.resultado?.let { valor ->
            runCatching { ResultadoRevision.valueOf(valor.uppercase()) }.getOrNull()
        } ?: estadoTramite.siguienteResultado()

        estadoTramite = estadoTramite.copy(resultado = resultado)
        estadoPersistido = when (resultado) {
            ResultadoRevision.OBSERVACIONES -> EstadoPersistido.OBSERVACIONES
            ResultadoRevision.FINALIZADO -> EstadoPersistido.FINALIZADO
        }
        guardarBorradorInterno()
        tramiteRemotoId?.let { id ->
            runCatching { remote.resolucion(id, resultado.name) }
        }
    }

    suspend fun prepararReenvio() = mutex.withLock {
        estadoPersistido = EstadoPersistido.EN_REVISION
        estadoTramite = EstadoTramite(id = codigoActual ?: estadoTramite.id)
        guardarBorradorInterno()
    }

    suspend fun sembrarDemo() = mutex.withLock {
        val usuarioId = usuarioActualId ?: return@withLock
        if (tramiteDao.listarPorUsuario(usuarioId).isNotEmpty()) {
            actualizarListaInterno()
            return@withLock
        }

        val ahora = System.currentTimeMillis()
        val dia = 24L * 60L * 60L * 1000L
        val demos = listOf(
            demoEntity(
                usuarioId = usuarioId,
                formatoId = "declaracion-jurada",
                codigo = "NX-DEMO-024",
                fechaCreacion = ahora - dia,
                estado = EstadoPersistido.BORRADOR,
                resultado = null
            ),
            demoEntity(
                usuarioId = usuarioId,
                formatoId = "autorizacion-viaje",
                codigo = "NX-DEMO-023",
                fechaCreacion = ahora - 2 * dia,
                estado = EstadoPersistido.EN_REVISION,
                resultado = null
            ),
            demoEntity(
                usuarioId = usuarioId,
                formatoId = "poder-simple",
                codigo = "NX-DEMO-022",
                fechaCreacion = ahora - 3 * dia,
                estado = EstadoPersistido.FINALIZADO,
                resultado = ResultadoRevision.FINALIZADO
            )
        )

        demos.forEach { tramiteDao.guardar(it, emptyList()) }
        actualizarListaInterno()
    }

    private suspend fun guardarBorradorInterno() {
        val usuarioId = usuarioActualId ?: return

        val fecha = fechaCreacionActual ?: System.currentTimeMillis()
        var codigo = codigoActual ?: generarCodigo()
        fechaCreacionActual = fecha
        codigoActual = codigo

        if (tramiteActualId == null) {
            val creado = runCatching {
                remote.crear(
                    usuarioId = usuarioId,
                    formatoId = formatoSeleccionado.id,
                    solicitanteNombre = datosSolicitante.nombre,
                    solicitanteId = datosSolicitante.id,
                    solicitanteRol = datosSolicitante.rol.name
                )
            }.getOrNull()
            if (creado != null) {
                tramiteRemotoId = creado.id.takeIf { it > 0 }
                if (creado.codigo.isNotBlank()) {
                    codigo = creado.codigo
                    codigoActual = codigo
                }
            }
        }

        val entity = TramiteEntity(
            id = tramiteActualId ?: 0,
            usuarioId = usuarioId,
            codigo = codigo,
            formatoId = formatoSeleccionado.id,
            formatoNombre = formatoSeleccionado.nombre,
            fechaCreacion = fecha,
            estado = estadoPersistido.name,
            resultado = estadoTramite.resultado?.name,
            tipoFirma = tipoFirmaSeleccionado.name,
            solicitanteNombre = datosSolicitante.nombre,
            solicitanteId = datosSolicitante.id,
            solicitanteRol = datosSolicitante.rol.name
        )

        val documentos = documentosPropios.map { documento ->
            DocumentoTramiteEntity(
                tramiteId = tramiteActualId ?: 0,
                nombre = documento.nombre,
                mimeType = documento.mimeType,
                tamanoBytes = documento.tamanoBytes,
                uri = documento.uri
            )
        }

        val id = tramiteDao.guardar(entity, documentos)
        tramiteActualId = id
        if (estadoTramite.id != codigo) {
            estadoTramite = estadoTramite.copy(id = codigo)
        }

        tramiteRemotoId?.let { idRemoto ->
            documentosPropios.filter { it.uri != null && it.id !in documentosEnviados }
                .forEach { documento ->
                    val enviado = runCatching {
                        remote.documento(
                            idRemoto,
                            documento.nombre,
                            documento.mimeType,
                            documento.tamanoBytes
                        )
                    }.isSuccess
                    if (enviado) documentosEnviados = documentosEnviados + documento.id
                }
        }
        actualizarListaInterno()
    }

    private suspend fun actualizarListaInterno() {
        val usuarioId = usuarioActualId
        if (usuarioId == null) {
            tramites = emptyList()
            return
        }

        val remotos = runCatching { remote.tramites(usuarioId) }.getOrNull().orEmpty()
        remotos.forEach { dto ->
            if (dto.codigo.isNullOrBlank()) return@forEach
            val local = tramiteDao.obtenerPorCodigo(dto.codigo)
            tramiteDao.guardar(
                tramite = TramiteEntity(
                    id = local?.tramite?.id ?: 0,
                    usuarioId = usuarioId,
                    codigo = dto.codigo,
                    formatoId = dto.formatoId.orEmpty().ifBlank {
                        local?.tramite?.formatoId ?: formatoSeleccionado.id
                    },
                    formatoNombre = dto.formatoNombre.orEmpty().ifBlank {
                        local?.tramite?.formatoNombre ?: formatoSeleccionado.nombre
                    },
                    fechaCreacion = dto.fechaCreacion.takeIf { it > 0 }
                        ?: local?.tramite?.fechaCreacion
                        ?: System.currentTimeMillis(),
                    estado = dto.estado.orEmpty().ifBlank { local?.tramite?.estado ?: "BORRADOR" },
                    resultado = dto.resultado?.takeIf { it.isNotBlank() }
                        ?: local?.tramite?.resultado,
                    tipoFirma = dto.tipoFirma.orEmpty().ifBlank {
                        local?.tramite?.tipoFirma ?: TipoFirma.NOTARIADA.name
                    },
                    solicitanteNombre = dto.solicitanteNombre.orEmpty().ifBlank {
                        local?.tramite?.solicitanteNombre ?: nombreUsuarioActual
                    },
                    solicitanteId = dto.solicitanteId.orEmpty().ifBlank {
                        local?.tramite?.solicitanteId ?: idUsuarioActual
                    },
                    solicitanteRol = dto.solicitanteRol.orEmpty().ifBlank {
                        local?.tramite?.solicitanteRol ?: RolSolicitante.PERSONA_NATURAL.name
                    }
                ),
                documentos = local?.documentos.orEmpty()
            )
        }

        tramites = tramiteDao.listarPorUsuario(usuarioId).map { it.aResumen() }
    }

    private suspend fun cargarFormatosRemotos() {
        val remotos = runCatching { remote.formatos() }.getOrNull().orEmpty()
        val convertidos = remotos
            .filter { !it.id.isNullOrBlank() && !it.nombre.isNullOrBlank() }
            .map { it.aFormato() }
        if (convertidos.isNotEmpty()) {
            formatos = convertidos
            formatoSeleccionado = formatos.firstOrNull { it.id == formatoSeleccionado.id }
                ?: formatos.first()
        }
    }

    private fun aplicarTramite(data: TramiteConDocumentos) {
        val tramite = data.tramite
        formatoSeleccionado = buscarFormato(tramite.formatoId)
        documentosPropios = data.documentos.map { documento ->
            Documento(
                id = documento.id.toString(),
                nombre = documento.nombre,
                mimeType = documento.mimeType,
                tamanoBytes = documento.tamanoBytes,
                uri = documento.uri
            )
        }
        datosSolicitante = DatosSolicitante(
            nombre = tramite.solicitanteNombre,
            id = tramite.solicitanteId,
            rol = valorEnum(tramite.solicitanteRol, RolSolicitante.PERSONA_NATURAL)
        )
        tipoFirmaSeleccionado = valorEnum(tramite.tipoFirma, TipoFirma.NOTARIADA)
        estadoPersistido = valorEnum(tramite.estado, EstadoPersistido.BORRADOR)
        estadoTramite = EstadoTramite(
            id = tramite.codigo,
            resultado = tramite.resultado?.let { valor ->
                runCatching { ResultadoRevision.valueOf(valor.uppercase()) }.getOrNull()
            }
        )
        antecedentesRevisados = true
        tramiteActualId = tramite.id
        codigoActual = tramite.codigo
        fechaCreacionActual = tramite.fechaCreacion
    }

    private fun TramiteConDocumentos.aResumen(): TramiteResumen = TramiteResumen(
        id = tramite.id,
        titulo = tramite.formatoNombre,
        codigo = tramite.codigo,
        fechaCreacion = tramite.fechaCreacion,
        estado = valorEnum(tramite.estado, EstadoPersistido.BORRADOR),
        resultado = tramite.resultado?.let { valor ->
            runCatching { ResultadoRevision.valueOf(valor.uppercase()) }.getOrNull()
        }
    )

    private fun buscarFormato(id: String): FormatoTramite =
        formatos.firstOrNull { it.id == id } ?: CatalogoTramites.buscar(id)

    private fun demoEntity(
        usuarioId: Long,
        formatoId: String,
        codigo: String,
        fechaCreacion: Long,
        estado: EstadoPersistido,
        resultado: ResultadoRevision?
    ): TramiteEntity {
        val formato = buscarFormato(formatoId)
        return TramiteEntity(
            usuarioId = usuarioId,
            codigo = codigo,
            formatoId = formato.id,
            formatoNombre = formato.nombre,
            fechaCreacion = fechaCreacion,
            estado = estado.name,
            resultado = resultado?.name,
            tipoFirma = TipoFirma.NOTARIADA.name,
            solicitanteNombre = nombreUsuarioActual,
            solicitanteId = idUsuarioActual,
            solicitanteRol = RolSolicitante.PERSONA_NATURAL.name
        )
    }

    private fun generarCodigo(): String =
        "NX-${System.currentTimeMillis().toString().takeLast(6)}"

    private inline fun <reified T : Enum<T>> valorEnum(valor: String?, porDefecto: T): T =
        runCatching { enumValueOf<T>(valor.orEmpty().uppercase()) }.getOrDefault(porDefecto)
}
