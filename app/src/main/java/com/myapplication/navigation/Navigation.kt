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
fun AppNavigation(
    navController: NavHostController,
    viewModel: PostUsuarioViewModel
) {
    val productoViewModel: ProductoViewModel =
        androidx.lifecycle.viewmodel.compose.viewModel()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        composable("login") {
            LoginScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable("registro") {
            RegistroScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable("home") {
            HomeScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable("productos") {
            ProductosScreen(
                navController = navController,
                productoViewModel = productoViewModel,
                usuarioViewModel = viewModel
            )
        }

        composable("admin") {
            AdminScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable("carrito") {
            CarritoScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable("productosAdmin") {
            ProductosAdminScreen(
                navController = navController,
                viewModel = productoViewModel
            )
        }

        composable("posts") {
            PostScreen(navController = navController)
        }
    }
}
