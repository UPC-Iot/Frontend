package com.example.protectify.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.protectify.presentation.addVisitor.AddVisitorScreen
import com.example.protectify.presentation.addVisitor.AddVisitorViewModel
import com.example.protectify.presentation.addVisitorImage.AddVisitorImageScreen
import com.example.protectify.presentation.addVisitorImage.AddVisitorImageViewModel
import com.example.protectify.presentation.alertList.AlertListScreen
import com.example.protectify.presentation.alertList.AlertListViewModel
import com.example.protectify.presentation.auth.login.LoginScreen
import com.example.protectify.presentation.auth.login.LoginViewModel
import com.example.protectify.presentation.auth.register.RegisterScreen
import com.example.protectify.presentation.auth.register.RegisterViewModel
import com.example.protectify.presentation.camerasList.CamerasListScreen
import com.example.protectify.presentation.camerasList.CamerasListViewModel
import com.example.protectify.presentation.createHouse.CreateHouseScreen
import com.example.protectify.presentation.createHouse.CreateHouseViewModel
import com.example.protectify.presentation.createProfile.CreateProfileScreen
import com.example.protectify.presentation.createProfile.CreateProfileViewModel
import com.example.protectify.presentation.home.HomeScreen
import com.example.protectify.presentation.home.HomeViewModel
import com.example.protectify.presentation.notificationList.NotificationScreen
import com.example.protectify.presentation.notificationList.NotificationViewModel
import com.example.protectify.presentation.shared.SplashScreen
import com.example.protectify.presentation.shared.StartInfoScreen
import com.example.protectify.presentation.visitorsList.VisitorsListScreen
import com.example.protectify.presentation.visitorsList.VisitorsListViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    padding: PaddingValues,
    homeViewModel: HomeViewModel,
    visitorsListViewModel: VisitorsListViewModel,
    addVisitorViewModel: AddVisitorViewModel,
    addVisitorImageViewModel: AddVisitorImageViewModel,
    loginViewModel: LoginViewModel,
    registerViewModel: RegisterViewModel,
    createProfileViewModel: CreateProfileViewModel,
    createHouseViewModel: CreateHouseViewModel,
    notificationViewModel: NotificationViewModel,
    alertListViewModel: AlertListViewModel,
    camerasListViewModel: CamerasListViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Login.route,
        modifier = Modifier.padding(padding)
    ) {
        composable(route = Routes.AddVisitor.route) {
            AddVisitorScreen(viewModel = addVisitorViewModel)
        }
        composable(route = Routes.AddVisitorImage.route) {
            AddVisitorImageScreen(viewModel = addVisitorImageViewModel)
        }
        composable(route = Routes.VisitorsList.route) {
            VisitorsListScreen(viewModel = visitorsListViewModel)
        }
        composable(route = Routes.Home.route) {
            HomeScreen(viewModel = homeViewModel)
        }
        composable(route = Routes.Login.route) {
            LoginScreen(viewModel = loginViewModel)
        }
        composable(route = Routes.Register.route) {
            RegisterScreen(viewModel = registerViewModel)
        }
        composable(route = Routes.CreateHouse.route) {
            CreateHouseScreen(viewModel = createHouseViewModel)
        }
        composable(route = Routes.CreateProfile.route) {
            CreateProfileScreen(viewModel = createProfileViewModel)
        }
        composable(route = Routes.NotificationList.route) {
            NotificationScreen(viewModel = notificationViewModel)
        }
        composable(route = Routes.AlertList.route) {
            AlertListScreen(viewModel = alertListViewModel)
        }
        composable(route = Routes.CamerasList.route) {
            CamerasListScreen(viewModel = camerasListViewModel)
        }
        composable(route = Routes.StartInfo.route) {
            StartInfoScreen(padding,navController)
        }
        composable(route = Routes.SplashScreen.route) {
            SplashScreen(padding){navController.navigate(Routes.StartInfo.route){
                popUpTo(Routes.SplashScreen.route) { inclusive = true }
            } }
        }



    }
}