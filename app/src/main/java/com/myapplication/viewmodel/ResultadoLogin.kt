package com.example.form.viewmodel

sealed class ResultadoLogin {
    object ExitoAdmin : ResultadoLogin()
    object ExitoUsuario : ResultadoLogin()
    data class Error(val mensaje: String) : ResultadoLogin()
}
