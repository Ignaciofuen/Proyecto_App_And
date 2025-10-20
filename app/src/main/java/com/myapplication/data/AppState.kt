package com.myapplication.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import androidx.annotation.DrawableRes

data class Usuario(val email: String, val password: String)

class AppState(private val dataStore: DataStoreManager) {
    val usuarios = mutableStateListOf<Usuario>()
    var usuarioActual: Usuario? = null

    private val _carritos = mutableStateMapOf<String, List<Producto>>()
    private val _carrito = mutableStateListOf<Producto>()
    val carrito: List<Producto> get() = _carrito

    private val _productos = mutableStateListOf<Producto>()
    val productos: List<Producto> get() = _productos

    private val scope = CoroutineScope(Dispatchers.IO)


    fun cargarDatos() {
        scope.launch {
            val users = dataStore.getUsers().first()
            usuarios.clear()
            usuarios.addAll(users)

            val prods = dataStore.getProducts().first()
            _productos.clear()
            if (prods == null) {

                _productos.addAll(Productos)
                guardarProductos()
            } else {
                _productos.addAll(prods)
            }




            val carts = dataStore.getCarts().first()
            _carritos.clear()
            _carritos.putAll(carts)
        }

    }

    fun registrarUsuario(email: String, password: String): Boolean {
        if (usuarios.any { it.email == email }) return false
        val nuevo = Usuario(email, password)
        usuarios.add(nuevo)
        guardarUsuarios()
        return true
    }

    fun login(email: String, password: String): Boolean {
        val user = usuarios.find { it.email == email && it.password == password }
        return if (user != null) {
            usuarioActual = user
            true
        } else false
    }

    fun logout() {
        usuarioActual = null
    }

    fun agregarProducto(nombre: String, descripcion: String, precio: Int, @DrawableRes imagen: Int, categoria: String) {
        val nuevoId = (_productos.maxOfOrNull { it.id } ?: 0) + 1
        val nuevoProducto = Producto(nuevoId, nombre, precio, imagen, descripcion, categoria)
        _productos.add(nuevoProducto)
        guardarProductos()
    }

    fun eliminarProducto(producto: Producto) {
        _productos.remove(producto)


        _carritos.forEach { (email, cart) ->
            _carritos[email] = cart.filter { it.id != producto.id }
        }

        _carrito.removeAll { it.id == producto.id }

        guardarProductos()
        guardarCarritos()
    }

    fun editarProducto(idProducto: Int, nombre: String, descripcion: String, precio: Int, @DrawableRes imagen: Int, categoria: String) {
        val indice = _productos.indexOfFirst { it.id == idProducto }
        if (indice != -1) {
            val actualizado = Producto(idProducto, nombre, precio, imagen, descripcion, categoria)
            _productos[indice] = actualizado


            _carritos.forEach { (email, cart) ->
                _carritos[email] = cart.map { if (it.id == idProducto) actualizado else it }
            }

            _carrito.replaceAll { if (it.id == idProducto) actualizado else it }

            guardarProductos()
            guardarCarritos() // Guardar los carritos actualizados
        }
    }

    private fun guardarProductos() {
        scope.launch {
            dataStore.saveProducts(_productos.toList())
        }
    }



    fun agregarAlCarrito(producto: Producto) {
        _carrito.add(producto)
        guardarCarritoActual()
    }

    fun eliminarDelCarrito(producto: Producto) {
        _carrito.remove(producto)
        guardarCarritoActual()
    }

    fun vaciarCarrito() {
        _carrito.clear()
        guardarCarritoActual()
    }
    private fun guardarCarritos() {
        scope.launch {
            dataStore.saveCarts(_carritos.toMap())
        }
    }



    private fun guardarCarritoActual() {
        usuarioActual?.let { user ->
            _carritos[user.email] = _carrito.toList()
            guardarCarritos()
        }
    }






    // Guardar usuarios en DataStore
    private fun guardarUsuarios() {
        scope.launch {
            dataStore.saveUsers(usuarios)
        }
    }





}

