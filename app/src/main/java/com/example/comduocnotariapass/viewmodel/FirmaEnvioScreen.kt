package com.example.comduocnotariapass.viewmodel

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FirmaEnvioScreen(
    onEnviarNotario: () -> Unit,
    puedeVolver: Boolean,
    puedeAvanzar: Boolean,
    onVolver: () -> Unit,
    onAvanzar: () -> Unit
) {
    var selectedFirma by remember { mutableStateOf(2) } // 0: Simple, 1: Avanzada, 2: Notariada
    var entiendoCheckbox by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FC))
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        BarraNavegacionEsquinas(
            puedeVolver = puedeVolver,
            puedeAvanzar = puedeAvanzar,
            onVolver = onVolver,
            onAvanzar = onAvanzar
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Firma y envío", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Surface(color = Color(0xFFE0E7FF), shape = RoundedCornerShape(4.dp)) {
                Text("DEMO", modifier = Modifier.padding(6.dp, 2.dp), fontSize = 11.sp, color = Color(0xFF1E3A8A))
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text("Paso 3 de 3", fontSize = 12.sp, color = Color(0xFF2563EB), fontWeight = FontWeight.Bold)
        Text("Elige cómo firmar", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text("Selecciona una opción para representar tu firma en este recorrido demo.", fontSize = 13.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(16.dp))

        // Opción 1
        FirmaOpciónCard(
            titulo = "Firma electrónica simple",
            subtitulo = "Trazo digital de prueba, sin firma real.",
            selected = selectedFirma == 0,
            onClick = { selectedFirma = 0 }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Opción 2
        FirmaOpciónCard(
            titulo = "Firma electrónica avanzada",
            subtitulo = "Certificado y proveedor simulados; sin integración real.",
            selected = selectedFirma == 1,
            onClick = { selectedFirma = 1 }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Opción 3
        FirmaOpciónCard(
            titulo = "Firma notariada",
            subtitulo = "Firma demo y revisión por notario de prueba.",
            selected = selectedFirma == 2,
            onClick = { selectedFirma = 2 }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Representación visual de firma
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Representación de firma", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Text("DEMO", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1E3A8A))
                Text("Sello Ficticio · No es una firma personal", fontSize = 10.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Representación simulada aplicada", fontSize = 11.sp, color = Color(0xFF10B981))
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = entiendoCheckbox, onCheckedChange = { entiendoCheckbox = it })
            Text("Entiendo que el envío y la revisión no tienen validez legal.", fontSize = 11.sp, color = Color.Gray)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onEnviarNotario,
            enabled = entiendoCheckbox,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Enviar al notario de prueba", color = Color.White)
        }
    }
}

@Composable
fun FirmaOpciónCard(titulo: String, subtitulo: String, selected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) Color(0xFF2563EB) else Color.LightGray,
                shape = RoundedCornerShape(8.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = if (selected) Color(0xFFEFF6FF) else Color.White)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = selected, onClick = onClick)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(titulo, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(subtitulo, fontSize = 11.sp, color = Color.Gray)
            }
        }
    }
}