package com.example.comduocnotariapass.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.comduocnotariapass.model.Pantalla

class MainViewModel : ViewModel() {

    // Estado que indica qué pantalla se muestra actualmente
    var pantallaActual by mutableStateOf(Pantalla.LOGIN)
        private set

    // ACCIONES DE CLIC (BUTTON CLICKS)

    // LoginScreen Clicks
    fun onLoginClick(correo: String, clave: String) {
        // Aquí puedes agregar validación o llamados al repository
        pantallaActual = Pantalla.MIS_TRAMITES
    }

    fun onEntrarModoDemoClick() {
        pantallaActual = Pantalla.MIS_TRAMITES
    }

    fun onIrARegistroClick() {
        pantallaActual = Pantalla.REGISTRO
    }

    // RegisterScreen Clicks
    fun onCrearCuentaClick(nombre: String, correo: String, clave: String) {
        // Lógica de registro...
        pantallaActual = Pantalla.MIS_TRAMITES
    }

    fun onVolverALoginClick() {
        pantallaActual = Pantalla.LOGIN
    }

    // MisTramitesScreen Clicks
    fun onNuevoTramiteClick() {
        pantallaActual = Pantalla.NUEVO_TRAMITE
    }

    // NuevoTramiteScreen Clicks
    fun onContinuarRevisionClick() {
        // Avanzar en el flujo de nuevo trámite...
    }

    fun onVolverAMisTramitesClick() {
        pantallaActual = Pantalla.MIS_TRAMITES
    }
}