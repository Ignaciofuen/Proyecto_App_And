package com.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
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
        composable ("registro") {RegistroScreen(navController,appState)}
        composable ("home"){ HomeScreen(navController,appState) }
        composable ("productos"){ ProductosScreen(navController,appState) }

    }
}