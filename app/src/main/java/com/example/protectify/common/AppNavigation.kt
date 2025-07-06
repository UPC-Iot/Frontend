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
        startDestination = Screen.SplashScreen.route
    ) {
        composable(route = Screen.SplashScreen.route) {
            SplashScreen(padding){navController.navigate(Screen.StartInfo.route){
                popUpTo(Screen.SplashScreen.route) { inclusive = true }
            } }
        }
        composable(route = Screen.StartInfo.route) {
            StartInfoScreen(padding,navController)
        }
        composable(route = Screen.Login.route) {
            LoginScreen(padding,navController)
        }
        composable(route = Screen.Register.route) {
            RegisterScreen(padding,navController)
        }

    }
}