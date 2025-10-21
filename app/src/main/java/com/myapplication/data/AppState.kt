package com.myapplication.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import androidx.annotation.DrawableRes



class AppState(
    private val dataStore: DataStoreManager,
    private val db: AppDatabase
) {

    private val usuarioDao = db.usuarioDao()
    private val productoDao = db.productoDao()

    val usuarios = mutableStateListOf<Usuario>()
    var usuarioActual: Usuario? = null

    private val _carritos = mutableStateMapOf<String, List<Producto>>()
    private val _carrito = mutableStateListOf<Producto>()
    val carrito: List<Producto> get() = _carrito

    private val _productos = mutableStateListOf<Producto>()
    val productos: List<Producto> get() = _productos

    private val scope = CoroutineScope(Dispatchers.IO)

    // Carga de datos actualizada
    fun cargarDatos() {
        scope.launch {
            // 1. Cargar Usuarios desde Room
            val users = usuarioDao.getAll().first()
            usuarios.clear()
            usuarios.addAll(users)

            // 2. Cargar Productos desde Room
            val prods = productoDao.getAll().first()
            _productos.clear()
            if (prods.isEmpty()) {

                productoDao.insertAll(Productos)
                _productos.addAll(Productos)
            } else {
                _productos.addAll(prods)
            }


            val carts = dataStore.getCarts().first()
            _carritos.clear()
            _carritos.putAll(carts)
        }
    }

    // Actualizado para usar Room
    fun registrarUsuario(email: String, password: String): Boolean {
        if (usuarios.any { it.email == email }) return false

        val nuevo = Usuario(email, password)
        usuarios.add(nuevo) // Añadir a la lista en memoria
        scope.launch { usuarioDao.insert(nuevo) } // Guardar en DB
        return true
    }

    // Actualizado para usar Room
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
        // Generamos un nuevo ID manualmente
        val nuevoId = (_productos.maxOfOrNull { it.id } ?: 0) + 1

        val nuevoProducto = Producto(nuevoId, nombre, precio, imagen, descripcion, categoria)

        _productos.add(nuevoProducto) // Añadir a la lista en memoria
        scope.launch {
            productoDao.insert(nuevoProducto) // Guardar en Room
        }
    }


    fun eliminarProducto(producto: Producto) {
        _productos.remove(producto)
        scope.launch {
            productoDao.delete(producto)
        }

        // El resto (quitar de carritos) sigue igual
        _carritos.forEach { (email, cart) ->
            _carritos[email] = cart.filter { it.id != producto.id }
        }
        _carrito.removeAll { it.id == producto.id }

        guardarCarritos()
    }


    fun editarProducto(idProducto: Int, nombre: String, descripcion: String, precio: Int, @DrawableRes imagen: Int, categoria: String) {
        val indice = _productos.indexOfFirst { it.id == idProducto }
        if (indice != -1) {
            val actualizado = Producto(idProducto, nombre, precio, imagen, descripcion, categoria)
            _productos[indice] = actualizado
            scope.launch {
                productoDao.update(actualizado)
            }


            _carritos.forEach { (email, cart) ->
                _carritos[email] = cart.map { if (it.id == idProducto) actualizado else it }
            }
            _carrito.replaceAll { if (it.id == idProducto) actualizado else it }

            guardarCarritos()
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


}