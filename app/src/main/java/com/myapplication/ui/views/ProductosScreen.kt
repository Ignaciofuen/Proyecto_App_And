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
import com.myapplication.data.Productos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductosScreen(navController: NavController, appState: AppState) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Catálogo de Productos") },
                navigationIcon = {
                    Text(
                        text = "<-",
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .clickable { navController.popBackStack() }
                    )
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


            items(Productos) { producto ->
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
                        Spacer(Modifier.height(8.dp))
                        Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
                        Text("Precio: $${producto.precio}")
                        Text(producto.descripcion, style = MaterialTheme.typography.bodySmall)

                        Button(onClick = {}){
                            Text("Agregar Carrito")
                        }
                    }
                }
            }
        }
    }
}
