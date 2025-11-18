package com.myapplication.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.myapplication.R
import com.myapplication.viewmodel.PostUsuarioViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    navController: NavController,
    viewModel: PostUsuarioViewModel = viewModel()
) {

    val carrito by viewModel.carrito.collectAsState()

    val formatter = remember {
        NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply {
            maximumFractionDigits = 0
        }
    }
    LaunchedEffect(Unit) {
        viewModel.cargarCarrito()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Carrito de compras") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onSecondary
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.volver),
                            contentDescription = "Volver",
                            tint = Color(0xFFFFFFFF)
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
                                // 2. ¡CAMBIO CRÍTICO: USA COIL!
                                AsyncImage(
                                    model = producto.imagen, // Carga la URL
                                    contentDescription = producto.nombre,
                                    modifier = Modifier
                                        .size(80.dp)
                                        .padding(end = 12.dp),
                                    contentScale = ContentScale.Fit,
                                    placeholder = painterResource(id = R.drawable.pc2)
                                )

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
                                    Text(
                                        formatter.format(producto.precio), // Precio formateado
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }

                                // 3. Llama al ViewModel
                                Button(
                                    onClick = {
                                        viewModel.eliminarDelCarrito(producto)
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
                    text = "Items totales: ${carrito.size}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.End)
                )
                Text(
                    text = "Total: ${formatter.format(total)}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.End)
                )

                Spacer(Modifier.height(16.dp))

                // 4. Llama al ViewModel
                Button(
                    onClick = {
                        viewModel.vaciarCarrito()
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