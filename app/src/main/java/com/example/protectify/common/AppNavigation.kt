package com.example.protectify.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.protectify.presentation.auth.login.LoginScreen
import com.example.protectify.presentation.auth.register.RegisterScreen
import com.example.protectify.presentation.shared.SplashScreen
import com.example.protectify.presentation.shared.StartInfoScreen

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController(), padding: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = Routes.SplashScreen.route
    ) {
        composable(route = Routes.SplashScreen.route) {
            SplashScreen(padding){navController.navigate(Routes.StartInfo.route){
                popUpTo(Routes.SplashScreen.route) { inclusive = true }
            } }
        }
        composable(route = Routes.StartInfo.route) {
            StartInfoScreen(padding,navController)
        }
        composable(route = Routes.Login.route) {
            LoginScreen(padding,navController)
        }
        composable(route = Routes.Register.route) {
            RegisterScreen(padding,navController)
        }

    }
}