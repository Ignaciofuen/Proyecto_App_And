package com.myapplication.data.model

data class ProductoDto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val categoria: String,
    val imagen: String,
    val precio: Int
)