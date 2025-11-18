package com.myapplication.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.form.viewmodel.UsuarioViewModel
import com.myapplication.R
import com.myapplication.viewmodel.PostUsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,

    formViewModel: UsuarioViewModel = viewModel(),
    authViewModel: PostUsuarioViewModel = viewModel()
) {
    // 2. Observamos los estados
    val formEstado by formViewModel.estado.collectAsState()
    val authUsuario by authViewModel.usuarioActual.collectAsState()
    val authError by authViewModel.error.collectAsState()

    // 3. Efecto que reacciona al login
    LaunchedEffect(authUsuario) {
        authUsuario?.let {

            val ruta = if (it.rol.equals("ADMIN", ignoreCase = true)) "admin" else "home"
            navController.navigate(ruta) {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Inicio de Sesión") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onSecondary
                ),
                actions = {
                    Icon(
                        painter = painterResource(id = R.drawable.user),
                        contentDescription = "User icon",
                        tint = Color(0xFFFFFFFF),
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "¡Hola! A continuación debes iniciar sesión o registrarte.",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = formEstado.correo,
                onValueChange = formViewModel::onCorreoChange,
                label = { Text("Ingresa tu e-mail") },
                isError = formEstado.errores.correo != null,
                supportingText = {
                    formEstado.errores.correo?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = formEstado.clave,
                onValueChange = formViewModel::onClaveChange,
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                isError = formEstado.errores.clave != null,
                supportingText = {
                    formEstado.errores.clave?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            authError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }


            Button(
                onClick = {
                    authViewModel.login(formEstado.correo.trim().lowercase(), formEstado.clave)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Iniciar Sesión")
            }


            TextButton(
                onClick = { navController.navigate("registro") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("¿No tienes cuenta? Regístrate")
            }
        }
    }
}