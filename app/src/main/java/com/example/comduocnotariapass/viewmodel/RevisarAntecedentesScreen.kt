package com.example.comduocnotariapass.viewmodel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.comduocnotariapass.model.DatosSolicitante
import com.example.comduocnotariapass.model.Documento
import com.example.comduocnotariapass.model.FormatoTramite
import com.example.comduocnotariapass.model.RolSolicitante
import com.example.comduocnotariapass.model.puedeContinuarVerificacion
import com.example.comduocnotariapass.util.detalleDocumento

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RevisarAntecedentesScreen(
    formatoSeleccionado: FormatoTramite,
    documentosPropios: List<Documento>,
    datosSolicitante: DatosSolicitante,
    antecedentesRevisados: Boolean,
    onActualizarDatos: (nombre: String, id: String, rol: RolSolicitante) -> Unit,
    onAntecedentesRevisadosChange: (Boolean) -> Unit,
    onContinuarFirma: () -> Unit,
    puedeVolver: Boolean,
    puedeAvanzar: Boolean,
    onVolver: () -> Unit,
    onAvanzar: () -> Unit
) {
    var menuRolExpandido by remember { mutableStateOf(false) }

    val documentos = formatoSeleccionado.documentosInclusos + documentosPropios
    val puedeContinuar = puedeContinuarVerificacion(datosSolicitante, antecedentesRevisados)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FC))
            .statusBarsPadding()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Botones de navegación en las esquinas
        BarraNavegacionEsquinas(
            puedeVolver = puedeVolver,
            puedeAvanzar = puedeAvanzar,
            onVolver = onVolver,
            onAvanzar = onAvanzar
        )

        // Título + badge
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Revisar antecedentes", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Surface(color = Color(0xFFE0E7FF), shape = RoundedCornerShape(4.dp)) {
                Text("DEMO", modifier = Modifier.padding(6.dp, 2.dp), fontSize = 11.sp, color = Color(0xFF1E3A8A))
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text("Paso 2 de 3", fontSize = 12.sp, color = Color(0xFF2563EB), fontWeight = FontWeight.Bold)
        Text("Revisa antes de seguir", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(
            "Comprueba tus datos, el formato y los archivos de tu borrador.",
            fontSize = 13.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tarjeta Resumen Borrador
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "NX-DEMO-024 · ${datosSolicitante.rol.nombre}",
                    fontSize = 11.sp,
                    color = Color(0xFF2563EB),
                    fontWeight = FontWeight.Bold
                )
                Text(formatoSeleccionado.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("Formato · Creado el 12 oct. 2026", fontSize = 12.sp, color = Color.Gray)

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.LightGray)

                Text("Correo: persona@demo.invalid", fontSize = 12.sp)
                Text("Región: Región · Dato ficticio", fontSize = 12.sp)
                Text("Comuna: Sin completar", fontSize = 12.sp, color = Color.Red)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // FORMULARIO DE VERIFICACIÓN DE DATOS
        Text("Verificación de datos", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Text("Corrige los datos si hace falta antes de continuar.", fontSize = 11.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = datosSolicitante.nombre,
            onValueChange = { nuevo ->
                onActualizarDatos(nuevo, datosSolicitante.id, datosSolicitante.rol)
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Nombre") },
            isError = !datosSolicitante.nombreCompleto,
            supportingText = {
                if (!datosSolicitante.nombreCompleto) {
                    Text("El nombre es obligatorio", fontSize = 11.sp, color = Color.Red)
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = datosSolicitante.id,
            onValueChange = { nuevo ->
                onActualizarDatos(datosSolicitante.nombre, nuevo, datosSolicitante.rol)
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("ID") },
            isError = !datosSolicitante.idCompleto,
            supportingText = {
                if (!datosSolicitante.idCompleto) {
                    Text("Ingresa tu número de identificación", fontSize = 11.sp, color = Color.Red)
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ROL DEL SOLICITANTE
        ExposedDropdownMenuBox(
            expanded = menuRolExpandido,
            onExpandedChange = { menuRolExpandido = it }
        ) {
            OutlinedTextField(
                value = datosSolicitante.rol.nombre,
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                readOnly = true,
                label = { Text("Rol") },
                supportingText = {
                    Text("Dato ficticio · sin RUT real", fontSize = 11.sp, color = Color.Gray)
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuRolExpandido)
                },
                shape = RoundedCornerShape(8.dp)
            )

            ExposedDropdownMenu(
                expanded = menuRolExpandido,
                onDismissRequest = { menuRolExpandido = false }
            ) {
                RolSolicitante.entries.forEach { rol ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                rol.nombre,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF1E3A8A)
                            )
                        },
                        onClick = {
                            onActualizarDatos(datosSolicitante.nombre, datosSolicitante.id, rol)
                            menuRolExpandido = false
                        },
                        trailingIcon = {
                            if (rol == datosSolicitante.rol) {
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

        Spacer(modifier = Modifier.height(16.dp))

        Text("Documentos (${documentos.size})", fontSize = 14.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(8.dp))

        // Lista de documentos
        documentos.forEach { documento ->
            DocumentoItemView(
                nombre = documento.nombre,
                subtext = "${detalleDocumento(documento)} · ${if (documento.esPropio) "Propio" else "Del formato"}"
            )
            Spacer(modifier = Modifier.height(6.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Checkbox confirmación de antecedentes revisados
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = antecedentesRevisados,
                onCheckedChange = onAntecedentesRevisadosChange
            )
            Text("Antecedentes revisados", fontSize = 12.sp, color = Color.DarkGray)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onContinuarFirma,
            enabled = puedeContinuar,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Continuar", color = Color.White)
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
