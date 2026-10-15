package com.example.comduocnotariapass

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.comduocnotariapass.viewmodel.LoginScreen
import com.example.comduocnotariapass.viewmodel.MisTramitesScreen
import com.example.comduocnotariapass.viewmodel.NuevoTramiteScreen
import com.example.comduocnotariapass.viewmodel.RegisterScreen
import com.example.comduocnotariapass.ui.theme.ComduocNotariaPassTheme

// Enum o Sealed Class para definir tus pantallas
enum class Pantalla {
    LOGIN,
    REGISTRO,
    MIS_TRAMITES,
    NUEVO_TRAMITE
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComduocNotariaPassTheme {
                Surface(color = MaterialTheme.colorScheme.background) {

                    // Variable de estado que guarda cuál pantalla se está mostrando actualmente
                    var pantallaActual by remember { mutableStateOf(Pantalla.LOGIN) }

                    // Según la pantalla actual, renderizamos el composable correspondiente
                    when (pantallaActual) {
                        Pantalla.LOGIN -> {
                            LoginScreen(
                                onLoginClick = {
                                    // Al iniciar sesión, navegamos a "Mis Trámites"
                                    pantallaActual = Pantalla.MIS_TRAMITES
                                },
                                onDemoClick = {
                                    // Al entrar en modo demo, navegamos a "Mis Trámites"
                                    pantallaActual = Pantalla.MIS_TRAMITES
                                },
                                onRegisterClick = {
                                    // Al presionar registrarse, navegamos a "Registro"
                                    pantallaActual = Pantalla.REGISTRO
                                }
                            )
                        }

                        Pantalla.REGISTRO -> {
                            RegisterScreen(
                                onRegisterSuccess = {
                                    // Al completarse el registro, podemos ir a "Mis Trámites" o "Login"
                                    pantallaActual = Pantalla.MIS_TRAMITES
                                },
                                onBackToLogin = {
                                    pantallaActual = Pantalla.LOGIN
                                }
                            )
                        }

                        Pantalla.MIS_TRAMITES -> {
                            MisTramitesScreen(
                                onNuevoTramiteClick = {
                                    pantallaActual = Pantalla.NUEVO_TRAMITE
                                }
                            )
                        }

                        Pantalla.NUEVO_TRAMITE -> {
                            NuevoTramiteScreen(
                                onContinuarRevisionClick = {
                                    // Lógica o siguiente pantalla
                                },
                                onBackClick = {
                                    pantallaActual = Pantalla.MIS_TRAMITES
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}