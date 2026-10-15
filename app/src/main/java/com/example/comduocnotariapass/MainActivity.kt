package com.example.comduocnotariapass

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.comduocnotariapass.model.Pantalla
import com.example.comduocnotariapass.model.TramiteModel
import com.example.comduocnotariapass.viewmodel.LoginScreen
import com.example.comduocnotariapass.viewmodel.MisTramitesScreen
import com.example.comduocnotariapass.viewmodel.NuevoTramiteScreen
import com.example.comduocnotariapass.viewmodel.RegisterScreen
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
                                }
                            )
                        }

                        Pantalla.MIS_TRAMITES -> {
                            MisTramitesScreen(
                                onNuevoTramiteClick = {
                                    mainViewModel.onNuevoTramiteClick()
                                }
                            )
                        }

                        Pantalla.NUEVO_TRAMITE -> {
                            NuevoTramiteScreen(
                                onContinuarRevisionClick = {
                                    mainViewModel.onContinuarRevisionClick()
                                },
                                onBackClick = {
                                    mainViewModel.onVolverAMisTramitesClick()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}