package com.example.comduocnotariapass.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.comduocnotariapass.model.DatosSolicitante
import com.example.comduocnotariapass.model.Documento
import com.example.comduocnotariapass.model.EstadoPersistido
import com.example.comduocnotariapass.model.EstadoTramite
import com.example.comduocnotariapass.model.FormatoTramite
import com.example.comduocnotariapass.model.Pantalla
import com.example.comduocnotariapass.model.RolSolicitante
import com.example.comduocnotariapass.model.TipoFirma
import com.example.comduocnotariapass.model.TramiteResumen
import com.example.comduocnotariapass.repository.AuthRepository
import com.example.comduocnotariapass.repository.ResultadoAuth
import com.example.comduocnotariapass.repository.TramiteRepository
import com.example.comduocnotariapass.repository.UsuarioSesion
import com.example.comduocnotariapass.repository.local.NotariaDatabase
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = NotariaDatabase.obtener(application)
    private val authRepository = AuthRepository(
        usuarioDao = database.usuarioDao(),
        sesionDao = database.sesionDao()
    )
    private val tramiteRepository = TramiteRepository(database.tramiteDao())

    // Historial de pantallas visitadas (tipo navegador)
    private val historial = mutableStateListOf(Pantalla.LOGIN)
    private var indice by mutableStateOf(0)

    var cargandoSesion by mutableStateOf(true)
        private set

    var cargandoLogin by mutableStateOf(false)
        private set

    var errorLogin by mutableStateOf<String?>(null)
        private set

    var cargandoRegistro by mutableStateOf(false)
        private set

    var errorRegistro by mutableStateOf<String?>(null)
        private set

    var usuarioActual by mutableStateOf<UsuarioSesion?>(null)
        private set

    val nombreUsuario: String
        get() = usuarioActual?.nombre ?: "Persona Demo"

    // Estado del trámite administrado por el repository
    val formatoSeleccionado: FormatoTramite
        get() = tramiteRepository.formatoSeleccionado
    val documentosPropios: List<Documento>
        get() = tramiteRepository.documentosPropios
    val datosSolicitante: DatosSolicitante
        get() = tramiteRepository.datosSolicitante
    val antecedentesRevisados: Boolean
        get() = tramiteRepository.antecedentesRevisados
    val tipoFirmaSeleccionado: TipoFirma
        get() = tramiteRepository.tipoFirmaSeleccionado
    val estadoTramite: EstadoTramite
        get() = tramiteRepository.estadoTramite
    val tramites: List<TramiteResumen>
        get() = tramiteRepository.tramites

    // Estado que indica qué pantalla se muestra actualmente
    val pantallaActual: Pantalla get() = historial[indice]

    // Estado de los botones de navegación en las esquinas
    val puedeVolver: Boolean get() = indice > 0
    val puedeAvanzar: Boolean get() = indice < historial.lastIndex

    init {
        viewModelScope.launch {
            try {
                val usuario = authRepository.sesionActiva()
                if (usuario != null) {
                    iniciarSesionInterna(usuario)
                }
            } finally {
                cargandoSesion = false
            }
        }
    }

    private fun navegarA(pantalla: Pantalla) {
        while (historial.lastIndex > indice) {
            historial.removeAt(historial.lastIndex)
        }
        if (pantallaActual != pantalla) {
            historial.add(pantalla)
            indice = historial.lastIndex
        }
    }

    private fun reiniciarNavegacion(pantalla: Pantalla) {
        historial.clear()
        historial.add(pantalla)
        indice = 0
    }

    private suspend fun iniciarSesionInterna(
        usuario: UsuarioSesion,
        sembrarDemo: Boolean = false
    ) {
        usuarioActual = usuario
        tramiteRepository.iniciarSesion(usuario)
        if (sembrarDemo) {
            tramiteRepository.sembrarDemo()
        }
        tramiteRepository.cargarTramites()
        reiniciarNavegacion(Pantalla.MIS_TRAMITES)
    }

    private fun persistirBorrador() {
        viewModelScope.launch {
            tramiteRepository.guardarBorrador()
        }
    }

    fun volver() {
        if (puedeVolver) indice--
    }

    fun avanzar() {
        if (puedeAvanzar) indice++
    }

    // ACCIONES DE CLIC (BUTTON CLICKS)

    // LoginScreen Clicks
    fun onLoginClick(correo: String, clave: String) {
        if (cargandoLogin) return
        cargandoLogin = true
        errorLogin = null
        viewModelScope.launch {
            try {
                when (val resultado = authRepository.iniciarSesion(correo, clave)) {
                    is ResultadoAuth.Exito -> iniciarSesionInterna(resultado.usuario)
                    is ResultadoAuth.Error -> errorLogin = resultado.mensaje
                }
            } finally {
                cargandoLogin = false
            }
        }
    }

    fun onEntrarModoDemoClick() {
        if (cargandoLogin) return
        cargandoLogin = true
        errorLogin = null
        viewModelScope.launch {
            try {
                when (val resultado = authRepository.entrarDemo()) {
                    is ResultadoAuth.Exito -> iniciarSesionInterna(resultado.usuario)
                    is ResultadoAuth.Error -> errorLogin = resultado.mensaje
                }
            } finally {
                cargandoLogin = false
            }
        }
    }

    fun onIrARegistroClick() {
        errorRegistro = null
        navegarA(Pantalla.REGISTRO)
    }

    // RegisterScreen Clicks
    fun onCrearCuentaClick(nombre: String, correo: String, clave: String) {
        if (cargandoRegistro) return
        cargandoRegistro = true
        errorRegistro = null
        viewModelScope.launch {
            try {
                when (val resultado = authRepository.registrar(nombre, correo, clave)) {
                    is ResultadoAuth.Exito -> iniciarSesionInterna(
                        usuario = resultado.usuario,
                        sembrarDemo = true
                    )

                    is ResultadoAuth.Error -> errorRegistro = resultado.mensaje
                }
            } finally {
                cargandoRegistro = false
            }
        }
    }

    fun onVolverALoginClick() {
        errorLogin = null
        navegarA(Pantalla.LOGIN)
    }

    fun cerrarSesion() {
        viewModelScope.launch {
            authRepository.cerrarSesion()
            usuarioActual = null
            tramiteRepository.limpiarSesion()
            errorLogin = null
            errorRegistro = null
            reiniciarNavegacion(Pantalla.LOGIN)
        }
    }

    // MisTramitesScreen Clicks
    fun onNuevoTramiteClick() {
        tramiteRepository.limpiarBorrador()
        navegarA(Pantalla.NUEVO_TRAMITE)
    }

    fun onTramiteClick(id: Long) {
        viewModelScope.launch {
            val resumen = tramiteRepository.cargarTramite(id) ?: return@launch
            when (resumen.estado) {
                EstadoPersistido.BORRADOR -> navegarA(Pantalla.NUEVO_TRAMITE)
                EstadoPersistido.EN_REVISION,
                EstadoPersistido.FINALIZADO -> navegarA(Pantalla.TRAMITE_ENVIADO)

                EstadoPersistido.OBSERVACIONES -> navegarA(Pantalla.DETALLE_TRAMITE)
            }
        }
    }

    // NuevoTramiteScreen Clicks

    fun seleccionarFormato(formato: FormatoTramite) {
        tramiteRepository.seleccionarFormato(formato)
        persistirBorrador()
    }

    fun agregarDocumentoPropio(documento: Documento) {
        tramiteRepository.agregarDocumentoPropio(documento)
        persistirBorrador()
    }

    fun quitarDocumentoPropio(id: String) {
        tramiteRepository.quitarDocumentoPropio(id)
        persistirBorrador()
    }

    // RevisarAntecedentesScreen Clicks
    fun actualizarDatosSolicitante(nombre: String, id: String, rol: RolSolicitante) {
        tramiteRepository.actualizarDatosSolicitante(nombre, id, rol)
        persistirBorrador()
    }

    fun marcarAntecedentesRevisados(valor: Boolean) {
        tramiteRepository.marcarAntecedentesRevisados(valor)
    }

    fun limpiarBorrador() {
        tramiteRepository.limpiarBorrador()
    }

    fun onContinuarRevisionClick() {
        persistirBorrador()
        navegarA(Pantalla.REVISAR_ANTECEDENTES)
    }

    fun onVolverAMisTramitesClick() {
        viewModelScope.launch {
            tramiteRepository.cargarTramites()
            navegarA(Pantalla.MIS_TRAMITES)
        }
    }

    // RevisarAntecedentesScreen Clicks
    fun onContinuarFirmaClick() {
        persistirBorrador()
        navegarA(Pantalla.FIRMA_ENVIO)
    }

    // FirmaEnvioScreen Clicks
    fun seleccionarTipoFirma(tipo: TipoFirma) {
        tramiteRepository.seleccionarTipoFirma(tipo)
        persistirBorrador()
    }

    fun onEnviarNotarioClick() {
        viewModelScope.launch {
            tramiteRepository.enviarAlNotario()
            navegarA(Pantalla.TRAMITE_ENVIADO)
        }
    }

    // TramiteEnviadoScreen Clicks
    fun simularResolucion() {
        viewModelScope.launch {
            tramiteRepository.simularResolucion()
        }
    }

    // DetalleTramiteScreen Clicks
    fun onReenviarRevisionClick() {
        viewModelScope.launch {
            tramiteRepository.prepararReenvio()
            navegarA(Pantalla.REVISAR_ANTECEDENTES)
        }
    }
}
