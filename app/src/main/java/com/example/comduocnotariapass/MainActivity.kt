package com.example.comduocnotariapass

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.comduocnotariapass.model.Pantalla
import com.example.comduocnotariapass.ui.theme.ComduocNotariaPassTheme
import com.example.comduocnotariapass.util.NotificacionesHelper
import com.example.comduocnotariapass.viewmodel.DetalleTramiteScreen
import com.example.comduocnotariapass.viewmodel.FirmaEnvioScreen
import com.example.comduocnotariapass.viewmodel.LoginScreen
import com.example.comduocnotariapass.viewmodel.MainViewModel
import com.example.comduocnotariapass.viewmodel.MisTramitesScreen
import com.example.comduocnotariapass.viewmodel.NuevoTramiteScreen
import com.example.comduocnotariapass.viewmodel.RegisterScreen
import com.example.comduocnotariapass.viewmodel.RevisarAntecedentesScreen
import com.example.comduocnotariapass.viewmodel.TramiteEnviadoScreen


class MainActivity : ComponentActivity() {

    // Instancia del ViewModel
    private val mainViewModel: MainViewModel by viewModels()

    private val solicitarPermisoNotificacion = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            NotificacionesHelper.mostrarFirmaConfirmada(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        NotificacionesHelper.crearCanal(this)
        setContent {
            ComduocNotariaPassTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    if (mainViewModel.cargandoSesion) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        // Renderiza la pantalla según el estado del ViewModel
                        when (mainViewModel.pantallaActual) {

                            Pantalla.LOGIN -> {
                                LoginScreen(
                                    onLoginClick = { correo, clave ->
                                        mainViewModel.onLoginClick(correo, clave)
                                    },
                                    onDemoClick = {
                                        mainViewModel.onEntrarModoDemoClick()
                                    },
                                    onRegisterClick = {
                                        mainViewModel.onIrARegistroClick()
                                    },
                                    error = mainViewModel.errorLogin,
                                    cargando = mainViewModel.cargandoLogin
                                )
                            }

                            Pantalla.REGISTRO -> {
                                RegisterScreen(
                                    onRegisterSuccess = { nombre, correo, clave ->
                                        mainViewModel.onCrearCuentaClick(nombre, correo, clave)
                                    },
                                    onBackToLogin = {
                                        mainViewModel.onVolverALoginClick()
                                    },
                                    puedeVolver = mainViewModel.puedeVolver,
                                    puedeAvanzar = mainViewModel.puedeAvanzar,
                                    onVolver = { mainViewModel.volver() },
                                    onAvanzar = { mainViewModel.avanzar() },
                                    error = mainViewModel.errorRegistro,
                                    cargando = mainViewModel.cargandoRegistro
                                )
                            }

                            Pantalla.MIS_TRAMITES -> {
                                MisTramitesScreen(
                                    usuarioNombre = mainViewModel.nombreUsuario,
                                    tramites = mainViewModel.tramites,
                                    onNuevoTramiteClick = {
                                        mainViewModel.onNuevoTramiteClick()
                                    },
                                    onTramiteClick = { id ->
                                        mainViewModel.onTramiteClick(id)
                                    },
                                    onCerrarSesion = {
                                        mainViewModel.cerrarSesion()
                                    },
                                    puedeVolver = mainViewModel.puedeVolver,
                                    puedeAvanzar = mainViewModel.puedeAvanzar,
                                    onVolver = { mainViewModel.volver() },
                                    onAvanzar = { mainViewModel.avanzar() }
                                )
                            }

                            Pantalla.NUEVO_TRAMITE -> {
                                NuevoTramiteScreen(
                                    formatos = mainViewModel.formatos,
                                    formatoSeleccionado = mainViewModel.formatoSeleccionado,
                                    documentosPropios = mainViewModel.documentosPropios,
                                    onSeleccionarFormato = { formato ->
                                        mainViewModel.seleccionarFormato(formato)
                                    },
                                    onAgregarDocumento = { documento ->
                                        mainViewModel.agregarDocumentoPropio(documento)
                                    },
                                    onQuitarDocumento = { id ->
                                        mainViewModel.quitarDocumentoPropio(id)
                                    },
                                    onContinuarRevisionClick = {
                                        mainViewModel.onContinuarRevisionClick()
                                    },
                                    puedeVolver = mainViewModel.puedeVolver,
                                    puedeAvanzar = mainViewModel.puedeAvanzar,
                                    onVolver = { mainViewModel.volver() },
                                    onAvanzar = { mainViewModel.avanzar() }
                                )
                            }

                            Pantalla.REVISAR_ANTECEDENTES -> {
                                RevisarAntecedentesScreen(
                                    formatoSeleccionado = mainViewModel.formatoSeleccionado,
                                    documentosPropios = mainViewModel.documentosPropios,
                                    datosSolicitante = mainViewModel.datosSolicitante,
                                    antecedentesRevisados = mainViewModel.antecedentesRevisados,
                                    onActualizarDatos = { nombre, id, rol ->
                                        mainViewModel.actualizarDatosSolicitante(nombre, id, rol)
                                    },
                                    onAntecedentesRevisadosChange = { revisados ->
                                        mainViewModel.marcarAntecedentesRevisados(revisados)
                                    },
                                    onContinuarFirma = {
                                        mainViewModel.onContinuarFirmaClick()
                                    },
                                    puedeVolver = mainViewModel.puedeVolver,
                                    puedeAvanzar = mainViewModel.puedeAvanzar,
                                    onVolver = { mainViewModel.volver() },
                                    onAvanzar = { mainViewModel.avanzar() }
                                )
                            }

                            Pantalla.FIRMA_ENVIO -> {
                                FirmaEnvioScreen(
                                    tipoFirmaSeleccionado = mainViewModel.tipoFirmaSeleccionado,
                                    onSeleccionarTipoFirma = { tipo ->
                                        mainViewModel.seleccionarTipoFirma(tipo)
                                    },
                                    onEnviarNotario = {
                                        mainViewModel.onEnviarNotarioClick()
                                    },
                                    puedeVolver = mainViewModel.puedeVolver,
                                    puedeAvanzar = mainViewModel.puedeAvanzar,
                                    onVolver = { mainViewModel.volver() },
                                    onAvanzar = { mainViewModel.avanzar() }
                                )
                            }

                            Pantalla.TRAMITE_ENVIADO -> {
                                TramiteEnviadoScreen(
                                    formatoSeleccionado = mainViewModel.formatoSeleccionado,
                                    documentos = mainViewModel.formatoSeleccionado.documentosInclusos +
                                        mainViewModel.documentosPropios,
                                    datosSolicitante = mainViewModel.datosSolicitante,
                                    tipoFirma = mainViewModel.tipoFirmaSeleccionado,
                                    estadoTramite = mainViewModel.estadoTramite,
                                    onSimularResolucion = {
                                        mainViewModel.simularResolucion()
                                    },
                                    onConfirmarEnvio = {
                                        if (NotificacionesHelper.tienePermiso(this)) {
                                            NotificacionesHelper.mostrarFirmaConfirmada(this)
                                        } else {
                                            solicitarPermisoNotificacion.launch(Manifest.permission.POST_NOTIFICATIONS)
                                        }
                                    },
                                    onVolverInicio = {
                                        mainViewModel.onVolverAMisTramitesClick()
                                    },
                                    puedeVolver = mainViewModel.puedeVolver,
                                    puedeAvanzar = mainViewModel.puedeAvanzar,
                                    onVolver = { mainViewModel.volver() },
                                    onAvanzar = { mainViewModel.avanzar() }
                                )
                            }

                            Pantalla.DETALLE_TRAMITE -> {
                                DetalleTramiteScreen(
                                    formatoSeleccionado = mainViewModel.formatoSeleccionado,
                                    documentosPropios = mainViewModel.documentosPropios,
                                    estadoTramite = mainViewModel.estadoTramite,
                                    onAgregarDocumento = { documento ->
                                        mainViewModel.agregarDocumentoPropio(documento)
                                    },
                                    onReenviarRevision = {
                                        mainViewModel.onReenviarRevisionClick()
                                    },
                                    puedeVolver = mainViewModel.puedeVolver,
                                    puedeAvanzar = mainViewModel.puedeAvanzar,
                                    onVolver = { mainViewModel.volver() },
                                    onAvanzar = { mainViewModel.avanzar() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
