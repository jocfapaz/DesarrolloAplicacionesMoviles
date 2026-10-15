package com.example.comduocnotariapass.viewmodel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TramiteEnviadoScreen(
    onVolverInicio: () -> Unit,
    puedeVolver: Boolean,
    puedeAvanzar: Boolean,
    onVolver: () -> Unit,
    onAvanzar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FC))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
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
            Text("Trámite a enviar", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Surface(color = Color(0xFFE0E7FF), shape = RoundedCornerShape(4.dp)) {
                Text("DEMO", modifier = Modifier.padding(6.dp, 2.dp), fontSize = 11.sp, color = Color(0xFF1E3A8A))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Enviado", fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
        Text("Su solicitud de trámite ha sido enviada a NotariaX.", fontSize = 13.sp, color = Color.Gray, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(24.dp))

        // Card con resumen del envío
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("NX-DEMO-024 · Persona natural", fontSize = 11.sp, color = Color(0xFF2563EB), fontWeight = FontWeight.Bold)
                Text("Declaración jurada simple", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("En corrección · Firma notariada simulada", fontSize = 12.sp, color = Color.Gray)

                Spacer(modifier = Modifier.height(16.dp))

                Text("Nombre · Dato ficticio", fontSize = 11.sp, color = Color.Gray)
                Text("Nombre del Solicitante", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)

                Spacer(modifier = Modifier.height(12.dp))

                Text("Documentos Adjuntados (2)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("anexo_demo_v2.pdf", fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Surface(
            color = Color(0xFFDBEAFE),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Solicitud enviada correctamente!",
                modifier = Modifier.padding(14.dp),
                color = Color(0xFF1E3A8A),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onVolverInicio,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Volver a mis trámites", color = Color.White)
        }
    }
}