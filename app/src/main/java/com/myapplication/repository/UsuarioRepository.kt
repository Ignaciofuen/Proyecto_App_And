package com.myapplication.repository

import com.myapplication.data.model.LoginRequestDto
import com.myapplication.data.model.ProductoDto
import com.myapplication.data.model.UsuarioDto
import com.myapplication.data.remote.RetrofitInstance

class UsuarioRepository {

    private val api = RetrofitInstance.api

    suspend fun login(email: String, clave: String): UsuarioDto {
        val request = LoginRequestDto(email, clave)
        return api.login(request)
    }

    suspend fun registrarUsuario(usuario: UsuarioDto): UsuarioDto {
        return api.registrarUsuario(usuario)
    }

    suspend fun getAllUsuarios(): List<UsuarioDto> {
        return api.getAllUsuarios()
    }

    suspend fun obtenerCarrito(userId: Long): List<ProductoDto> {
        return api.obtenerCarrito(userId)
    }

    suspend fun agregarAlCarrito(userId: Long, productoId: Long) {
        api.agregarAlCarrito(userId, productoId)
    }

    suspend fun eliminarDelCarrito(userId: Long, productoId: Long) {
        api.eliminarDelCarrito(userId, productoId)
    }

    suspend fun vaciarCarrito(userId: Long) {
        api.vaciarCarrito(userId)
    }
}