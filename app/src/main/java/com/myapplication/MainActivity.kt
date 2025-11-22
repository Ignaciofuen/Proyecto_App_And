package com.myapplication

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.myapplication.navigation.AppNavigation
import com.myapplication.repository.UsuarioRepository
import com.myapplication.viewmodel.PostUsuarioViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        createNotificationChannel(applicationContext)
        solicitarPermisos()

        setContent {
            MyApp()
        }
    }

    private fun solicitarPermisos() {

        val requestLocationLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                println(if (isGranted) "✅ Permiso de ubicación concedido" else "❌ Permiso de ubicación denegado")
            }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

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
fun MyApp() {

    val viewModel = remember {
        PostUsuarioViewModel(
            repository = UsuarioRepository()
        )
    }

    val navController = rememberNavController()

    AppNavigation(
        navController = navController,
        viewModel = viewModel
    )
}