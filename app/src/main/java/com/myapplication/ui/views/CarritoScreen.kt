package com.myapplication.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.myapplication.data.AppState
import com.myapplication.R
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(navController: NavController, appState: AppState) {

    val carrito = appState.carrito

    val formatter = remember {
        NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply {
            maximumFractionDigits = 0
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Carrito") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.volver),
                            contentDescription = "Volver"
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
                .padding(18.dp)
        ) {
            if (carrito.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Tu carrito está vacío",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1.0f)) {
                    items(carrito) { producto ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceAround,
                            ) {
                                Image(
                                    painter = painterResource(producto.imagen),
                                    contentDescription = producto.nombre,
                                    modifier = Modifier
                                        .size(80.dp)
                                        .padding(end = 12.dp),
                                    contentScale = ContentScale.Fit
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
                                    Text(
                                        formatter.format(producto.precio), // Precio formateado
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                                Button(
                                    onClick = {
                                        appState.eliminarDelCarrito(producto)
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.error
                                    )
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.borrar),
                                        contentDescription = "Borrar",
                                    )

                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                val total = carrito.sumOf { it.precio }

                Text(
                    text = "Total: ${formatter.format(total)}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.End)
                )

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {
                        appState.vaciarCarrito()
                        navController.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text("Pagar", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}