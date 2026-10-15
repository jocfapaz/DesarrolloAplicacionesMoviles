package com.example.comduocnotariapass.viewmodel

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NuevoTramiteScreen(
    onContinuarRevisionClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var tipoTramite by remember { mutableStateOf("Declaración jurada simple") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton(onClick = onBackClick) { Text("< Volver") }
            Spacer(modifier = Modifier.weight(1f))
            Text("Nuevo trámite", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Paso 1 de 3: Documentos", fontSize = 12.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(16.dp))

        Text("¿Qué necesitas tramitar?", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text("Elige un formato y revisa tus documentos de prueba.", fontSize = 12.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Trámite / Formato - demo", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = tipoTramite,
            onValueChange = { tipoTramite = it },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text("Documentos adjuntos (2 archivos)", fontSize = 14.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(8.dp))

        // Archivo 1
        DocumentoItem("declaracion_demo.pdf", "PDF - 120 KB - Formato inicial")
        Spacer(modifier = Modifier.height(8.dp))
        // Archivo 2
        DocumentoItem("anexo_demo.pdf", "PDF - 190 KB - Documento previo")

        Spacer(modifier = Modifier.height(16.dp))

        // Zona para adjuntar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("↑", fontSize = 24.sp)
                Text("Adjuntar otro documento demo", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Text("PDF, PNG o JPG, hasta 10 MB por archivo", fontSize = 10.sp, color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onContinuarRevisionClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continuar a revisión")
        }
    }
}

@Composable
fun DocumentoItem(nombre: String, detalles: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(nombre, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(detalles, fontSize = 10.sp, color = Color.Gray)
            }
            Text("🗑️")
        }
    }
}