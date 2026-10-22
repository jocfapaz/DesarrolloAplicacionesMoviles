package com.example.comduocnotariapass.viewmodel

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
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
import com.example.comduocnotariapass.model.FormatoTramite
import com.example.comduocnotariapass.util.MIME_PERMITIDOS
import com.example.comduocnotariapass.util.ResultadoValidacion
import com.example.comduocnotariapass.util.detalleDocumento
import com.example.comduocnotariapass.util.leerDocumento
import com.example.comduocnotariapass.util.mensajeError
import com.example.comduocnotariapass.util.validarDocumento

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevoTramiteScreen(
    formatos: List<FormatoTramite>,
    formatoSeleccionado: FormatoTramite,
    documentosPropios: List<Documento>,
    onSeleccionarFormato: (FormatoTramite) -> Unit,
    onAgregarDocumento: (Documento) -> Unit,
    onQuitarDocumento: (String) -> Unit,
    onContinuarRevisionClick: () -> Unit,
    puedeVolver: Boolean,
    puedeAvanzar: Boolean,
    onVolver: () -> Unit,
    onAvanzar: () -> Unit
) {
    val context = LocalContext.current
    var menuExpandido by remember { mutableStateOf(false) }
    var errorArchivo by remember { mutableStateOf("") }

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
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        BarraNavegacionEsquinas(
            puedeVolver = puedeVolver,
            puedeAvanzar = puedeAvanzar,
            onVolver = onVolver,
            onAvanzar = onAvanzar
        )

        Text("Nuevo trámite", fontWeight = FontWeight.Bold, fontSize = 18.sp)

        Spacer(modifier = Modifier.height(8.dp))

        Text("Paso 1 de 3: Documentos", fontSize = 12.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Selección de Trámite y Archivos", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(
            "Elige un formato predefinido y adjunta tus propios documentos.",
            fontSize = 13.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        // SELECCIÓN DE FORMATO PREDEFINIDO
        Text("Trámite / Formato", fontSize = 12.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = menuExpandido,
            onExpandedChange = { menuExpandido = it }
        ) {
            OutlinedTextField(
                value = formatoSeleccionado.nombre,
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                readOnly = true,
                label = { Text("Formato del trámite") },
                supportingText = {
                    Text(formatoSeleccionado.descripcion, fontSize = 11.sp, color = Color.Gray)
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuExpandido)
                }
            )

            ExposedDropdownMenu(
                expanded = menuExpandido,
                onDismissRequest = { menuExpandido = false }
            ) {
                formatos.forEach { formato ->
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text(
                                    formato.nombre,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color(0xFF1E3A8A)
                                )
                                Text(
                                    formato.descripcion,
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                        },
                        onClick = {
                            onSeleccionarFormato(formato)
                            menuExpandido = false
                        },
                        trailingIcon = {
                            if (formato.id == formatoSeleccionado.id) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = "Seleccionado",
                                    tint = Color(0xFF2563EB)
                                )
                            }
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // DOCUMENTOS INCLUIDOS EN EL FORMATO
        Text(
            "Documentos incluidos en el formato (${formatoSeleccionado.documentosInclusos.size})",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        formatoSeleccionado.documentosInclusos.forEach { documento ->
            DocumentoItem(documento = documento, esDelFormato = true)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(12.dp))

        // SECCIÓN PARA ADJUNTAR DOCUMENTOS PROPIOS
        Text(
            "Documentos propios adjuntos (${documentosPropios.size})",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (documentosPropios.isEmpty()) {
            Surface(
                color = Color(0xFFF5F5F5),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Todavía no has adjuntado documentos propios.",
                    modifier = Modifier.padding(14.dp),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        } else {
            documentosPropios.forEach { documento ->
                DocumentoItem(
                    documento = documento,
                    onEliminar = { onQuitarDocumento(documento.id) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botón para adjuntar un documento más
        OutlinedButton(
            onClick = { selectorArchivos.launch(MIME_PERMITIDOS.toTypedArray()) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("+ Adjuntar otro documento")
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
fun DocumentoItem(
    documento: Documento,
    modifier: Modifier = Modifier,
    esDelFormato: Boolean = false,
    onEliminar: (() -> Unit)? = null
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (esDelFormato) Color(0xFFF5F5F5) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (esDelFormato) 0.dp else 1.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Description,
                    contentDescription = null,
                    tint = Color(0xFF2563EB),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(documento.nombre, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(detalleDocumento(documento), fontSize = 10.sp, color = Color.Gray)
                }
            }

            if (esDelFormato) {
                Surface(color = Color(0xFFE0E7FF), shape = RoundedCornerShape(4.dp)) {
                    Text(
                        "del formato",
                        modifier = Modifier.padding(6.dp, 2.dp),
                        fontSize = 10.sp,
                        color = Color(0xFF1E3A8A)
                    )
                }
            } else if (onEliminar != null) {
                IconButton(onClick = onEliminar) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar ${documento.nombre}",
                        tint = Color(0xFFD97706)
                    )
                }
            }
        }
    }
}
