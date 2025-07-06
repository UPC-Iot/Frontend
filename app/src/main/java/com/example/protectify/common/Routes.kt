package com.example.protectify.common

sealed class Routes(val route: String) {
    data object Register: Routes ("register")
    data object Login : Routes("login")
    data object SplashScreen: Routes("splash")
    data object StartInfo: Routes("start-info")
    data object AddVisitor: Routes("add-visitor")
    data object VisitorsList: Routes("visitor-list")
    data object AddVisitorImage: Routes("add-visitor-image")
    data object Home: Routes("home")

}


