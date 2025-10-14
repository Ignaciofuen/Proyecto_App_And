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
import com.myapplication.ui.views.RegistroScreen
import com.myapplication.ui.views.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dataStore = DataStoreManager(applicationContext)
        val appState = AppState(dataStore)

        enableEdgeToEdge()
        setContent {
            MyApp(appState)
        }
    }
}

@Composable
fun MyApp(appState: AppState) {
    val navController = rememberNavController()
    MaterialTheme{
        AppNavigation(navController, appState)
    }

    /*NavHost(navController= navController, startDestination = "login"){
        composable("login") { LoginScreen(navController) }
        composable("registro") { RegistroScreen(navController) }
        composable("notas") { NotasScreen() }
    }*/
}

