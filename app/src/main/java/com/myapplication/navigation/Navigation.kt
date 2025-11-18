package com.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.form.view.RegistroScreen
import com.myapplication.ui.views.AdminScreen
import com.myapplication.ui.views.CarritoScreen
import com.myapplication.ui.views.HomeScreen
import com.myapplication.ui.views.LoginScreen
import com.myapplication.ui.views.ProductosAdminScreen
import com.myapplication.ui.views.ProductosScreen
import com.myapplication.ui.views.PostScreen
import com.myapplication.viewmodel.PostUsuarioViewModel
import com.myapplication.viewmodel.ProductoViewModel

@Composable
fun AppNavigation(navController: NavHostController){


    val authViewModel: PostUsuarioViewModel = viewModel()
    val productoViewModel: ProductoViewModel = viewModel()

    NavHost(
        navController = navController, startDestination = "login",
    ){
        composable ("login") {

            LoginScreen(navController, authViewModel = authViewModel)
        }
        composable ("registro") {
            RegistroScreen(navController, authViewModel = authViewModel)
        }
        composable ("home"){
            HomeScreen(navController, viewModel = authViewModel)
        }
        composable ("productos"){

            ProductosScreen(navController, productoViewModel, authViewModel)
        }
        composable ("admin"){
            AdminScreen(navController, viewModel = authViewModel)
        }
        composable ("carrito"){
            CarritoScreen(navController, viewModel = authViewModel)
        }
        composable ("productosAdmin"){
            ProductosAdminScreen(navController, viewModel = productoViewModel)
        }
        composable ("posts") {
            PostScreen(navController)
        }
    }
}