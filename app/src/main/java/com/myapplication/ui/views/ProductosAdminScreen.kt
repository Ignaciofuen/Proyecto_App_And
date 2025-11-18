package com.myapplication.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.myapplication.R
import com.myapplication.data.model.ProductoDto
import com.myapplication.viewmodel.ProductoViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductosAdminScreen(
    navController: NavController,
    viewModel: ProductoViewModel = viewModel()
) {
    var mostrarDialogoAgregar by remember { mutableStateOf(false) }
    var mostrarDialogoEditar by remember { mutableStateOf(false) }
    var mostrarDialogoEliminar by remember { mutableStateOf(false) }

    var productoSeleccionado by remember { mutableStateOf<ProductoDto?>(null) } // <-- Usa el DTO

    // 1. Observa los productos desde el ViewModel
    val productos by viewModel.productos.collectAsState()

    val formatter = remember {
        NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply {
            maximumFractionDigits = 0
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Gestión de productos") },
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
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                productoSeleccionado = null
                mostrarDialogoAgregar = true
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.addproducto),
                    contentDescription = "Agregar producto"
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            items(productos) { producto -> // 'producto' ahora es ProductoDto
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        AsyncImage(
                            model = producto.imagen, // Carga la URL
                            contentDescription = producto.nombre,
                            modifier = Modifier
                                .size(60.dp)
                                .padding(end = 12.dp),
                            contentScale = ContentScale.Fit,
                            placeholder = painterResource(id = R.drawable.pc2)
                        )

                        Column(modifier = Modifier.weight(1f)) {
                            Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
                            Text(producto.categoria, style = MaterialTheme.typography.bodySmall)
                            Text(
                                formatter.format(producto.precio),
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        // Botón EDITAR
                        TextButton(onClick = {
                            productoSeleccionado = producto
                            mostrarDialogoEditar = true
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.editar),
                                contentDescription = "Editar producto"
                            )
                        }

                        // Botón ELIMINAR
                        TextButton(onClick = {
                            productoSeleccionado = producto
                            mostrarDialogoEliminar = true
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.borrar),
                                contentDescription = "Borrar producto"
                            )
                        }
                    }
                }
            }
        }
    }




    if (mostrarDialogoAgregar) {
        FormularioProductoDialog( // Llama al diálogo actualizado)
            onDismiss = { mostrarDialogoAgregar = false },
            onSave = { nombre, desc, precio, imgUrl, categoria ->
                val nuevoDto = ProductoDto(
                    id = 0, // El backend lo ignora y genera uno nuevo
                    nombre = nombre,
                    descripcion = desc,
                    categoria = categoria,
                    imagen = imgUrl,
                    precio = precio
                )
                viewModel.agregarProducto(nuevoDto) // Llama al VM
                mostrarDialogoAgregar = false
            }
        )
    }

    if (mostrarDialogoEditar && productoSeleccionado != null) {
        FormularioProductoDialog(
            producto = productoSeleccionado, // Pasa el DTO
            onDismiss = { mostrarDialogoEditar = false },
            onSave = { nombre, desc, precio, imgUrl, categoria ->
                val dtoActualizado = ProductoDto(
                    id = productoSeleccionado!!.id, // Usa el ID existente
                    nombre = nombre,
                    descripcion = desc,
                    categoria = categoria,
                    imagen = if(imgUrl.isBlank()) productoSeleccionado!!.imagen else imgUrl,
                    precio = precio
                )
                viewModel.editarProducto(dtoActualizado.id, dtoActualizado) // Llama al VM
                mostrarDialogoEditar = false
            }
        )
    }

    if (mostrarDialogoEliminar && productoSeleccionado != null) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoEliminar = false },
            title = { Text("Confirmar Eliminación") },
            text = { Text("¿Estás seguro de que deseas eliminar '${productoSeleccionado!!.nombre}'?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.eliminarProducto(productoSeleccionado!!) // Llama al VM
                        mostrarDialogoEliminar = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Eliminar") // Cambiado el icono por texto
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoEliminar = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}


@Composable
fun FormularioProductoDialog(
    producto: ProductoDto? = null,
    onDismiss: () -> Unit,

    onSave: (nombre: String, descripcion: String, precio: Int, imagenUrl: String, categoria: String) -> Unit
) {
    var nombre by remember { mutableStateOf(producto?.nombre ?: "") }
    var descripcion by remember { mutableStateOf(producto?.descripcion ?: "") }
    var categoria by remember { mutableStateOf(producto?.categoria ?: "") }
    var precio by remember { mutableStateOf(producto?.precio?.toString() ?: "") }
    var imagenUrl by remember { mutableStateOf(producto?.imagen ?: "") } // Usa el String

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (producto == null) "Agregar Producto" else "Editar Producto") },
        text = {
            LazyColumn {
                item {
                    Column {
                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { nombre = it },
                            label = { Text("Nombre") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(
                            value = descripcion,
                            onValueChange = { descripcion = it },
                            label = { Text("Descripción") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(
                            value = categoria,
                            onValueChange = { categoria = it },
                            label = { Text("Categoría") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(
                            value = precio,
                            onValueChange = { precio = it },
                            label = { Text("Precio") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(8.dp))


                        OutlinedTextField(
                            value = imagenUrl,
                            onValueChange = { imagenUrl = it },
                            label = { Text("URL de la Imagen (http://...)") },
                            placeholder = { Text(if(producto != null) "Dejar vacío para no cambiar" else "") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri), // Teclado para URLs
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val precioInt = precio.toIntOrNull() ?: 0

                onSave(nombre, descripcion, precioInt, imagenUrl, categoria)
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}