package com.example.comduocnotariapass.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.comduocnotariapass.model.Pantalla

class MainViewModel : ViewModel() {

    // Historial de pantallas visitadas (tipo navegador)
    private val historial = mutableStateListOf(Pantalla.LOGIN)
    private var indice by mutableStateOf(0)

    // Estado que indica qué pantalla se muestra actualmente
    val pantallaActual: Pantalla get() = historial[indice]

    // Estado de los botones de navegación en las esquinas
    val puedeVolver: Boolean get() = indice > 0
    val puedeAvanzar: Boolean get() = indice < historial.lastIndex

    private fun navegarA(pantalla: Pantalla) {
        while (historial.lastIndex > indice) {
            historial.removeAt(historial.lastIndex)
        }
        if (pantallaActual != pantalla) {
            historial.add(pantalla)
            indice = historial.lastIndex
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
        // Aquí puedes agregar validación o llamados al repository
        navegarA(Pantalla.MIS_TRAMITES)
    }

    fun onEntrarModoDemoClick() {
        navegarA(Pantalla.MIS_TRAMITES)
    }

    fun onIrARegistroClick() {
        navegarA(Pantalla.REGISTRO)
    }

    // RegisterScreen Clicks
    fun onCrearCuentaClick(nombre: String, correo: String, clave: String) {
        // Lógica de registro...
        navegarA(Pantalla.MIS_TRAMITES)
    }

    fun onVolverALoginClick() {
        navegarA(Pantalla.LOGIN)
    }

    // MisTramitesScreen Clicks
    fun onNuevoTramiteClick() {
        navegarA(Pantalla.NUEVO_TRAMITE)
    }

    fun onTramiteClick() {
        navegarA(Pantalla.DETALLE_TRAMITE)
    }

    // NuevoTramiteScreen Clicks
    fun onContinuarRevisionClick() {
        navegarA(Pantalla.REVISAR_ANTECEDENTES)
    }

    fun onVolverAMisTramitesClick() {
        navegarA(Pantalla.MIS_TRAMITES)
    }

    // RevisarAntecedentesScreen Clicks
    fun onContinuarFirmaClick() {
        navegarA(Pantalla.FIRMA_ENVIO)
    }

    // FirmaEnvioScreen Clicks
    fun onEnviarNotarioClick() {
        navegarA(Pantalla.TRAMITE_ENVIADO)
    }

    // DetalleTramiteScreen Clicks
    fun onReenviarRevisionClick() {
        navegarA(Pantalla.REVISAR_ANTECEDENTES)
    }
}
