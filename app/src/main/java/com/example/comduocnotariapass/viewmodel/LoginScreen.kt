package com.example.comduocnotariapass.viewmodel

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.comduocnotariapass.R

@Composable
fun LoginScreen(
    onLoginClick: (String, String) -> Unit,
    onDemoClick: () -> Unit,
    onRegisterClick: () -> Unit,
    error: String? = null,
    cargando: Boolean = false
) {
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(40.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_notaria_pass),
            contentDescription = "Logo de NotariaPass",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .width(340.dp)
                .aspectRatio(1980f / 521f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Tus trámites, paso a paso.", fontWeight = FontWeight.Bold)
                Text("Notaría digital en Chile", fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Te damos la bienvenida", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text("Accede con tu cuenta o explora el recorrido sin iniciar sesion.", fontSize = 12.sp)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = clave,
            onValueChange = { clave = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        if (error != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onLoginClick(correo, clave) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !cargando
        ) {
            Text(if (cargando) "Ingresando..." else "Ingresar")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onDemoClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = !cargando
        ) {
            Text("Entrar sin cuenta")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onRegisterClick) {
            Text("¿No tienes cuenta? Registrate aquí")
        }
    }
}