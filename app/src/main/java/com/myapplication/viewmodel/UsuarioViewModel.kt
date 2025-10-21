package com.example.form.viewmodel

import androidx.lifecycle.ViewModel
import com.example.form.model.UsuarioErrores
import com.example.form.model.UsuarioUIState
import com.myapplication.data.AppState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update


class UsuarioViewModel : ViewModel() {

    private val _estado = MutableStateFlow(UsuarioUIState())

    val estado: StateFlow<UsuarioUIState> = _estado

    fun onCorreoChange (valor : String) {
        _estado.update { it.copy(correo = valor, errores = it.errores.copy(correo = null)) }
    }
    fun onClaveChange (valor : String) {
        _estado.update { it.copy(clave = valor, errores = it.errores.copy(clave = null)) }
    }

    fun onRepetirClaveChange(valor: String) {
        _estado.update { it.copy(repetirClave = valor, errores = it.errores.copy(repetirClave = null)) }
    }

    //Validamos Registro
    fun validarRegistro(): Boolean {
        val estadoActual = _estado.value
        val dominiosPermitidos = listOf(
            "@admin.cl",
            "@gmail.cl",
            "@gmail.com",
            "@hotmail.cl",
            "@hotmail.com",
            "@outlook.com"
        )
        val correoValido = dominiosPermitidos.any { estadoActual.correo.endsWith(it) }

        val errores = UsuarioErrores(
            correo = if (!correoValido) "Dominio del e-mail incorrecto" else null,
            clave = if (estadoActual.clave.length < 6) "La contraseña debe contener mínimo 6 caracteres" else null,
            repetirClave = if (estadoActual.repetirClave != estadoActual.clave) "Las contraseñas no coinciden" else null
        )

        val hayErrores = listOfNotNull(
            errores.correo,
            errores.clave,
            errores.repetirClave
        ).isNotEmpty()

        _estado.update { it.copy(errores = errores) }

        return !hayErrores
    }

    //Validamos Login
    fun validarLogin(appState: AppState): ResultadoLogin {
        val estadoActual = _estado.value
        val dominiosPermitidos = listOf(
            "@admin.cl",
            "@gmail.cl",
            "@gmail.com",
            "@hotmail.cl",
            "@hotmail.com",
            "@outlook.com"
        )

        val correoValido = dominiosPermitidos.any { estadoActual.correo.endsWith(it) }

        if (estadoActual.correo.isBlank() && estadoActual.clave.isBlank()) {
            return ResultadoLogin.Error("Correo y contraseña no pueden estar vacíos")
        } else if (estadoActual.correo.isBlank()) {
            return ResultadoLogin.Error("Correo no puede estar vacío")
        } else if (estadoActual.clave.isBlank()) {
            return ResultadoLogin.Error("Contraseña no puede estar vacía")
        }

        if (!correoValido) {
            return ResultadoLogin.Error("Dominio del e-mail incorrecto")
        }
        val usuario = appState.usuarios.find { it.email == estadoActual.correo.trim().lowercase() }
            ?: return ResultadoLogin.Error("El e-mail ingresado no ha sido registrado")

        if (usuario.password != estadoActual.clave) {
            return ResultadoLogin.Error("Contraseña incorrecta")
        }

        appState.usuarioActual = usuario


        return if (usuario.email.endsWith("@admin.cl")) {
            ResultadoLogin.ExitoAdmin
        } else {
            ResultadoLogin.ExitoUsuario
        }
    }
}

private fun AppState.obtenerUsuarioPorEmail(lowercase: String) {}
