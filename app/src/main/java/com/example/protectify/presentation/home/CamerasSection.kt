package com.example.protectify.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.unit.dp
import com.example.protectify.domain.device.Device


@Composable
fun CamerasSection(
    devices: List<Device>,
    isLoading: Boolean,
    selectedFilter: String = "Ver",
    onFilterClick: () -> Unit,
    onDeviceClick: ((Long) -> Unit)? = null,
    onDeviceMenuClick: ((Long) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2A2A2A) // Color gris oscuro
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header con título y filtro
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Camaras",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )

                // Filtro dropdown
                Row(
                    modifier = Modifier.clickable { onFilterClick() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedFilter,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.Gray
                        )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = "Filter dropdown",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Contenido: Loading o Grid de cámaras
            if (isLoading) {
                LoadingCamerasContent()
            } else {
                CamerasContent(
                    devices = devices,
                    onDeviceClick = onDeviceClick,
                    onDeviceMenuClick = onDeviceMenuClick
                )
            }
        }
    }
}

@Composable
private fun LoadingCamerasContent() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.height(240.dp) // Altura fija para loading
    ) {
        items(4) {
            CameraLoadingItem()
        }
    }
}

@Composable
private fun CameraLoadingItem() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Gray.copy(alpha = 0.3f)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Icon placeholder
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        Color.White.copy(alpha = 0.3f),
                        RoundedCornerShape(8.dp)
                    )
                    .align(Alignment.TopStart)
            )

            // Name placeholder
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(16.dp)
                    .background(
                        Color.White.copy(alpha = 0.3f),
                        RoundedCornerShape(8.dp)
                    )
                    .align(Alignment.BottomStart)
            )

            // Menu placeholder
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(
                        Color.White.copy(alpha = 0.3f),
                        RoundedCornerShape(10.dp)
                    )
                    .align(Alignment.TopEnd)
            )
        }
    }
}

@Composable
private fun CamerasContent(
    devices: List<Device>,
    onDeviceClick: ((Long) -> Unit)?,
    onDeviceMenuClick: ((Long) -> Unit)?
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.heightIn(max = 400.dp) // Altura máxima para evitar overflow
    ) {
        items(devices) { device ->
            CameraCard(
                device = device,
                onClick = onDeviceClick,
                onMenuClick = onDeviceMenuClick
            )
        }
    }
}

@Composable
private fun CameraCard(
    device: Device,
    onClick: ((Long) -> Unit)?,
    onMenuClick: ((Long) -> Unit)?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .then(
                if (onClick != null && device.id != null) {
                    Modifier.clickable { onClick(device.id) }
                } else Modifier
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF3A3A3A) // Color más claro que el fondo
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Status indicator
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(
                        if (device.active) Color.Green else Color.Red,
                        RoundedCornerShape(4.dp)
                    )
                    .align(Alignment.TopStart)
            )

            // Device icon
            Icon(
                imageVector = Icons.Filled.Home,
                contentDescription = device.name,
                tint = Color.White,
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.CenterStart)
            )

            // Device name
            Text(
                text = device.name,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                ),
                modifier = Modifier.align(Alignment.BottomStart)
            )

            // Device status
            Text(
                text = device.status,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = if (device.active) Color.Green else Color.Gray
                ),
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .offset(y = (-20).dp)
            )

            // Menu button
            if (onMenuClick != null && device.id != null) {
                IconButton(
                    onClick = { onMenuClick(device.id) },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "More options",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}





