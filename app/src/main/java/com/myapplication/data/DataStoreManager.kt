package com.myapplication.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// DataStore "singleton"
val Context.dataStore by preferencesDataStore(name = "app_prefs")

class DataStoreManager(private val context: Context){
    private val gson = Gson()

    private val USERS_KEY = stringPreferencesKey("usuarios")
    private val PRODUCTS_KEY = stringPreferencesKey("productos")
    private val CARTS_KEY = stringPreferencesKey("carritos")


    suspend fun saveUsers(users: List<Usuario>){
        val json = gson.toJson(users)
        context.dataStore.edit { prefs ->
            prefs[USERS_KEY] = json
        }
    }

    fun getUsers(): Flow<List<Usuario>>{
        return context.dataStore.data.map { prefs ->
            val json = prefs[USERS_KEY] ?: "[]"
            val type = object : TypeToken<List<Usuario>>() {}.type
            gson.fromJson(json,type)
        }
    }


    suspend fun saveProducts(products: List<Producto>) {
        val json = gson.toJson(products)
        context.dataStore.edit { prefs ->
            prefs[PRODUCTS_KEY] = json
        }
    }

    fun getProducts(): Flow<List<Producto>?> { // Devuelve nullable
        return context.dataStore.data.map { prefs ->
            val json = prefs[PRODUCTS_KEY]
            if (json == null) {
                null // AppState se encargará de poner la lista default
            } else {
                val type = object : TypeToken<List<Producto>>() {}.type
                gson.fromJson(json, type)
            }
        }
    }

    suspend fun saveCarts(carts: Map<String, List<Producto>>) {
        val json = gson.toJson(carts)
        context.dataStore.edit { prefs ->
            prefs[CARTS_KEY] = json
        }
    }

    fun getCarts(): Flow<Map<String, List<Producto>>> {
        return context.dataStore.data.map { prefs ->
            val json = prefs[CARTS_KEY] ?: "{}"
            val type = object : TypeToken<Map<String, List<Producto>>>() {}.type
            gson.fromJson(json, type)
        }
    }
}