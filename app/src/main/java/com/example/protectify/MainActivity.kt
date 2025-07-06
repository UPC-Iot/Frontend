package com.example.protectify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.room.Room
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.protectify.common.Constants
import com.example.protectify.common.Routes
import com.example.protectify.data.local.AppDatabase
import com.example.protectify.data.remote.alert.AlertService
import com.example.protectify.data.remote.authentication.AuthenticationService
import com.example.protectify.data.remote.device.DeviceService
import com.example.protectify.data.remote.house.HouseService
import com.example.protectify.data.remote.notification.NotificationService
import com.example.protectify.data.remote.owner.OwnerService
import com.example.protectify.data.remote.profile.ProfileService
import com.example.protectify.data.remote.visitor.VisitorService
import com.example.protectify.data.repository.alert.AlertRepository
import com.example.protectify.data.repository.authentication.AuthenticationRepository
import com.example.protectify.data.repository.device.DeviceRepository
import com.example.protectify.data.repository.house.HouseRepository
import com.example.protectify.data.repository.notification.NotificationRepository
import com.example.protectify.data.repository.owner.OwnerRepository
import com.example.protectify.data.repository.profile.CloudStorageRepository
import com.example.protectify.data.repository.profile.ProfileRepository
import com.example.protectify.data.repository.visitor.VisitorRepository
import com.example.protectify.presentation.addVisitor.AddVisitorScreen
import com.example.protectify.presentation.addVisitor.AddVisitorViewModel
import com.example.protectify.presentation.addVisitorImage.AddVisitorImageScreen
import com.example.protectify.presentation.addVisitorImage.AddVisitorImageViewModel
import com.example.protectify.presentation.home.HomeScreen
import com.example.protectify.presentation.home.HomeViewModel
import com.example.protectify.presentation.visitorsList.VisitorsListScreen
import com.example.protectify.presentation.visitorsList.VisitorsListViewModel
import com.example.protectify.ui.theme.ProtectifyTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Room (si tienes base de datos local)
        val userDao = Room
            .databaseBuilder(applicationContext, AppDatabase::class.java, "protectify-db")
            .build()
            .getUserDao()

        // Services
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

                // Repositorios
                val alertRepository = AlertRepository(alertService)
                val deviceRepository = DeviceRepository(deviceService)
                val houseRepository = HouseRepository(houseService)
                val notificationRepository = NotificationRepository(notificationService)
                val ownerRepository = OwnerRepository(ownerService)
                val profileRepository = ProfileRepository(profileService)
                val visitorRepository = VisitorRepository(visitorService)
                val authenticationRepository = AuthenticationRepository(authenticationService, userDao)
                val cloudStorageRepository = CloudStorageRepository()

                // ViewModels

                val homeViewModel = HomeViewModel(
                    navController,
                    ownerRepository = ownerRepository,
                    profileRepository = profileRepository,
                    visitorRepository = visitorRepository,
                    notificationRepository = notificationRepository,
                    deviceRepository = deviceRepository
                )

                val visitorsListViewModel = VisitorsListViewModel(
                    navController,
                    ownerRepository = ownerRepository,
                    visitorRepository = visitorRepository
                )

                val addVisitorViewModel = AddVisitorViewModel(
                    navController,
                    ownerRepository = ownerRepository,
                    houseRepository = houseRepository,
                )

                val addVisitorImageViewModel = AddVisitorImageViewModel(
                    navController,
                    visitorRepository = visitorRepository,
                    addVisitorViewModel = addVisitorViewModel,
                    cloudStorageRepository = cloudStorageRepository
                )

                // NavController

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Routes.Home.route,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        composable(route = Routes.AddVisitor.route) {
                            AddVisitorScreen(
                                viewModel = addVisitorViewModel,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        composable(route = Routes.AddVisitorImage.route) {
                            AddVisitorImageScreen(
                                viewModel = addVisitorImageViewModel,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        composable(route = Routes.VisitorsList.route) {
                            VisitorsListScreen(
                                viewModel = visitorsListViewModel,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        composable(route = Routes.Home.route) {
                            HomeScreen(
                                viewModel = homeViewModel,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}