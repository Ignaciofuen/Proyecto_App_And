package com.myapplication.repository

import com.myapplication.data.model.ProductoDto
import com.myapplication.data.remote.RetrofitInstance

class ProductoRepository {

    private val api = RetrofitInstance.api

    suspend fun getAllProductos(): List<ProductoDto> {
        return api.getAllProductos()
    }

    suspend fun saveProducto(producto: ProductoDto): ProductoDto {
        return api.saveProducto(producto)
    }

    suspend fun updateProducto(id: Int, producto: ProductoDto): ProductoDto {
        return api.updateProducto(id, producto)
    }

    // --- AQUÍ ESTÁ EL CAMBIO ---
    suspend fun deleteProducto(id: Int) {

        val response = api.deleteProducto(id)

        if (!response.isSuccessful) {
            throw Exception("Error al eliminar: ${response.code()}")
        }
    }
}