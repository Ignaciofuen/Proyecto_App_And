package com.myapplication.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.myapplication.R
import com.myapplication.data.AppState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(navController: NavController, appState: AppState) {
    val usuario = appState.usuarioActual

    Scaffold(
        topBar = {
            TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.onBackground,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                title = {
                    Text("ADMINISTRADOR")
                },
                actions = {
                    IconButton(onClick = { navController.navigate("login") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.logout),
                            contentDescription = "Logout"
                        )
                    }
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
                text = "¡Bienvenido al panel de administrador!",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            usuario?.let {
                Text(
                    text = "Has iniciado sesión como:",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = it.email,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = {navController.navigate("productosAdmin") }) {
                Text("Administrar productos")
            }
        }
    }
}