package com.myapplication.ui.views

import android.text.style.IconMarginSpan
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.myapplication.data.AppState

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import com.myapplication.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductosScreen(navController: NavController, appState: AppState) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catálogo de Productos") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.volver),
                            contentDescription = "Volver"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("carrito") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.carrito),
                            contentDescription = "Carrito"
                        )
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(18.dp)
        ) {

            // Usamos appState.productos
            items(appState.productos) { producto ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Image(
                            painter = painterResource(producto.imagen),
                            contentDescription = producto.nombre,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),

                            contentScale = ContentScale.Fit
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

                        // Botón para agregar al carrito
                        Button(
                            onClick = {
                                appState.agregarAlCarrito(producto)
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