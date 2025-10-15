package com.myapplication.navigation

import androidx.compose.runtime.Composable
<<<<<<< HEAD
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
=======
import androidx.datastore.dataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.form.view.FormularioScreen
import com.example.form.viewmodel.UsuarioViewModel
>>>>>>> e9c2c18 (Cambiamos estructura del proyecto  MVVM para mayor organicación y mejores prácticas. Comenzamos a desarrollar el login y registro de admin. Se comienza a desarrollar la vista de admin)
import com.myapplication.data.AppState
import com.myapplication.ui.views.HomeScreen
import com.myapplication.ui.views.LoginScreen
import com.myapplication.ui.views.ProductosScreen
import com.myapplication.ui.views.RegistroScreen

@Composable
fun AppNavigation(navController: NavHostController, appState: AppState){
    NavHost(
        navController = navController, startDestination = "login",
    ){
        composable ("login"){ LoginScreen(navController,appState) }
<<<<<<< HEAD
        composable ("registro") {RegistroScreen(navController,appState)}
=======

        composable ("registro") { val usuarioViewModel: UsuarioViewModel = viewModel()
            FormularioScreen(navController, usuarioViewModel, appState) }

>>>>>>> e9c2c18 (Cambiamos estructura del proyecto  MVVM para mayor organicación y mejores prácticas. Comenzamos a desarrollar el login y registro de admin. Se comienza a desarrollar la vista de admin)
        composable ("home"){ HomeScreen(navController,appState) }
        composable ("productos"){ ProductosScreen(navController,appState) }

    }
}