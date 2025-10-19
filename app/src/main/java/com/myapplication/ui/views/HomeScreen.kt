package com.myapplication.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.myapplication.data.AppState
import com.myapplication.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, appState: AppState) {
    val usuario = appState.usuarioActual

    Scaffold(
        topBar = {
            TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.secondary,
                ),
                title = {
                    Text("Home")
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
                text = "¡Bienvenido a Level-Up!",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(id = R.drawable.levelupgamerimg),
                contentDescription = "Logo de bienvenida de la tienda",
                modifier = Modifier
                    .height(400.dp)
                    .fillMaxWidth(0.7f),
                contentScale = ContentScale.Fit
            )

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

            Button(onClick = {navController.navigate("productos") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )

            ) {

                Text("Ver catálogo de productos")

            }
        }
    }
}