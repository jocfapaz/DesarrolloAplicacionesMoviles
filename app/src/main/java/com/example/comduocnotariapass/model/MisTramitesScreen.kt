package com.example.comduocnotariapass.model

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class TramiteItem(
    val id: String,
    val titulo: String,
    val codigo: String,
    val fecha: String,
    val estado: String,
    val estadoColor: Color
)

@Composable
fun MisTramitesScreen(
    onNuevoTramiteClick: () -> Unit
) {
    val tramites = listOf(
        TramiteItem("1", "Declaración jurada simple", "NX-DEMO-024", "12 oct. 2026", "Borrador", Color.Gray),
        TramiteItem("2", "Autorización de viaje demo", "NX-DEMO-023", "11 oct. 2026", "En revisión", Color(0xFFFFA000)),
        TramiteItem("3", "Poder simple demo", "NX-DEMO-022", "10 oct. 2026", "Finalizado", Color(0xFF388E3C))
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(selected = true, onClick = {}, label = { Text("Mis trámites") }, icon = {})
                NavigationBarItem(selected = false, onClick = {}, label = { Text("Ayuda demo") }, icon = {})
                NavigationBarItem(selected = false, onClick = {}, label = { Text("Perfil demo") }, icon = {})
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text("NotariaX", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            Text("Mis trámites", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("Hola, Persona Demo. Aquí puedes ordenar y seguir cada solicitud.", fontSize = 12.sp)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onNuevoTramiteClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("+ Nuevo trámite")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Actividad reciente", fontWeight = FontWeight.Bold, fontSize = 14.sp)

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(tramites) { tramite ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Surface(
                                    color = tramite.estadoColor.copy(alpha = 0.2f),
                                    shape = MaterialTheme.shapes.small
                                ) {
                                    Text(
                                        text = tramite.estado,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                        fontSize = 10.sp,
                                        color = tramite.estadoColor,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text(tramite.fecha, fontSize = 10.sp, color = Color.Gray)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(tramite.titulo, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(tramite.codigo, fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}