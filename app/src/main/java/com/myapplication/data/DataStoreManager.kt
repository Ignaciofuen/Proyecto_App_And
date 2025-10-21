package com.myapplication.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore by preferencesDataStore(name = "app_prefs")

class DataStoreManager(private val context: Context){
    private val gson = Gson()

    private val CARTS_KEY = stringPreferencesKey("carritos")

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