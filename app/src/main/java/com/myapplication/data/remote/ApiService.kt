package com.myapplication.data.remote


import com.myapplication.data.model.LoginRequestDto
import com.myapplication.data.model.ProductoDto
import com.myapplication.data.model.UsuarioDto
import retrofit2.http.*
import retrofit2.Response
interface ApiService {

    @GET("api/productos")
    suspend fun getAllProductos(): List<ProductoDto>

    @GET("api/productos/{id}")
    suspend fun getProductoById(@Path("id") id: Long): ProductoDto

    @POST("api/productos")
    suspend fun saveProducto(@Body producto: ProductoDto): ProductoDto

    @PUT("api/productos/{id}")
    suspend fun updateProducto(@Path("id") id: Long, @Body producto: ProductoDto): ProductoDto

    @DELETE("api/productos/{id}")
    suspend fun deleteProducto(@Path("id") id: Long): Response<Unit>
    @GET("api/auth/usuarios")
    suspend fun getAllUsuarios(): List<UsuarioDto>

    @POST("api/auth/login")
    suspend fun login(@Body loginRequest: LoginRequestDto): Response<UsuarioDto>

    @POST("api/auth/registro")
    suspend fun registrarUsuario(@Body usuario: UsuarioDto): Response<UsuarioDto>

    @GET("api/carrito/{userId}")
    suspend fun obtenerCarrito(@Path("userId") userId: Long): List<ProductoDto>

    @POST("api/carrito/{userId}/agregar/{productoId}")
    suspend fun agregarAlCarrito(@Path("userId") userId: Long, @Path("productoId") productoId: Long)

    @DELETE("api/carrito/{userId}/quitar/{productoId}")
    suspend fun eliminarDelCarrito(@Path("userId") userId: Long, @Path("productoId") productoId: Long)

    @DELETE("api/carrito/{userId}/vaciar")
    suspend fun vaciarCarrito(@Path("userId") userId: Long)
}