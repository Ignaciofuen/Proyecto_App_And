package com.example.form.view

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.form.viewmodel.UsuarioViewModel
import com.myapplication.R
import com.myapplication.data.model.UsuarioDto
import com.myapplication.viewmodel.PostUsuarioViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    navController: NavController,
    viewModel: PostUsuarioViewModel,
    formViewModel: UsuarioViewModel = viewModel()
) {
    val estado by formViewModel.estado.collectAsState()
    val error by viewModel.error.collectAsState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro de usuario") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onSecondary
                ),
                actions = {
                    Icon(
                        painter = painterResource(id = R.drawable.newuser),
                        contentDescription = "User icon",
                        tint = Color.White,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            )
        }
    ) { padding ->

        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Ingresa los datos requeridos.",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ----------- EMAIL -----------
            OutlinedTextField(
                value = estado.correo,
                onValueChange = formViewModel::onCorreoChange,
                label = { Text("Ingresa tu e-mail") },
                isError = estado.errores.correo != null,
                supportingText = {
                    estado.errores.correo?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            // ----------- CONTRASEÑA -----------
            OutlinedTextField(
                value = estado.clave,
                onValueChange = formViewModel::onClaveChange,
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                isError = estado.errores.clave != null,
                supportingText = {
                    estado.errores.clave?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            // ----------- REPETIR CONTRASEÑA -----------
            OutlinedTextField(
                value = estado.repetirClave,
                onValueChange = formViewModel::onRepetirClaveChange,
                label = { Text("Repetir contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                isError = estado.errores.repetirClave != null,
                supportingText = {
                    estado.errores.repetirClave?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ----------- ERROR DEL BACKEND -----------
            error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // ----------- BOTÓN REGISTRO -----------
            Button(
                onClick = {

                    if (formViewModel.validarRegistro()) {

                        val emailLimpio = estado.correo.trim().lowercase()
                        val rolAsignado =
                            if (emailLimpio.endsWith("@admin.cl")) "ADMIN" else "USER"

                        val nuevoUsuario = UsuarioDto(
                            idusu = 0,
                            email = emailLimpio,
                            password = estado.clave,
                            rol = rolAsignado
                        )

                        scope.launch {

                            viewModel.registrarUsuario(nuevoUsuario)

                            if (viewModel.error.value == null) {
                                navController.navigate("login") {
                                    popUpTo("registro") { inclusive = true }
                                }
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrarse")
            }

            TextButton(
                onClick = { navController.navigate("login") }
            ) {
                Text("¿Ya tienes una cuenta creada? Inicia sesión")
            }
        }
    }
}
