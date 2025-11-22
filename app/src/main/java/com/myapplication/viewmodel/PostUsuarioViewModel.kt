package com.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapplication.data.model.UsuarioDto
import com.myapplication.data.model.ProductoDto
import com.myapplication.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PostUsuarioViewModel(
    private val repository: UsuarioRepository
) : ViewModel() {

    private val _usuarioActual = MutableStateFlow<UsuarioDto?>(null)
    val usuarioActual: StateFlow<UsuarioDto?> = _usuarioActual.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _carrito = MutableStateFlow<List<ProductoDto>>(emptyList())
    val carrito: StateFlow<List<ProductoDto>> = _carrito.asStateFlow()

    fun login(email: String, clave: String) {
        viewModelScope.launch {
            try {
                val usuarioLogueado = repository.login(email, clave)
                _usuarioActual.value = usuarioLogueado
                _error.value = null

                cargarCarrito()

            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Error al iniciar sesión"
            }
        }
    }

    fun registrarUsuario(usuario: UsuarioDto) {
        viewModelScope.launch {
            try {
                repository.registrarUsuario(usuario)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.localizedMessage
            }
        }
    }

    fun logout() {
        _usuarioActual.value = null
        _carrito.value = emptyList()
    }

    fun cargarCarrito() {
        val usuario = _usuarioActual.value ?: return

        viewModelScope.launch {
            try {
                val productos = repository.obtenerCarrito(usuario.idusu)
                _carrito.value = productos
            } catch (e: Exception) {
                println("Error cargando carrito: ${e.message}")
            }
        }
    }

    fun agregarAlCarrito(producto: ProductoDto) {
        val usuario = _usuarioActual.value ?: return

        viewModelScope.launch {
            try {
                repository.agregarAlCarrito(usuario.idusu, producto.id)
                cargarCarrito()
            } catch (e: Exception) {
                println("Error agregando al carrito: ${e.message}")
            }
        }
    }

    fun eliminarDelCarrito(producto: ProductoDto) {
        val usuario = _usuarioActual.value ?: return

        viewModelScope.launch {
            try {
                repository.eliminarDelCarrito(usuario.idusu, producto.id)
                cargarCarrito()
            } catch (e: Exception) {
                println("Error eliminando del carrito: ${e.message}")
            }
        }
    }

    fun vaciarCarrito() {
        val usuario = _usuarioActual.value ?: return

        viewModelScope.launch {
            try {
                repository.vaciarCarrito(usuario.idusu)
                cargarCarrito()
            } catch (e: Exception) {
                println("Error vaciando carrito: ${e.message}")
            }
        }
    }
}
