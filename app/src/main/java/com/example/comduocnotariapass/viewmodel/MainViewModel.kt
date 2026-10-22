package com.example.comduocnotariapass.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.comduocnotariapass.model.CatalogoTramites
import com.example.comduocnotariapass.model.DatosSolicitante
import com.example.comduocnotariapass.model.Documento
import com.example.comduocnotariapass.model.FormatoTramite
import com.example.comduocnotariapass.model.Pantalla
import com.example.comduocnotariapass.model.RolSolicitante
import com.example.comduocnotariapass.model.TipoFirma

class MainViewModel : ViewModel() {

    // Historial de pantallas visitadas (tipo navegador)
    private val historial = mutableStateListOf(Pantalla.LOGIN)
    private var indice by mutableStateOf(0)

    // BORRADOR DEL TRÁMITE (se conserva al navegar entre el paso 1, 2 y 3)

    // Formato predefinido elegido en "Selección de Trámite y Archivos"
    var formatoSeleccionado by mutableStateOf(CatalogoTramites.formatoPorDefecto)
        private set

    // Documentos propios adjuntados por el solicitante
    var documentosPropios by mutableStateOf<List<Documento>>(emptyList())
        private set

    // Datos verificados en el paso 2 (nombre, ID y rol del solicitante)
    var datosSolicitante by mutableStateOf(DatosSolicitante())
        private set

    // Confirmación de que el solicitante revisó los antecedentes
    var antecedentesRevisados by mutableStateOf(false)
        private set

    // Tipo de firma elegido en el paso 3
    var tipoFirmaSeleccionado by mutableStateOf(TipoFirma.NOTARIADA)
        private set

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
        limpiarBorrador()
        navegarA(Pantalla.NUEVO_TRAMITE)
    }

    fun onTramiteClick() {
        navegarA(Pantalla.DETALLE_TRAMITE)
    }

    // NuevoTramiteScreen Clicks

    fun seleccionarFormato(formato: FormatoTramite) {
        formatoSeleccionado = formato
    }

    fun agregarDocumentoPropio(documento: Documento) {
        documentosPropios = documentosPropios + documento
    }

    fun quitarDocumentoPropio(id: String) {
        documentosPropios = documentosPropios.filterNot { it.id == id }
    }

    // RevisarAntecedentesScreen Clicks
    fun actualizarDatosSolicitante(nombre: String, id: String, rol: RolSolicitante) {
        datosSolicitante = datosSolicitante.copy(nombre = nombre, id = id, rol = rol)
    }

    fun marcarAntecedentesRevisados(valor: Boolean) {
        antecedentesRevisados = valor
    }

    fun limpiarBorrador() {
        formatoSeleccionado = CatalogoTramites.formatoPorDefecto
        documentosPropios = emptyList()
        datosSolicitante = DatosSolicitante()
        antecedentesRevisados = false
        tipoFirmaSeleccionado = TipoFirma.NOTARIADA
    }

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
    fun seleccionarTipoFirma(tipo: TipoFirma) {
        tipoFirmaSeleccionado = tipo
    }

    fun onEnviarNotarioClick() {
        navegarA(Pantalla.TRAMITE_ENVIADO)
    }

    // DetalleTramiteScreen Clicks
    fun onReenviarRevisionClick() {
        navegarA(Pantalla.REVISAR_ANTECEDENTES)
    }
}
