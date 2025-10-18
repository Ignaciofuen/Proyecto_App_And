package com.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.form.view.RegistroScreen
import com.example.form.viewmodel.UsuarioViewModel
import com.myapplication.data.AppState
import com.myapplication.ui.views.AdminScreen
import com.myapplication.ui.views.CarritoScreen
import com.myapplication.ui.views.HomeScreen
import com.myapplication.ui.views.LoginScreen
import com.myapplication.ui.views.ProductosAdminScreen
import com.myapplication.ui.views.ProductosScreen

@Composable
fun AppNavigation(navController: NavHostController, appState: AppState){
    NavHost(
        navController = navController, startDestination = "login",
    ){
        composable ("login") { val usuarioViewModel: UsuarioViewModel = viewModel()
            LoginScreen(navController, usuarioViewModel, appState)
        }
        composable ("registro") { val usuarioViewModel: UsuarioViewModel = viewModel()
            RegistroScreen(navController, usuarioViewModel, appState)
        }
        composable ("home"){ HomeScreen(navController,appState) }
        composable ("productos"){ ProductosScreen(navController,appState) }
        composable ("admin"){ AdminScreen(navController,appState) }
        composable ("carrito"){ CarritoScreen(navController,appState) }
        composable ("productosAdmin"){ ProductosAdminScreen(navController,appState) }

    }
}
