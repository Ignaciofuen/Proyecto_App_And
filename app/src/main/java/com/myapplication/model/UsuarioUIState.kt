package com.example.form.model

data class UsuarioUIState(
    val correo : String = "",
    val clave : String = "",
    val password : String = "",
    val repetirClave : String = "",
    val errores : UsuarioErrores = UsuarioErrores()
)