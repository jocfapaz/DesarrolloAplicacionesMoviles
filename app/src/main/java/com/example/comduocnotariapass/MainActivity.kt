package com.example.comduocnotariapass

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.comduocnotariapass.model.LoginScreen
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

                        else -> {}
                    }
                }
            }
        }
    }
}