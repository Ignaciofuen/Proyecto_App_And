package com.myapplication.ui.views

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import com.myapplication.data.AppState
import com.myapplication.R
import com.myapplication.utils.mostrarNotificacion
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, appState: AppState) {
    val context = LocalContext.current
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    var ubicacion by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    try {
                        val geocoder = Geocoder(context, Locale.getDefault())
                        val direcciones = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        if (!direcciones.isNullOrEmpty()) {
                            val ciudad = direcciones[0].locality ?: "Ciudad desconocida"
                            val pais = direcciones[0].countryName ?: "País desconocido"
                            ubicacion = "$ciudad, $pais"
                        } else {
                            ubicacion = "Ubicación no disponible"
                        }
                    } catch (e: Exception) {
                        ubicacion = "Error obteniendo ubicación"
                        e.printStackTrace()
                    }
                } else {
                    ubicacion = "No se pudo obtener la ubicación"
                }
            }
        } else {
            ubicacion = "Permiso de ubicación no otorgado"
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Home") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onSecondary
                ),
                actions = {
                    IconButton(onClick = { navController.navigate("login") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.logout),
                            contentDescription = "Logout",
                            tint = Color.White
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
            verticalArrangement = Arrangement.Center ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("¡Bienvenido a Level-Up!", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id = R.drawable.levelupgamerimg),
                contentDescription = "Logo de bienvenida",
                modifier = Modifier.height(400.dp).fillMaxWidth(0.7f),
                contentScale = ContentScale.Fit

            )

            appState.usuarioActual?.let {
                Text("Has iniciado sesión como:", style = MaterialTheme.typography.bodyMedium)
                Text(it.email, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    mostrarNotificacion(
                        context = context,
                        titulo = "¡Nuevo catálogo disponible!",
                        mensaje = "Explora las últimas ofertas gamer 🎮"
                    )
                    navController.navigate("productos")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Ver catálogo de productos")
            }

            Spacer(modifier = Modifier.height(16.dp))

            ubicacion?.let {
                Text(
                    text = "\uD83D\uDCCD $it",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
    }
}


