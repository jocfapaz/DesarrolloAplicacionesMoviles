package com.example.comduocnotariapass.viewmodel

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.comduocnotariapass.model.MecanismoSimulado
import com.example.comduocnotariapass.model.TipoFirma

@Composable
fun FirmaEnvioScreen(
    tipoFirmaSeleccionado: TipoFirma,
    onSeleccionarTipoFirma: (TipoFirma) -> Unit,
    onEnviarNotario: () -> Unit,
    puedeVolver: Boolean,
    puedeAvanzar: Boolean,
    onVolver: () -> Unit,
    onAvanzar: () -> Unit
) {
    var entiendoCheckbox by remember { mutableStateOf(false) }

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
        Text(
            "Selecciona una opción para representar tu firma en este recorrido demo.",
            fontSize = 13.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        // SELECCIÓN DE TIPO DE FIRMA
        Text("Selección de Tipo de Firma", fontSize = 14.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(8.dp))

        TipoFirma.entries.forEachIndexed { indice, tipo ->
            if (indice > 0) Spacer(modifier = Modifier.height(10.dp))
            FirmaOpcionCard(
                tipo = tipo,
                selected = tipo == tipoFirmaSeleccionado,
                onClick = { onSeleccionarTipoFirma(tipo) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // CUADRO EXPLICATIVO DE SIMULACIÓN ACADÉMICA
        CuadroSimulacionAcademica(tipoFirma = tipoFirmaSeleccionado)

        Spacer(modifier = Modifier.height(16.dp))

        // Representación visual de firma
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Representación de firma · ${tipoFirmaSeleccionado.nombre}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
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
fun FirmaOpcionCard(tipo: TipoFirma, selected: Boolean, onClick: () -> Unit) {
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
            Column(modifier = Modifier.weight(1f)) {
                Text(tipo.nombre, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(tipo.subtitulo, fontSize = 11.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    "Simula: ${tipo.mecanismos.joinToString(" · ") { it.etiqueta }}",
                    fontSize = 10.sp,
                    color = Color(0xFF2563EB),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun CuadroSimulacionAcademica(tipoFirma: TipoFirma) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = null,
                    tint = Color(0xFF2563EB),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    "Cuadro explicativo de simulación académica",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color(0xFF1E3A8A)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                "Este recorrido es académico: no se usan firmas ni certificados reales. " +
                    "Los recuadros resaltados son los mecanismos que simula la opción elegida.",
                fontSize = 11.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            MecanismoSimulado.entries.forEachIndexed { indice, mecanismo ->
                if (indice > 0) Spacer(modifier = Modifier.height(8.dp))
                MecanismoSimuladoFila(
                    mecanismo = mecanismo,
                    activo = tipoFirma.simula(mecanismo)
                )
            }
        }
    }
}

@Composable
fun MecanismoSimuladoFila(mecanismo: MecanismoSimulado, activo: Boolean) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = if (activo) Color.White else Color(0xFFF5F5F5),
        border = BorderStroke(
            width = 1.dp,
            color = if (activo) Color(0xFF2563EB) else Color.LightGray
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    mecanismo.titulo,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = if (activo) Color(0xFF1E3A8A) else Color.DarkGray
                )
                if (activo) {
                    Surface(color = Color(0xFF2563EB), shape = RoundedCornerShape(4.dp)) {
                        Text(
                            "Activo",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            fontSize = 9.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Text(mecanismo.etiqueta, fontSize = 9.sp, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(mecanismo.descripcion, fontSize = 10.sp, color = Color.Gray)
        }
    }
}
