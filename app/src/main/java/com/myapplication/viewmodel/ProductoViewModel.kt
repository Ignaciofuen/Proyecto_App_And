package com.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapplication.data.model.ProductoDto
import com.myapplication.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductoViewModel : ViewModel() {

    private val repository = ProductoRepository()

    private val _productos = MutableStateFlow<List<ProductoDto>>(emptyList())
    val productos: StateFlow<List<ProductoDto>> = _productos

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        listarProductos()
    }

    fun listarProductos() {
        viewModelScope.launch {
            try {
                _error.value = null
                _productos.value = repository.getAllProductos()
            } catch (e: Exception) {
                _error.value = "Error al cargar productos: ${e.localizedMessage}"
                e.printStackTrace()
            }
        }
    }

    fun agregarProducto(producto: ProductoDto) {
        viewModelScope.launch {
            try {
                _error.value = null
                repository.saveProducto(producto)
                listarProductos()
            } catch (e: Exception) {
                _error.value = "Error al agregar producto: ${e.localizedMessage}"
                e.printStackTrace()
            }
        }
    }

    fun editarProducto(id: Long, producto: ProductoDto) {
        viewModelScope.launch {
            try {
                _error.value = null
                repository.updateProducto(id, producto)
                listarProductos()
            } catch (e: Exception) {
                _error.value = "Error al editar producto: ${e.localizedMessage}"
                e.printStackTrace()
            }
        }
    }


    fun eliminarProducto(producto: ProductoDto) {
        viewModelScope.launch {
            try {
                _error.value = null


                repository.deleteProducto(producto.id)


                listarProductos()

            } catch (e: Exception) {
                _error.value = "Error al eliminar: ${e.localizedMessage}"
                e.printStackTrace()
            }
        }
    }
}