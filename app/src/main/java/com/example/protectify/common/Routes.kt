package com.example.protectify.common

sealed class Screen(val route: String) {
    object Register: Screen ("register")
    object Login : Screen("login")
    object SplashScreen: Screen("splash")
    object StartInfo: Screen("start-info")
}


