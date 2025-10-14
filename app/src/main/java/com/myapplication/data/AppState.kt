package com.myapplication.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class Usuario(val email: String, val password: String)

class AppState(private val dataStore: DataStoreManager) {
    val usuarios = mutableStateListOf<Usuario>()
    var usuarioActual: Usuario? = null

    private val scope = CoroutineScope(Dispatchers.IO)

    // Carga de usuarios desde DataStore
    fun cargarDatos() {
        scope.launch {
            val users = dataStore.getUsers().first()
            usuarios.clear()
            usuarios.addAll(users)
        }
    }

    // Registro de usuario
    fun registrarUsuario(email: String, password: String): Boolean {
        if (usuarios.any { it.email == email }) return false
        val nuevo = Usuario(email, password)
        usuarios.add(nuevo)
        guardarUsuarios()
        return true
    }

    // Login
    fun login(email: String, password: String): Boolean {
        val user = usuarios.find { it.email == email && it.password == password }
        return if (user != null) {
            usuarioActual = user
            true
        } else false
    }

    // Logout
    fun logout() {
        usuarioActual = null
    }

    // Guardar usuarios en DataStore
    private fun guardarUsuarios() {
        scope.launch {
            dataStore.saveUsers(usuarios)
        }
    }
}
