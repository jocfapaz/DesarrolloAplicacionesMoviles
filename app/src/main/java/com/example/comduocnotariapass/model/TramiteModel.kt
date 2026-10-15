package com.example.comduocnotariapass.model
import androidx.compose.ui.graphics.Color

// Modelo de datos para las pantallas
data class TramiteModel(
    val id: String,
    val titulo: String,
    val codigo: String,
    val fecha: String,
    val estado: String,
    val estadoColor: Color
)

// Definición de las pantallas de la app
enum class Pantalla {
    LOGIN,
    REGISTRO,
    MIS_TRAMITES,
    NUEVO_TRAMITE
}