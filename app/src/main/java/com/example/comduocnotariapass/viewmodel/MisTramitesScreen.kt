package com.example.comduocnotariapass.viewmodel

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.comduocnotariapass.R
import com.example.comduocnotariapass.model.EstadoPersistido
import com.example.comduocnotariapass.model.TramiteResumen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MisTramitesScreen(
    usuarioNombre: String,
    tramites: List<TramiteResumen>,
    onNuevoTramiteClick: () -> Unit,
    onTramiteClick: (Long) -> Unit,
    onCerrarSesion: () -> Unit,
    puedeVolver: Boolean,
    puedeAvanzar: Boolean,
    onVolver: () -> Unit,
    onAvanzar: () -> Unit
) {
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
                Image(
                    painter = painterResource(id = R.drawable.logo_notaria_pass),
                    contentDescription = "Logo de NotariaPass",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(240.dp)
                        .aspectRatio(1980f / 521f)
                )
                TextButton(onClick = onCerrarSesion) {
                    Text("Cerrar sesión", fontSize = 11.sp)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            Text("Mis trámites", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(
                "Hola, $usuarioNombre. Aquí puedes ordenar y seguir cada solicitud.",
                fontSize = 12.sp
            )

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
                if (tramites.isEmpty()) {
                    item {
                        Text(
                            text = "Todavía no tienes trámites. Crea uno nuevo para comenzar.",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                } else {
                    items(tramites, key = { it.id }) { tramite ->
                        val estadoColor = colorEstado(tramite.estado)
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onTramiteClick(tramite.id) },
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Surface(
                                        color = estadoColor.copy(alpha = 0.2f),
                                        shape = MaterialTheme.shapes.small
                                    ) {
                                        Text(
                                            text = tramite.estado.etiqueta,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                            fontSize = 10.sp,
                                            color = estadoColor,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Text(
                                        text = fechaLegible(tramite.fechaCreacion),
                                        fontSize = 10.sp,
                                        color = Color.Gray
                                    )
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
}

private fun colorEstado(estado: EstadoPersistido): Color = when (estado) {
    EstadoPersistido.BORRADOR -> Color.Gray
    EstadoPersistido.EN_REVISION -> Color(0xFFFFA000)
    EstadoPersistido.OBSERVACIONES -> Color(0xFFE65100)
    EstadoPersistido.FINALIZADO -> Color(0xFF388E3C)
}

private fun fechaLegible(millis: Long): String =
    SimpleDateFormat("dd MMM yyyy", Locale.forLanguageTag("es-CL")).format(Date(millis))
