package com.example.protectify.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    val profileState by viewModel.profileState
    val notificationCount by viewModel.notificationCount
    val visitorsState by viewModel.state
    val camerasState by viewModel.camerasState
    val alertsState by viewModel.alertState

    // Load data when screen is first composed
    LaunchedEffect(Unit) {
        viewModel.getProfile()
        viewModel.loadNotificationCount()
        viewModel.getVisitors()
        viewModel.getCameras()
        viewModel.getAlerts()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF26272C))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            // Header Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left side - Profile info
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Profile picture or loading placeholder
                    if (profileState.isLoading) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    Color.Gray.copy(alpha = 0.3f),
                                    CircleShape
                                )
                        )
                    } else {
                        GlideImage(
                            imageModel = { profileState.data?.photo },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .clickable { /* Navegar al perfil */ },
                            loading = {
                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .background(Color.Gray.copy(alpha = 0.3f), CircleShape)
                                )
                            },
                            failure = {
                                androidx.compose.foundation.Image(
                                    painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                                    contentDescription = "Imagen no disponible",
                                    modifier = Modifier.matchParentSize(),
                                    contentScale = ContentScale.Crop
                                )
                            },
                        )
                    }



                    Spacer(modifier = Modifier.width(12.dp))
                    // Greeting text
                    Column {
                        Text(
                            text = if (profileState.isLoading) {
                                "Cargando..."
                            } else {
                                "${profileState.data?.firstName ?: "Usuario"} 👋"
                            },
                            color = Color(0xFFBF4D36),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Bienvenido de nuevo",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }

                // Right side - Action buttons
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Notification button with counter
                    Box {
                        IconButton(onClick = { viewModel.goToNotificationListScreen() }) {
                            Icon(
                                Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = Color.White
                            )
                        }

                        // Custom notification counter
                        if (notificationCount > 0) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .offset(x = 4.dp, y = 4.dp)
                                    .size(18.dp)
                                    .background(
                                        Color.Red,
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (notificationCount > 99) "99+" else notificationCount.toString(),
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    // Botón de cerrar sesión
                    IconButton(onClick = {viewModel.signOut()}) {
                        Icon(
                            Icons.Default.ExitToApp, // Icono de logout
                            contentDescription = "Cerrar sesión",
                            tint = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(24.dp))

            // Alerts Section
            AlertsSection(
                alerts = alertsState.data ?: emptyList(),
                isLoading = alertsState.isLoading,
                onAlertClick = { alertId ->
                    // TODO: Navigate to alert detail
                },
                onMarkAsResolvedClick = { alertId ->
                    // TODO: Mark alert as resolved
                },
                onViewAllClick = {
                    // TODO: Navigate to all alerts screen
                }
            )

            // TODO: Agregar más secciones aquí
            Spacer(modifier = Modifier.height(24.dp))

            VisitorsSection(
                visitors = visitorsState.data ?: emptyList(),
                isLoading = visitorsState.isLoading,
                onAddVisitorClick = { viewModel.goToAddVisitorScreen() },
                onVisitorClick = { visitorId -> /* Acción al hacer click en visitante */ }
            )

            Spacer(modifier = Modifier.height(24.dp))

            CamerasSection(
                devices = camerasState.data ?: emptyList(),
                isLoading = camerasState.isLoading,
                onFilterClick = { /* Acción para el filtro */ },
                onDeviceClick = { cameraId -> /* Acción al hacer click en cámara */ },
                onDeviceMenuClick = { cameraId -> /* Acción al hacer click en menú de cámara */ }
            )

        }
    }
}
