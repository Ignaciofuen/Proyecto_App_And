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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.myapplication.data.AppState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(navController: NavController, appState: AppState){

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Registro de Usuario") }) }
    ) { padding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email")},
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña")},
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar Contraseña") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            //Muestro los errores si es que error NO está vacío
            if (error.isNotEmpty()) {
                Text(error, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    when {
                        email.isBlank() || password.isBlank() || confirmPassword.isBlank() ->
                            error = "Todos los campos son obligatorios"
                        !email.contains("@") ->
                            error = "Email no válido"
                        password.length < 4 ->
                            error = "La contraseña debe tener al menos 4 caracteres"
                        password != confirmPassword ->
                            error = "Las contraseñas no coinciden"
                        //Con los datos validados llamo al registrarUsuario()
                        //si el usuario es nuevo lo graba y retorna true
                        !appState.registrarUsuario(email, password) ->
                            error = "El usuario ya existe"
                        else -> {
                            error = ""
                            navController.navigate("login")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrarse")
            }
        }
    }
}