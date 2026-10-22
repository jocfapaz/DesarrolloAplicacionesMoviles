package com.example.comduocnotariapass.viewmodel

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.comduocnotariapass.model.Documento
import com.example.comduocnotariapass.model.EstadoTramite
import com.example.comduocnotariapass.model.FormatoTramite
import com.example.comduocnotariapass.model.observacionDemo
import com.example.comduocnotariapass.util.MIME_PERMITIDOS
import com.example.comduocnotariapass.util.ResultadoValidacion
import com.example.comduocnotariapass.util.detalleDocumento
import com.example.comduocnotariapass.util.leerDocumento
import com.example.comduocnotariapass.util.mensajeError
import com.example.comduocnotariapass.util.validarDocumento

@Composable
fun DetalleTramiteScreen(
    formatoSeleccionado: FormatoTramite,
    documentosPropios: List<Documento>,
    estadoTramite: EstadoTramite,
    onAgregarDocumento: (Documento) -> Unit,
    onReenviarRevision: () -> Unit,
    puedeVolver: Boolean,
    puedeAvanzar: Boolean,
    onVolver: () -> Unit,
    onAvanzar: () -> Unit
) {
    val context = LocalContext.current
    var comuna by remember { mutableStateOf("Comuna Demo") }
    var errorArchivo by remember { mutableStateOf("") }

    val observacion = remember(formatoSeleccionado.id) { observacionDemo(formatoSeleccionado) }
    val documentos = formatoSeleccionado.documentosInclusos + documentosPropios

    // Selector real de archivos del dispositivo (SAF): no requiere permisos
    val selectorArchivos = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        val documento = context.leerDocumento(uri)
        when (val resultado = validarDocumento(documento)) {
            ResultadoValidacion.VALIDO -> {
                errorArchivo = ""
                onAgregarDocumento(documento)
            }

            else -> errorArchivo = mensajeError(resultado)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FC))
            .statusBarsPadding()
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
            Text("Corregir y reenviar", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Surface(color = Color(0xFFE0E7FF), shape = RoundedCornerShape(4.dp)) {
                Text("DEMO", modifier = Modifier.padding(6.dp, 2.dp), fontSize = 11.sp, color = Color(0xFF1E3A8A))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Indicador de estado: "Con observaciones"
        ChipEstadoTramite(estado = estadoTramite)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Necesita una corrección", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(
            "Revisa los comentarios del notario, adjunta lo solicitado y reenvía tu trámite.",
            fontSize = 13.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Caja con comentarios del notario
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7))
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = null,
                        tint = Color(0xFFD97706),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "Comentarios del notario (${observacion.comentarios.size})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color(0xFF92400E)
                    )
                }

                observacion.comentarios.forEach { comentario ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "• ${comentario.texto}",
                        fontSize = 11.sp,
                        color = Color(0xFFB45309)
                    )
                    Text(
                        buildString {
                            append(comentario.autor)
                            append(" · ")
                            append(comentario.momento)
                            comentario.documento?.let {
                                append(" · ")
                                append(it)
                            }
                        },
                        fontSize = 9.sp,
                        color = Color(0xFF92400E)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Documentos asociados adjuntos
        Text("Documentos asociados (${documentos.size})", fontSize = 14.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(8.dp))

        documentos.forEach { documento ->
            DocumentoAsociadoItem(
                documento = documento,
                observado = observacion.estaObservado(documento)
            )
            Spacer(modifier = Modifier.height(6.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botón para volver a adjuntar
        OutlinedButton(
            onClick = { selectorArchivos.launch(MIME_PERMITIDOS.toTypedArray()) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Volver a adjuntar documento")
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.AttachFile,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("PDF, PNG o JPG, hasta 10 MB por archivo", fontSize = 10.sp, color = Color.Gray)
        }

        if (errorArchivo.isNotBlank()) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(errorArchivo, fontSize = 11.sp, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Edición de Comuna
        Text("Comuna · Dato ", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        OutlinedTextField(
            value = comuna,
            onValueChange = { comuna = it },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onReenviarRevision,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Corregir y reenviar", color = Color.White)
        }
    }
}

@Composable
private fun DocumentoAsociadoItem(documento: Documento, observado: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Description,
                contentDescription = null,
                tint = if (observado) Color(0xFFD97706) else Color(0xFF2563EB),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(documento.nombre, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text(
                    buildString {
                        append(detalleDocumento(documento))
                        append(if (documento.esPropio) " · Adjuntado por ti" else " · Del formato")
                    },
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                color = if (observado) Color(0xFFFEF3C7) else Color(0xFFDCFCE7),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    if (observado) "Con observación" else "Sin cambios",
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (observado) Color(0xFFB45309) else Color(0xFF15803D)
                )
            }
        }
    }
}
