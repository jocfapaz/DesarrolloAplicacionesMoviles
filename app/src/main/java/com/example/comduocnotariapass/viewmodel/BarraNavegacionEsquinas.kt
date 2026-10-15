package com.example.comduocnotariapass.viewmodel

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun BarraNavegacionEsquinas(
    puedeVolver: Boolean,
    puedeAvanzar: Boolean,
    onVolver: () -> Unit,
    onAvanzar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onVolver, enabled = puedeVolver) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
        }
        IconButton(onClick = onAvanzar, enabled = puedeAvanzar) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Avanzar")
        }
    }
}
