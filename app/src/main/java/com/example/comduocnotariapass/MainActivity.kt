package com.example.comduocnotariapass

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.comduocnotariapass.model.CatalogoTramites
import com.example.comduocnotariapass.model.Pantalla
import com.example.comduocnotariapass.viewmodel.DetalleTramiteScreen
import com.example.comduocnotariapass.viewmodel.FirmaEnvioScreen
import com.example.comduocnotariapass.viewmodel.LoginScreen
import com.example.comduocnotariapass.viewmodel.MisTramitesScreen
import com.example.comduocnotariapass.viewmodel.NuevoTramiteScreen
import com.example.comduocnotariapass.viewmodel.RevisarAntecedentesScreen
import com.example.comduocnotariapass.viewmodel.RegisterScreen
import com.example.comduocnotariapass.viewmodel.TramiteEnviadoScreen
import com.example.comduocnotariapass.ui.theme.ComduocNotariaPassTheme
import com.example.comduocnotariapass.viewmodel.MainViewModel


class MainActivity : ComponentActivity() {

    // Instancia del ViewModel
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComduocNotariaPassTheme {
                Surface(color = MaterialTheme.colorScheme.background) {

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
                                }
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
                                onAvanzar = { mainViewModel.avanzar() }
                            )
                        }

                        Pantalla.MIS_TRAMITES -> {
                            MisTramitesScreen(
                                onNuevoTramiteClick = {
                                    mainViewModel.onNuevoTramiteClick()
                                },
                                onTramiteClick = {
                                    mainViewModel.onTramiteClick()
                                },
                                puedeVolver = mainViewModel.puedeVolver,
                                puedeAvanzar = mainViewModel.puedeAvanzar,
                                onVolver = { mainViewModel.volver() },
                                onAvanzar = { mainViewModel.avanzar() }
                            )
                        }

                        Pantalla.NUEVO_TRAMITE -> {
                            NuevoTramiteScreen(
                                formatos = CatalogoTramites.formatos,
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
