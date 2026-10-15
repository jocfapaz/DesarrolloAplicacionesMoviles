package com.example.comduocnotariapass.viewmodel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RevisarAntecedentesScreen(
    onBackClick: () -> Unit,
    onContinuarFirma: () -> Unit
) {
    var isChecked by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FC))
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // TopBar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
            }
            Text("Revisar antecedentes", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Surface(color = Color(0xFFE0E7FF), shape = RoundedCornerShape(4.dp)) {
                Text("DEMO", modifier = Modifier.padding(6.dp, 2.dp), fontSize = 11.sp, color = Color(0xFF1E3A8A))
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text("Paso 2 de 3", fontSize = 12.sp, color = Color(0xFF2563EB), fontWeight = FontWeight.Bold)
        Text("Revisa antes de seguir", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text("Comprueba los antecedentes y los archivos de tu borrador.", fontSize = 13.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(16.dp))

        // Tarjeta Resumen Borrador
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("NX-DEMO-024 · Persona natural", fontSize = 11.sp, color = Color(0xFF2563EB), fontWeight = FontWeight.Bold)
                Text("Declaración jurada simple", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("Formato demo · Creado el 12 oct. 2026", fontSize = 12.sp, color = Color.Gray)

                Divider(modifier = Modifier.padding(vertical = 12.dp), color = Color.LightGray)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Antecedentes demo", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text("Editar", color = Color(0xFF2563EB), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text("Solicitante: Persona Demo", fontSize = 12.sp)
                Text("Identificación: ID-DEMO-001 · Sin RUT real", fontSize = 12.sp)
                Text("Correo: persona@demo.invalid", fontSize = 12.sp)
                Text("Región: Región Demo · Dato ficticio", fontSize = 12.sp)
                Text("Comuna: Sin completar", fontSize = 12.sp, color = Color.Red)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Documentos (2)", fontSize = 14.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(8.dp))

        // Lista de documentos
        DocumentoItemView("declaracion_demo.pdf", "PDF · 240 KB · Ficticio")
        Spacer(modifier = Modifier.height(6.dp))
        DocumentoItemView("anexo_demo.pdf", "PDF · 180 KB · Ficticio")

        Spacer(modifier = Modifier.height(16.dp))

        // Checkbox confirmación
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { isChecked = it }
            )
            Text("Revisé los datos ficticios.", fontSize = 12.sp, color = Color.DarkGray)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onContinuarFirma,
            enabled = isChecked,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Continuar a firma", color = Color.White)
        }
    }
}

@Composable
fun DocumentoItemView(nombre: String, subtext: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(nombre, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text(subtext, fontSize = 11.sp, color = Color.Gray)
            }
            Icon(Icons.Default.Visibility, contentDescription = "Ver", tint = Color.Gray)
        }
    }
}