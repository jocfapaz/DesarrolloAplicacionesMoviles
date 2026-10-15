package com.example.comduocnotariapass.viewmodel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DetalleTramiteScreen(
    onBackClick: () -> Unit,
    onReenviarRevision: () -> Unit
) {
    var comuna by remember { mutableStateOf("Comuna Demo") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FC))
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
            }
            Text("Corregir solicitud", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Surface(color = Color(0xFFE0E7FF), shape = RoundedCornerShape(4.dp)) {
                Text("DEMO", modifier = Modifier.padding(6.dp, 2.dp), fontSize = 11.sp, color = Color(0xFF1E3A8A))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Necesita una corrección", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text("Actualiza lo solicitado y conserva el resto de tu trámite.", fontSize = 13.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(16.dp))

        // Alerta de observación
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7))
        ) {
            Row(modifier = Modifier.padding(12.dp)) {
                Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFD97706))
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("Observación del notario de prueba", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF92400E))
                    Text("Completa la comuna y reemplaza anexo_demo.pdf; la segunda página no es legible.", fontSize = 11.sp, color = Color(0xFFB45309))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Edición de Comuna
        Text("Comuna · Dato ficticio", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        OutlinedTextField(
            value = comuna,
            onValueChange = { comuna = it },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Documentos (2)", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        DocumentoItemView("declaracion_demo.pdf", "PDF · 240 KB · Sin cambios")
        Spacer(modifier = Modifier.height(6.dp))
        DocumentoItemView("anexo_demo_v2.pdf", "PDF · 210 KB · Corrección ficticia")

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onReenviarRevision,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Reenviar a revisión", color = Color.White)
        }
    }
}