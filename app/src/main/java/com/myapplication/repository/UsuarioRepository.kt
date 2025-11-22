package com.myapplication.repository

import com.myapplication.data.model.LoginRequestDto
import com.myapplication.data.model.ProductoDto
import com.myapplication.data.model.UsuarioDto
import com.myapplication.data.remote.RetrofitInstance

class UsuarioRepository {

    private val api = RetrofitInstance.api

    suspend fun login(email: String, clave: String): UsuarioDto {
        val request = LoginRequestDto(email, clave)
        val response = api.login(request)

        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Error inesperado: respuesta vacía del servidor")
        }

        when (response.code()) {
            400 -> throw Exception("Solicitud inválida")
            401 -> throw Exception("E-mail o contraseña incorrectos")
            404 -> throw Exception("Usuario no encontrado")
            500 -> throw Exception("Error interno del servidor")
        }

        throw Exception("Error desconocido (${response.code()})")
    }


    suspend fun registrarUsuario(usuario: UsuarioDto): UsuarioDto {
        val response = api.registrarUsuario(usuario)

        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Error inesperado: respuesta vacía del servidor")
        }

        when (response.code()) {
            400 -> throw Exception("E-mail ya registrado")
            409 -> throw Exception("El e-mail ingresado ya se encuentra registrado")
            500 -> throw Exception("Error interno del servidor")
        }

        throw Exception("Error desconocido (${response.code()})")
    }

    suspend fun getAllUsuarios(): List<UsuarioDto> {
        return api.getAllUsuarios()
    }

    suspend fun obtenerCarrito(userId: Long): List<ProductoDto> {
        return api.obtenerCarrito(userId)
    }

    suspend fun agregarAlCarrito(userId: Long, productoId: Int) {
        api.agregarAlCarrito(userId, productoId)
    }

    suspend fun eliminarDelCarrito(userId: Long, productoId: Int) {
        api.eliminarDelCarrito(userId, productoId)
    }

    suspend fun vaciarCarrito(userId: Long) {
        api.vaciarCarrito(userId)
    }
}
