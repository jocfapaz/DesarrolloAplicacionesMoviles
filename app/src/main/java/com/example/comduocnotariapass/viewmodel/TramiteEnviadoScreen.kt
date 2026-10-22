package com.example.comduocnotariapass.viewmodel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.comduocnotariapass.model.DatosSolicitante
import com.example.comduocnotariapass.model.Documento
import com.example.comduocnotariapass.model.EstadoTramite
import com.example.comduocnotariapass.model.FormatoTramite
import com.example.comduocnotariapass.model.TipoFirma

@Composable
fun TramiteEnviadoScreen(
    formatoSeleccionado: FormatoTramite,
    documentos: List<Documento>,
    datosSolicitante: DatosSolicitante,
    tipoFirma: TipoFirma,
    estadoTramite: EstadoTramite,
    onSimularResolucion: () -> Unit,
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
            .verticalScroll(rememberScrollState())
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
            Text("Estado del trámite", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Surface(color = Color(0xFFE0E7FF), shape = RoundedCornerShape(4.dp)) {
                Text("DEMO", modifier = Modifier.padding(6.dp, 2.dp), fontSize = 11.sp, color = Color(0xFF1E3A8A))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Encabezado: ID de seguimiento y modalidad de firma
        EncabezadoEstadoTramite(
            estado = estadoTramite,
            tipoFirma = tipoFirma
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Línea de progreso interactiva
        LineaProgresoTramite(estado = estadoTramite)

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = onSimularResolucion,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(estadoTramite.textoBotonDemo(), fontSize = 12.sp, color = Color(0xFF2563EB))
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Resumen del envío", fontSize = 13.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(8.dp))

        // Card con resumen del envío
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "${estadoTramite.id} · ${datosSolicitante.rol.nombre}",
                    fontSize = 11.sp,
                    color = Color(0xFF2563EB),
                    fontWeight = FontWeight.Bold
                )
                Text(formatoSeleccionado.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(
                    "${estadoTramite.etiquetaEstado} · ${tipoFirma.nombre} · Simulada",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    "Simula: ${tipoFirma.mecanismos.joinToString(" · ") { it.etiqueta }}",
                    fontSize = 11.sp,
                    color = Color(0xFF2563EB),
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Nombre · Dato ficticio", fontSize = 11.sp, color = Color.Gray)
                Text(datosSolicitante.nombre, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                Text("ID: ${datosSolicitante.id}", fontSize = 12.sp, color = Color.Gray)

                Spacer(modifier = Modifier.height(12.dp))

                Text("Documentos Adjuntados (${documentos.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                documentos.forEach { documento ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(documento.nombre, fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

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
