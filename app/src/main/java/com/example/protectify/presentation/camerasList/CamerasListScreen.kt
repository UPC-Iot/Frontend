package com.example.protectify.presentation.camerasList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.protectify.domain.device.Device

@Composable
fun CamerasListScreen(
    viewModel: CamerasListViewModel,
    modifier: Modifier = Modifier
) {
    val camerasState by viewModel.camerasState
    var selectedStatusFilter by remember { mutableStateOf("TODOS") }
    var selectedActiveFilter by remember { mutableStateOf("TODOS") }

    // Load data when screen is first composed
    LaunchedEffect(Unit) {
        viewModel.getCameras()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF26272C))
    ) {
        // Header
        HeaderSection(
            onBackClick = {
                viewModel.goToBack()
            },
            onAddClick = {}
        )

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Filters Section
            FiltersSection(
                selectedStatusFilter = selectedStatusFilter,
                selectedActiveFilter = selectedActiveFilter,
                onStatusFilterChange = { selectedStatusFilter = it },
                onActiveFilterChange = { selectedActiveFilter = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Cameras List
            when {
                camerasState.isLoading -> {
                    LoadingCamerasList()
                }
                else -> {
                    val filteredCameras = filterCameras(
                        cameras = camerasState.data ?: emptyList(),
                        statusFilter = selectedStatusFilter,
                        activeFilter = selectedActiveFilter
                    )

                    if (filteredCameras.isEmpty()) {
                        EmptyCamerasState(
                            hasFilters = selectedStatusFilter != "TODOS" || selectedActiveFilter != "TODOS"
                        )
                    } else {
                        CamerasList(
                            cameras = filteredCameras,
                            onCameraClick = {  },
                            onDeleteClick = { cameraId ->
                                viewModel.deleteCamera(cameraId.toString())
                            },
                            onToggleActiveClick = { cameraId ->
                                // TODO: Implement toggle active functionality
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HeaderSection(
    onBackClick: () -> Unit,
    onAddClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .statusBarsPadding(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back button
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(40.dp)
                .background(
                    Color.White.copy(alpha = 0.1f),
                    CircleShape
                )
        ) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }

        // Title centered
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Mis Cámaras",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }

        // Add camera button
        IconButton(
            onClick = onAddClick,
            modifier = Modifier
                .size(40.dp)
                .background(
                    Color(0xFFE17055).copy(alpha = 0.2f),
                    CircleShape
                )
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Add camera",
                tint = Color(0xFFE17055),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun FiltersSection(
    selectedStatusFilter: String,
    selectedActiveFilter: String,
    onStatusFilterChange: (String) -> Unit,
    onActiveFilterChange: (String) -> Unit
) {
    Column {
        // Status Filter
        Text(
            text = "Filtrar por estado:",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.White.copy(alpha = 0.7f),
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            val statusFilters = listOf(
                "TODOS" to "Todos",
                "ONLINE" to "En línea",
                "OFFLINE" to "Desconectado",
                "MAINTENANCE" to "Mantenimiento",
                "ERROR" to "Error"
            )

            items(statusFilters.size) { index ->
                val (value, label) = statusFilters[index]
                FilterChip(
                    selected = selectedStatusFilter == value,
                    onClick = { onStatusFilterChange(value) },
                    label = {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFE17055),
                        selectedLabelColor = Color.White,
                        containerColor = Color(0xFF3A3A3A),
                        labelColor = Color.Gray
                    )
                )
            }
        }

        // Active Filter
        Text(
            text = "Filtrar por actividad:",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.White.copy(alpha = 0.7f),
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val activeFilters = listOf(
                "TODOS" to "Todos",
                "ACTIVE" to "Activos",
                "INACTIVE" to "Inactivos"
            )

            items(activeFilters.size) { index ->
                val (value, label) = activeFilters[index]
                FilterChip(
                    selected = selectedActiveFilter == value,
                    onClick = { onActiveFilterChange(value) },
                    label = {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFE17055),
                        selectedLabelColor = Color.White,
                        containerColor = Color(0xFF3A3A3A),
                        labelColor = Color.Gray
                    )
                )
            }
        }
    }
}

@Composable
private fun LoadingCamerasList() {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(4) {
            CameraCardLoading()
        }
    }
}

@Composable
private fun CameraCardLoading() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2A2A2A)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon placeholder
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            Color.Gray.copy(alpha = 0.3f),
                            RoundedCornerShape(12.dp)
                        )
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Content placeholders
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .width(120.dp)
                            .height(18.dp)
                            .background(
                                Color.Gray.copy(alpha = 0.3f),
                                RoundedCornerShape(9.dp)
                            )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(14.dp)
                            .background(
                                Color.Gray.copy(alpha = 0.2f),
                                RoundedCornerShape(7.dp)
                            )
                    )
                }

                // Status placeholder
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(24.dp)
                        .background(
                            Color.Gray.copy(alpha = 0.3f),
                            RoundedCornerShape(12.dp)
                        )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Technical info placeholders
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                repeat(2) {
                    Column {
                        Box(
                            modifier = Modifier
                                .width(60.dp)
                                .height(12.dp)
                                .background(
                                    Color.Gray.copy(alpha = 0.2f),
                                    RoundedCornerShape(6.dp)
                                )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .width(80.dp)
                                .height(14.dp)
                                .background(
                                    Color.Gray.copy(alpha = 0.1f),
                                    RoundedCornerShape(7.dp)
                                )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyCamerasState(
    hasFilters: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                if (hasFilters) Icons.Default.List else Icons.Default.Warning,
                contentDescription = if (hasFilters) "No results" else "No cameras",
                tint = Color.Gray,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = if (hasFilters) "No se encontraron cámaras" else "Sin Cámaras Configuradas",
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (hasFilters)
                    "Intenta cambiar los filtros para ver más resultados"
                else
                    "Agrega tu primera cámara para comenzar a monitorear",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Gray.copy(alpha = 0.7f)
                ),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
private fun CamerasList(
    cameras: List<Device>,
    onCameraClick: (Long) -> Unit,
    onDeleteClick: (Long) -> Unit,
    onToggleActiveClick: (Long) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(cameras) { camera ->
            CameraListCard(
                camera = camera,
                onClick = onCameraClick,
                onDeleteClick = onDeleteClick,
                onToggleActiveClick = onToggleActiveClick
            )
        }
    }
}

@Composable
private fun CameraListCard(
    camera: Device,
    onClick: (Long) -> Unit,
    onDeleteClick: (Long) -> Unit,
    onToggleActiveClick: (Long) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                camera.id?.let { onClick(it) }
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2A2A2A)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Camera Icon
                CameraIconSection(camera = camera)

                Spacer(modifier = Modifier.width(16.dp))

                // Camera Info
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = camera.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = getCameraLocationFromName(camera.name),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.Gray
                        )
                    )
                }

                // Status Chip
                CameraStatusChip(
                    status = camera.status,
                    isActive = camera.active
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Technical Information
            TechnicalInfoSection(camera = camera)

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            ActionButtonsSection(
                camera = camera,
                onDeleteClick = onDeleteClick,
                onToggleActiveClick = onToggleActiveClick
            )
        }
    }
}

private val Orange = Color(0xFFE17055)

@Composable
private fun CameraIconSection(
    camera: Device
) {
    val (icon, backgroundColor) = when {
        !camera.active -> Icons.Filled.Face to Color.Gray.copy(alpha = 0.2f)
        camera.status.uppercase() == "ONLINE" -> Icons.Filled.Face to Color.Green.copy(alpha = 0.2f)
        camera.status.uppercase() == "OFFLINE" -> Icons.Filled.Face to Color.Red.copy(alpha = 0.2f)
        camera.status.uppercase() == "ERROR" -> Icons.Filled.Face to Color.Red.copy(alpha = 0.2f)
        camera.status.uppercase() == "MAINTENANCE" -> Icons.Filled.Build to Orange.copy(alpha = 0.2f)
        else -> Icons.Filled.Face to Color.Gray.copy(alpha = 0.2f)
    }

    val iconColor = when {
        !camera.active -> Color.Gray
        camera.status.uppercase() == "ONLINE" -> Color.Green
        camera.status.uppercase() == "OFFLINE" -> Color.Red
        camera.status.uppercase() == "ERROR" -> Color.Red
        camera.status.uppercase() == "MAINTENANCE" -> Orange
        else -> Color.Gray
    }

    Box(
        modifier = Modifier
            .size(48.dp)
            .background(backgroundColor, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = camera.name,
            tint = iconColor,
            modifier = Modifier.size(28.dp)
        )
    }
}



@Composable
private fun CameraStatusChip(
    status: String,
    isActive: Boolean
) {
    val (backgroundColor, textColor, text) = when {
        !isActive -> Triple(Color.Gray.copy(alpha = 0.2f), Color.Gray, "Inactiva")
        status.uppercase() == "ONLINE" -> Triple(Color.Green.copy(alpha = 0.2f), Color.Green, "En línea")
        status.uppercase() == "OFFLINE" -> Triple(Color.Red.copy(alpha = 0.2f), Color.Red, "Desconectada")
        status.uppercase() == "ERROR" -> Triple(Color.Red.copy(alpha = 0.2f), Color.Red, "Error")
        status.uppercase() == "MAINTENANCE" -> Triple(Orange.copy(alpha = 0.2f), Orange, "Mantenimiento")
        else -> Triple(Color.Gray.copy(alpha = 0.2f), Color.Gray, status)
    }

    Box(
        modifier = Modifier
            .background(backgroundColor, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(
                color = textColor,
                fontWeight = FontWeight.Medium,
                fontSize = 11.sp
            )
        )
    }
}

@Composable
private fun TechnicalInfoSection(
    camera: Device
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // IP Address
        TechnicalInfoItem(
            label = "Dirección IP",
            value = camera.ipAddress,
            icon = Icons.Default.List
        )

        // Port
        TechnicalInfoItem(
            label = "Puerto",
            value = camera.port.toString(),
            icon = Icons.Default.Settings
        )

        // Type
        TechnicalInfoItem(
            label = "Tipo",
            value = getDeviceTypeDisplayName(camera.type),
            icon = Icons.Default.List
        )
    }
}

@Composable
private fun TechnicalInfoItem(
    label: String,
    value: String,
    icon: ImageVector
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color.Gray,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color.Gray,
                    fontSize = 10.sp
                )
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
        )
    }
}

@Composable
private fun ActionButtonsSection(
    camera: Device,
    onDeleteClick: (Long) -> Unit,
    onToggleActiveClick: (Long) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Toggle Active Button
        OutlinedButton(
            onClick = {
                camera.id?.let { onToggleActiveClick(it) }
            },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = if (camera.active) Orange else Color.Green
            ),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                brush = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (camera.active) Orange else Color.Green
                ).brush
            )
        ) {
            Icon(
                imageVector = if (camera.active) Icons.Default.Star else Icons.Default.PlayArrow,
                contentDescription = if (camera.active) "Desactivar" else "Activar",
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = if (camera.active) "Desactivar" else "Activar",
                style = MaterialTheme.typography.labelSmall
            )
        }

        // Settings Button
        OutlinedButton(
            onClick = {
                // TODO: Navigate to camera settings
            },
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.Gray
            )
        ) {
            Icon(
                Icons.Default.Settings,
                contentDescription = "Configurar",
                modifier = Modifier.size(16.dp)
            )
        }

        // Delete Button
        OutlinedButton(
            onClick = {
                camera.id?.let { onDeleteClick(it) }
            },
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.Red
            ),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                brush = androidx.compose.foundation.BorderStroke(1.dp, Color.Red).brush
            )
        ) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "Eliminar",
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

// Helper functions
fun filterCameras(
    cameras: List<Device>,
    statusFilter: String,
    activeFilter: String
): List<Device> {
    return cameras.filter { camera ->
        val statusMatch = statusFilter == "TODOS" || camera.status.uppercase() == statusFilter
        val activeMatch = when (activeFilter) {
            "TODOS" -> true
            "ACTIVE" -> camera.active
            "INACTIVE" -> !camera.active
            else -> true
        }
        statusMatch && activeMatch
    }.sortedWith(
        compareByDescending<Device> { it.active }
            .thenBy { it.status }
            .thenBy { it.name }
    )
}

fun getCameraLocationFromName(name: String): String {
    return when {
        name.contains("entrada", ignoreCase = true) -> "Entrada Principal"
        name.contains("jardin", ignoreCase = true) -> "Jardín"
        name.contains("garage", ignoreCase = true) -> "Garaje"
        name.contains("cocina", ignoreCase = true) -> "Cocina"
        name.contains("sala", ignoreCase = true) -> "Sala de Estar"
        name.contains("dormitorio", ignoreCase = true) -> "Dormitorio"
        name.contains("patio", ignoreCase = true) -> "Patio"
        name.contains("exterior", ignoreCase = true) -> "Área Exterior"
        else -> "Ubicación no especificada"
    }
}

fun getDeviceTypeDisplayName(type: String): String {
    return when (type.uppercase()) {
        "CAMERA" -> "Cámara"
        "SENSOR" -> "Sensor"
        "ALARM" -> "Alarma"
        "DOOR" -> "Puerta"
        "OTHER" -> "Otro"
        else -> type.lowercase().replaceFirstChar { it.uppercase() }
    }
}


