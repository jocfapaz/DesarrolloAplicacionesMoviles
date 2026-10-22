package com.example.comduocnotariapass.viewmodel

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.comduocnotariapass.model.EstadoTramite
import com.example.comduocnotariapass.model.PasoProgreso
import com.example.comduocnotariapass.model.ResultadoRevision
import com.example.comduocnotariapass.model.TipoFirma

private val Azul = Color(0xFF2563EB)
private val Verde = Color(0xFF10B981)
private val Ambar = Color(0xFFD97706)
private val GrisPendiente = Color(0xFFD1D5DB)
private val GrisTexto = Color(0xFF9CA3AF)

@Composable
fun EncabezadoEstadoTramite(
    estado: EstadoTramite,
    tipoFirma: TipoFirma,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("ID de seguimiento", fontSize = 11.sp, color = Color.Gray)
            ChipEstadoTramite(estado = estado)
        }

        Text(
            estado.id,
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF111827)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(color = Color(0xFFEFF6FF), shape = RoundedCornerShape(4.dp)) {
                Text(
                    "${tipoFirma.nombre} · Simulada",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Azul
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text("Modalidad de firma", fontSize = 10.sp, color = Color.Gray)
        }
    }
}

@Composable
fun ChipEstadoTramite(estado: EstadoTramite, modifier: Modifier = Modifier) {
    val color = colorDelEstado(estado)
    Surface(color = color.copy(alpha = 0.15f), shape = RoundedCornerShape(4.dp), modifier = modifier) {
        Text(
            estado.etiquetaEstado,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
fun LineaProgresoTramite(estado: EstadoTramite, modifier: Modifier = Modifier) {
    var expandido by remember { mutableStateOf<PasoProgreso?>(null) }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Progreso del trámite", fontWeight = FontWeight.Bold, fontSize = 14.sp)

            Spacer(modifier = Modifier.height(12.dp))

            PasoProgreso.entries.forEachIndexed { indice, paso ->
                FilaPasoProgreso(
                    paso = paso,
                    estado = estado,
                    esUltimo = indice == PasoProgreso.entries.lastIndex,
                    expandido = expandido == paso,
                    onToggle = { expandido = if (expandido == paso) null else paso }
                )
            }
        }
    }
}

@Composable
private fun FilaPasoProgreso(
    paso: PasoProgreso,
    estado: EstadoTramite,
    esUltimo: Boolean,
    expandido: Boolean,
    onToggle: () -> Unit
) {
    val completado = estado.estaCompletado(paso)
    val actual = estado.esActual(paso)
    val tocable = estado.esTocable(paso)
    val color = colorDelPaso(paso, estado, completado, actual)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clickable(enabled = tocable, onClick = onToggle)
    ) {
        Box(modifier = Modifier.width(24.dp).fillMaxHeight()) {
            if (!esUltimo) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 22.dp)
                        .width(2.dp)
                        .fillMaxHeight()
                        .background(if (completado) Verde else Color(0xFFE5E7EB))
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(if (completado || actual) color else Color.White)
                    .border(2.dp, color, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (completado) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                } else if (actual) {
                    Box(Modifier.size(7.dp).clip(CircleShape).background(Color.White))
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = if (esUltimo) 0.dp else 18.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    estado.tituloDe(paso),
                    modifier = Modifier.weight(1f),
                    fontSize = 13.sp,
                    fontWeight = if (actual) FontWeight.Bold else FontWeight.SemiBold,
                    color = when {
                        actual -> Color(0xFF111827)
                        completado -> Color(0xFF374151)
                        else -> GrisTexto
                    }
                )
                if (tocable) {
                    Icon(
                        imageVector = if (expandido) {
                            Icons.Default.KeyboardArrowUp
                        } else {
                            Icons.Default.KeyboardArrowDown
                        },
                        contentDescription = if (expandido) "Ocultar detalle" else "Ver detalle",
                        tint = GrisTexto,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Text(
                when {
                    completado -> "Completado"
                    actual -> "En curso"
                    else -> "Pendiente"
                },
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = when {
                    completado -> Verde
                    actual -> color
                    else -> GrisTexto
                }
            )

            if (expandido) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFF3F4F6),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            estado.descripcionDe(paso),
                            fontSize = 11.sp,
                            color = Color(0xFF4B5563)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "${paso.responsable} · ${estado.momentoDe(paso)}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = GrisTexto
                        )
                    }
                }
            }
        }
    }
}

private fun colorDelEstado(estado: EstadoTramite): Color = when (estado.resultado) {
    ResultadoRevision.OBSERVACIONES -> Ambar
    ResultadoRevision.FINALIZADO -> Verde
    null -> Azul
}

private fun colorDelPaso(
    paso: PasoProgreso,
    estado: EstadoTramite,
    completado: Boolean,
    actual: Boolean
): Color = when {
    paso == PasoProgreso.RESULTADO && estado.resultado == ResultadoRevision.OBSERVACIONES -> Ambar
    paso == PasoProgreso.RESULTADO && estado.resultado == ResultadoRevision.FINALIZADO -> Verde
    actual -> Azul
    completado -> Verde
    else -> GrisPendiente
}
