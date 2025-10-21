package com.myapplication

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.myapplication.data.AppState
import com.myapplication.data.DataStoreManager
import com.myapplication.navigation.AppNavigation
import com.myapplication.ui.theme.MyAppNavegaValidaTheme
import com.myapplication.ui.views.LoginScreen
import com.myapplication.ui.views.HomeScreen
import androidx.navigation.compose.rememberNavController
import androidx.activity.enableEdgeToEdge
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.app.NotificationManagerCompat


import com.myapplication.data.AppDatabase


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializaciones
        val dataStore = DataStoreManager(applicationContext)
        val db = AppDatabase.getInstance(applicationContext)
        val appState = AppState(dataStore, db)
        appState.cargarDatos()

        enableEdgeToEdge()

        // Crear canal de notificaciones
        createNotificationChannel(applicationContext)

        // Pedir permisos
        solicitarPermisos()

        // Iniciar Compose
        setContent {
            MyAppNavegaValidaTheme {
                MyApp(appState)
            }
        }
    }

    private fun solicitarPermisos() {
        // Permiso de ubicación
        val requestLocationLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                println(if (isGranted) "✅ Permiso de ubicación concedido" else "❌ Permiso de ubicación denegado")
            }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        // Permiso de notificación (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val requestNotifLauncher =
                registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                    println(if (isGranted) "✅ Permiso de notificaciones concedido" else "❌ Permiso de notificaciones denegado")
                }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestNotifLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "canal_promos",
                "Promociones y eventos",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply { description = "Canal para notificaciones de promociones, descuentos o avisos" }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}



@Composable
fun MyApp(appState: AppState) {
    val navController = rememberNavController()
    AppNavigation(navController, appState)


    /*NavHost(navController= navController, startDestination = "login"){
        composable("login") { LoginScreen(navController) }
        composable("registro") { RegistroScreen(navController) }
        composable("notas") { NotasScreen() }
    }*/
}

