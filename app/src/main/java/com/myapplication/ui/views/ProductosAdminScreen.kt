package com.myapplication.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
// --- NINGUNA importación de androidx.compose.material.icons ---
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.myapplication.R
import com.myapplication.data.AppState
import com.myapplication.data.Producto
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductosAdminScreen(navController: NavController, appState: AppState) {

    // Estados para controlar los dialogs
    var mostrarDialogoAgregar by remember { mutableStateOf(false) }
    var mostrarDialogoEditar by remember { mutableStateOf(false) }
    var mostrarDialogoEliminar by remember { mutableStateOf(false) }

    // Estado para el producto seleccionado (para editar/eliminar)
    var productoSeleccionado by remember { mutableStateOf<Producto?>(null) }

    // Formateador de moneda
    val formatter = remember {
        NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply {
            maximumFractionDigits = 0
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Administrar Productos") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.volver),
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        },
        // --- INICIO DE LA MODIFICACIÓN FINAL ---
        floatingActionButton = {
            // Botón para AGREGAR (ahora con texto)
            FloatingActionButton(onClick = {
                productoSeleccionado = null // Limpiar selección
                mostrarDialogoAgregar = true
            }) {
                // Se usa Text en lugar de Icon
                Text("Agregar", modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
        // --- FIN DE LA MODIFICACIÓN FINAL ---
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            items(appState.productos) { producto ->
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
                        Image(
                            painter = painterResource(producto.imagen),
                            contentDescription = producto.nombre,
                            modifier = Modifier
                                .size(60.dp)
                                .padding(end = 12.dp),
                            contentScale = ContentScale.Fit
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

                        // Botón EDITAR (con texto)
                        TextButton(onClick = {
                            productoSeleccionado = producto
                            mostrarDialogoEditar = true
                        }) {
                            Text("Editar")
                        }

                        // Botón ELIMINAR (con texto y color)
                        TextButton(onClick = {
                            productoSeleccionado = producto
                            mostrarDialogoEliminar = true
                        }) {
                            Text(
                                "Eliminar",
                                color = MaterialTheme.colorScheme.error // Color rojo
                            )
                        }
                    }
                }
            }
        }
    }

    // --- DIALOGS (Sin cambios) ---

    // 1. Dialogo para AGREGAR producto
    if (mostrarDialogoAgregar) {
        FormularioProductoDialog(
            onDismiss = { mostrarDialogoAgregar = false },
            onSave = { nombre, desc, precio, imgRes, categoria ->
                val imagen = if(imgRes == 0) R.drawable.pc2 else imgRes
                appState.agregarProducto(nombre, desc, precio, imagen, categoria)
                mostrarDialogoAgregar = false
            }
        )
    }

    // 2. Dialogo para EDITAR producto
    if (mostrarDialogoEditar && productoSeleccionado != null) {
        FormularioProductoDialog(
            producto = productoSeleccionado,
            onDismiss = { mostrarDialogoEditar = false },
            onSave = { nombre, desc, precio, imgRes, categoria ->
                val imagen = if(imgRes == 0) productoSeleccionado!!.imagen else imgRes
                appState.editarProducto(productoSeleccionado!!.id, nombre, desc, precio, imagen, categoria)
                mostrarDialogoEditar = false
            }
        )
    }

    // 3. Dialogo para CONFIRMAR ELIMINACIÓN
    if (mostrarDialogoEliminar && productoSeleccionado != null) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoEliminar = false },
            title = { Text("Confirmar Eliminación") },
            text = { Text("¿Estás seguro de que deseas eliminar '${productoSeleccionado!!.nombre}'?") },
            confirmButton = {
                Button(
                    onClick = {
                        appState.eliminarProducto(productoSeleccionado!!)
                        mostrarDialogoEliminar = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Eliminar")
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


/**
 * Dialogo reutilizable para Agregar y Editar productos.
 * (Este Composable no tuvo cambios)
 */
@Composable
fun FormularioProductoDialog(
    producto: Producto? = null,
    onDismiss: () -> Unit,
    onSave: (nombre: String, descripcion: String, precio: Int, imagenResId: Int, categoria: String) -> Unit
) {
    // Estados para los campos del formulario
    var nombre by remember { mutableStateOf(producto?.nombre ?: "") }
    var descripcion by remember { mutableStateOf(producto?.descripcion ?: "") }
    var categoria by remember { mutableStateOf(producto?.categoria ?: "") }
    var precio by remember { mutableStateOf(producto?.precio?.toString() ?: "") }
    var imagenResId by remember { mutableStateOf(producto?.imagen?.toString() ?: "") }

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
                            value = imagenResId,
                            onValueChange = { imagenResId = it },
                            label = { Text("ID Recurso Imagen (Ej: R.drawable.img)") },
                            placeholder = { Text(if(producto != null) "Dejar vacío para no cambiar" else "") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val precioInt = precio.toIntOrNull() ?: 0
                val imagenInt = imagenResId.toIntOrNull() ?: 0

                onSave(nombre, descripcion, precioInt, imagenInt, categoria)
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