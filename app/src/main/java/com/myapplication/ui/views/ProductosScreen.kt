package com.myapplication.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.myapplication.R
import com.myapplication.viewmodel.ProductoViewModel
import com.myapplication.viewmodel.PostUsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductosScreen(
    navController: NavController,
    productoViewModel: ProductoViewModel = viewModel(),
    usuarioViewModel: PostUsuarioViewModel = viewModel() 
) {

    val productos by productoViewModel.productos.collectAsState()
    val carrito by usuarioViewModel.carrito.collectAsState()
    val errorProductos by productoViewModel.error.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catálogo de Productos") },
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
                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { navController.navigate("carrito") }) {
                            Icon(
                                painter = painterResource(id = R.drawable.carrito),
                                contentDescription = "Carrito",
                                tint = Color(0xFFFFFFFF)
                            )
                        }

                        Text(
                            text = "(${carrito.size})",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                }

            )
        }
    ) { padding ->

        if (errorProductos != null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text(text = "Error al cargar productos: $errorProductos")
            }
        } else if (productos.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator() // Muestra un spinner mientras carga
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(18.dp)
            ) {


                items(productos) { producto ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {


                            AsyncImage(
                                model = producto.imagen, // Carga la URL
                                contentDescription = producto.nombre,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp),
                                contentScale = ContentScale.Fit,
                                placeholder = painterResource(id = R.drawable.pc2)
                            )

                            Spacer(Modifier.height(6.dp))
                            Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(6.dp))

                            Text(
                                "$${producto.precio}",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(Modifier.height(6.dp))
                            Text(producto.descripcion, style = MaterialTheme.typography.bodySmall)
                            Spacer(Modifier.height(6.dp))

                            Button(
                                onClick = {
                                    usuarioViewModel.agregarAlCarrito(producto)
                                },
                                modifier = Modifier.fillMaxWidth()
                            ){
                                Text("Agregar al carrito")
                            }
                        }
                    }
                }
            }
        }
    }
}