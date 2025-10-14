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

}