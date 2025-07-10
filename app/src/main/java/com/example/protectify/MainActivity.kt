package com.example.protectify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.protectify.common.AppNavigation
import com.example.protectify.common.Constants
import com.example.protectify.common.Routes
import com.example.protectify.data.local.AppDatabase
import com.example.protectify.data.remote.*
import com.example.protectify.data.remote.alert.AlertService
import com.example.protectify.data.remote.authentication.AuthenticationService
import com.example.protectify.data.remote.device.DeviceService
import com.example.protectify.data.remote.house.HouseService
import com.example.protectify.data.remote.notification.NotificationService
import com.example.protectify.data.remote.owner.OwnerService
import com.example.protectify.data.remote.profile.ProfileService
import com.example.protectify.data.remote.visitor.VisitorService
import com.example.protectify.data.repository.*
import com.example.protectify.data.repository.alert.AlertRepository
import com.example.protectify.data.repository.authentication.AuthenticationRepository
import com.example.protectify.data.repository.device.DeviceRepository
import com.example.protectify.data.repository.house.HouseRepository
import com.example.protectify.data.repository.notification.NotificationRepository
import com.example.protectify.data.repository.owner.OwnerRepository
import com.example.protectify.data.repository.profile.CloudStorageRepository
import com.example.protectify.data.repository.profile.ProfileRepository
import com.example.protectify.data.repository.visitor.VisitorRepository
import com.example.protectify.presentation.*
import com.example.protectify.presentation.addVisitor.AddVisitorViewModel
import com.example.protectify.presentation.addVisitorImage.AddVisitorImageViewModel
import com.example.protectify.presentation.auth.login.LoginViewModel
import com.example.protectify.presentation.auth.register.RegisterViewModel
import com.example.protectify.presentation.createHouse.CreateHouseViewModel
import com.example.protectify.presentation.createProfile.CreateProfileViewModel
import com.example.protectify.presentation.home.HomeViewModel
import com.example.protectify.presentation.notificationList.NotificationViewModel
import com.example.protectify.presentation.shared.BottomNavigationBar
import com.example.protectify.presentation.visitorsList.VisitorsListViewModel
import com.example.protectify.ui.theme.ProtectifyTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.example.protectify.presentation.alertList.AlertListViewModel
import com.example.protectify.presentation.camerasList.CamerasListViewModel
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS") // Formato sin zona horaria
            .create()

        // Inicialización de dependencias (igual que antes)
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()




        val userDao = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "protectify-db").build().getUserDao()
        val alertService = retrofit.create(AlertService::class.java)
        val deviceService = retrofit.create(DeviceService::class.java)
        val houseService = retrofit.create(HouseService::class.java)
        val notificationService = retrofit.create(NotificationService::class.java)
        val ownerService = retrofit.create(OwnerService::class.java)
        val profileService = retrofit.create(ProfileService::class.java)
        val visitorService = retrofit.create(VisitorService::class.java)
        val authenticationService = retrofit.create(AuthenticationService::class.java)

        setContent {
            ProtectifyTheme {
                val navController = rememberNavController()
                val alertRepository = AlertRepository(alertService)
                val deviceRepository = DeviceRepository(deviceService)
                val houseRepository = HouseRepository(houseService)
                val notificationRepository = NotificationRepository(notificationService)
                val ownerRepository = OwnerRepository(ownerService)
                val profileRepository = ProfileRepository(profileService)
                val visitorRepository = VisitorRepository(visitorService)
                val authenticationRepository = AuthenticationRepository(authenticationService, userDao)
                val cloudStorageRepository = CloudStorageRepository()

                val homeViewModel = HomeViewModel(navController, ownerRepository, profileRepository, visitorRepository, notificationRepository, deviceRepository,authenticationRepository,alertRepository)
                val visitorsListViewModel = VisitorsListViewModel(navController, ownerRepository, visitorRepository)
                val addVisitorViewModel = AddVisitorViewModel(navController, ownerRepository, houseRepository)
                val addVisitorImageViewModel = AddVisitorImageViewModel(navController, visitorRepository, addVisitorViewModel, cloudStorageRepository)
                val loginViewModel = LoginViewModel(navController, authenticationRepository)
                val registerViewModel = RegisterViewModel(navController, authenticationRepository)
                val createProfileViewModel = CreateProfileViewModel(
                    navController,
                    profileRepository,
                    cloudStorageRepository,
                    registerViewModel
                )
                val createHouseViewModel = CreateHouseViewModel(
                    navController,
                    houseRepository,
                    ownerRepository,
                )
                val notificationViewModel = NotificationViewModel(
                    navController,
                    notificationRepository
                )
                val alertListViewModel = AlertListViewModel(
                    navController,
                    ownerRepository,
                    alertRepository
                )
                val camerasListViewModel = CamerasListViewModel(
                    navController,
                    ownerRepository,
                    deviceRepository
                )

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                Scaffold(
                    containerColor = Color(0xFF26272C),
                    bottomBar = {
                        if (currentRoute == Routes.Home.route ||
                            currentRoute == Routes.VisitorsList.route ||
                            currentRoute == Routes.CamerasList.route) {
                            BottomNavigationBar(navController = navController)
                        }
                    }
                ) { padding ->
                    AppNavigation(
                        navController = navController,
                        padding = padding,
                        homeViewModel = homeViewModel,
                        visitorsListViewModel = visitorsListViewModel,
                        addVisitorViewModel = addVisitorViewModel,
                        addVisitorImageViewModel = addVisitorImageViewModel,
                        loginViewModel = loginViewModel,
                        registerViewModel = registerViewModel,
                        createProfileViewModel = createProfileViewModel,
                        createHouseViewModel = createHouseViewModel,
                        notificationViewModel = notificationViewModel,
                        alertListViewModel = alertListViewModel,
                        camerasListViewModel = camerasListViewModel
                    )
                }
            }
        }
    }
}