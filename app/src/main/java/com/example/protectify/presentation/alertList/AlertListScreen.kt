package com.example.protectify.presentation.alertList

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.protectify.domain.alert.Alert
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AlertListScreen(
    viewModel: AlertListViewModel,
    modifier: Modifier = Modifier
) {
    val alertState by viewModel.alertState
    var selectedFilter by remember { mutableStateOf("TODOS") }
    var selectedStatus by remember { mutableStateOf("TODOS") }

    // Load data when screen is first composed
    LaunchedEffect(Unit) {
        viewModel.getAlerts()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
        // Header
        HeaderSection(
            onBackClick = {
                viewModel.goBack()
            }
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
                selectedFilter = selectedFilter,
                selectedStatus = selectedStatus,
                onFilterChange = { selectedFilter = it },
                onStatusChange = { selectedStatus = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Alerts List
            when {
                alertState.isLoading -> {
                    LoadingAlertsList()
                }
                else -> {
                    val filteredAlerts = filterAlerts(
                        alerts = alertState.data ?: emptyList(),
                        typeFilter = selectedFilter,
                        statusFilter = selectedStatus
                    )

                    if (filteredAlerts.isEmpty()) {
                        EmptyAlertsState(
                            hasFilters = selectedFilter != "TODOS" || selectedStatus != "TODOS"
                        )
                    } else {
                        AlertsList(
                            alerts = filteredAlerts,
                            onAlertClick = {},
                            onDeleteClick = { alertId ->
                                viewModel.deleteAlert(alertId)
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
    onBackClick: () -> Unit
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
                text = "Historial de Alertas",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }

        // Spacer to balance the back button
        Spacer(modifier = Modifier.size(40.dp))
    }
}

@Composable
private fun FiltersSection(
    selectedFilter: String,
    selectedStatus: String,
    onFilterChange: (String) -> Unit,
    onStatusChange: (String) -> Unit
) {
    Column {
        // Type Filter
        Text(
            text = "Filtrar por tipo:",
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
            val typeFilters = listOf(
                "TODOS" to "Todos",
                "INTRUDER_DETECTED" to "Intruso",
                "SUSPICIOUS_MOVEMENT" to "Movimiento",
                "UNKNOWN_PERSON" to "Desconocido",
                "SYSTEM_FAILURE" to "Sistema"
            )

            items(typeFilters.size) { index ->
                val (value, label) = typeFilters[index]
                FilterChip(
                    selected = selectedFilter == value,
                    onClick = { onFilterChange(value) },
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
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val statusFilters = listOf(
                "TODOS" to "Todos",
                "PENDING" to "Pendiente",
                "VIEWED" to "Vista",
                "CONFIRMED" to "Confirmada",
                "FALSE_ALARM" to "Falsa"
            )

            items(statusFilters.size) { index ->
                val (value, label) = statusFilters[index]
                FilterChip(
                    selected = selectedStatus == value,
                    onClick = { onStatusChange(value) },
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
private fun LoadingAlertsList() {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(5) {
            AlertCardLoading()
        }
    }
}

@Composable
private fun AlertCardLoading() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2A2A2A)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon placeholder
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        Color.Gray.copy(alpha = 0.3f),
                        RoundedCornerShape(8.dp)
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
                        .height(16.dp)
                        .background(
                            Color.Gray.copy(alpha = 0.3f),
                            RoundedCornerShape(8.dp)
                        )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(14.dp)
                        .background(
                            Color.Gray.copy(alpha = 0.2f),
                            RoundedCornerShape(7.dp)
                        )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(12.dp)
                        .background(
                            Color.Gray.copy(alpha = 0.1f),
                            RoundedCornerShape(6.dp)
                        )
                )
            }

            // Delete button placeholder
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        Color.Gray.copy(alpha = 0.3f),
                        RoundedCornerShape(16.dp)
                    )
            )
        }
    }
}

@Composable
private fun EmptyAlertsState(
    hasFilters: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally // Corregido
        ) {
            Icon(
                if (hasFilters) Icons.Default.List else Icons.Default.ThumbUp, // Corregido
                contentDescription = if (hasFilters) "No results" else "No alerts",
                tint = if (hasFilters) Color.Gray else Color.Green,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = if (hasFilters) "No se encontraron alertas" else "Sistema Seguro",
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
                    "No hay alertas de seguridad registradas",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Gray.copy(alpha = 0.7f)
                ),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
private fun AlertsList(
    alerts: List<Alert>,
    onAlertClick: (Long) -> Unit,
    onDeleteClick: (Long) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(alerts) { alert ->
            AlertListCard(
                alert = alert,
                onClick = onAlertClick,
                onDeleteClick = onDeleteClick
            )
        }
    }
}

@Composable
private fun AlertListCard(
    alert: Alert,
    onClick: (Long) -> Unit,
    onDeleteClick: (Long) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                alert.id?.let { onClick(it) }
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = getAlertBackgroundColor(alert.type, alert.status)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Alert icon with image if available
            AlertIconSection(alert = alert)

            Spacer(modifier = Modifier.width(16.dp))

            // Alert content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Alert type and time
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = getAlertTypeDisplayName(alert.type),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = getAlertTextColor(alert.status)
                        )
                    )
                    Text(
                        text = formatAlertDateTime(alert.timestamp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color.Gray,
                            fontSize = 11.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Alert message
                Text(
                    text = alert.message,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = getAlertTextColor(alert.status).copy(alpha = 0.8f)
                    ),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Status chip
                AlertStatusChip(status = alert.status)
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Delete button
            if (alert.id != null) {
                IconButton(
                    onClick = { onDeleteClick(alert.id) },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete alert",
                        tint = Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun AlertIconSection(
    alert: Alert
) {
    Box(
        modifier = Modifier.size(48.dp),
        contentAlignment = Alignment.Center
    ) {
        if (alert.imageUrl.isNotBlank()) {
            // Show image if available
            GlideImage(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp)),
                imageModel = { alert.imageUrl },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                ),
                loading = {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    }
                },
                failure = {
                    AlertTypeIcon(alert.type, alert.status)
                }
            )
        } else {
            // Show type icon
            AlertTypeIcon(alert.type, alert.status)
        }
    }
}

@Composable
private fun AlertTypeIcon(
    type: String,
    status: String
) {
    val (icon, color) = getAlertIconAndColor(type, status)

    Box(
        modifier = Modifier
            .size(48.dp)
            .background(color.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = type,
            tint = color,
            modifier = Modifier.size(28.dp)
        )
    }
}

@Composable
private fun AlertStatusChip(
    status: String
) {
    val (backgroundColor, textColor) = when (status.uppercase()) {
        "PENDING" -> Color(0xFFFF6B35).copy(alpha = 0.2f) to Color(0xFFFF6B35)
        "VIEWED" -> Color.Blue.copy(alpha = 0.2f) to Color.Blue
        "CONFIRMED" -> Color.Red.copy(alpha = 0.2f) to Color.Red
        "FALSE_ALARM" -> Color.Green.copy(alpha = 0.2f) to Color.Green
        else -> Color.Gray.copy(alpha = 0.2f) to Color.Gray
    }

    Box(
        modifier = Modifier
            .background(backgroundColor, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = getStatusDisplayName(status),
            style = MaterialTheme.typography.labelSmall.copy(
                color = textColor,
                fontWeight = FontWeight.Medium,
                fontSize = 11.sp
            )
        )
    }
}

// Helper functions
private fun filterAlerts(
    alerts: List<Alert>,
    typeFilter: String,
    statusFilter: String
): List<Alert> {
    return alerts.filter { alert ->
        val typeMatch = typeFilter == "TODOS" || alert.type.uppercase() == typeFilter
        val statusMatch = statusFilter == "TODOS" || alert.status.uppercase() == statusFilter
        typeMatch && statusMatch
    }.sortedByDescending { it.timestamp }
}

private val Orange = Color(0xFFFFA500)
private val Purple = Color(0xFF9C27B0)

private fun getAlertIconAndColor(type: String, status: String): Pair<ImageVector, Color> {
    val isActive = status.uppercase() in listOf("PENDING", "CONFIRMED")

    return when (type.uppercase()) {
        "INTRUDER_DETECTED" -> Icons.Default.Warning to if (isActive) Color.Red else Color.Gray
        "SUSPICIOUS_MOVEMENT" -> Icons.Default.Face to if (isActive) Orange else Color.Gray
        "UNKNOWN_PERSON" -> Icons.Default.Face to if (isActive) Color(0xFFFF6B35) else Color.Gray
        "SYSTEM_FAILURE" -> Icons.Default.Warning to if (isActive) Purple else Color.Gray
        else -> Icons.Default.Notifications to if (isActive) Color.White else Color.Gray
    }
}

private fun getAlertBackgroundColor(type: String, status: String): Color {
    val isActive = status.uppercase() in listOf("PENDING", "CONFIRMED")

    return when (type.uppercase()) {
        "INTRUDER_DETECTED" -> if (isActive) Color.Red.copy(alpha = 0.1f) else Color(0xFF3A3A3A)
        "SUSPICIOUS_MOVEMENT" -> if (isActive) Orange.copy(alpha = 0.1f) else Color(0xFF3A3A3A)
        "UNKNOWN_PERSON" -> if (isActive) Color(0xFFFF6B35).copy(alpha = 0.1f) else Color(0xFF3A3A3A)
        "SYSTEM_FAILURE" -> if (isActive) Purple.copy(alpha = 0.1f) else Color(0xFF3A3A3A)
        else -> Color(0xFF3A3A3A)
    }
}

private fun getAlertTextColor(status: String): Color {
    return when (status.uppercase()) {
        "PENDING", "CONFIRMED" -> Color.White
        "VIEWED", "FALSE_ALARM" -> Color.Gray
        else -> Color.White.copy(alpha = 0.8f)
    }
}

private fun getAlertTypeDisplayName(type: String): String {
    return when (type.uppercase()) {
        "INTRUDER_DETECTED" -> "Intruso Detectado"
        "SUSPICIOUS_MOVEMENT" -> "Movimiento Sospechoso"
        "UNKNOWN_PERSON" -> "Persona Desconocida"
        "SYSTEM_FAILURE" -> "Falla del Sistema"
        else -> type.replace("_", " ").split(" ").joinToString(" ") {
            it.lowercase().replaceFirstChar { char -> char.uppercase() }
        }
    }
}

private fun getStatusDisplayName(status: String): String {
    return when (status.uppercase()) {
        "PENDING" -> "Pendiente"
        "VIEWED" -> "Vista"
        "CONFIRMED" -> "Confirmada"
        "FALSE_ALARM" -> "Falsa Alarma"
        else -> status.lowercase().replaceFirstChar { it.uppercase() }
    }
}

private fun formatAlertDateTime(timestamp: Date): String {
    val now = Date()
    val diffInMillis = now.time - timestamp.time
    val diffInMinutes = diffInMillis / (1000 * 60)
    val diffInHours = diffInMinutes / 60
    val diffInDays = diffInHours / 24

    return when {
        diffInMinutes < 1 -> "Ahora"
        diffInMinutes < 60 -> "${diffInMinutes}m"
        diffInHours < 24 -> "${diffInHours}h"
        diffInDays < 7 -> {
            val dayFormat = SimpleDateFormat("EEEE", Locale("es", "ES"))
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            "${dayFormat.format(timestamp)} ${timeFormat.format(timestamp)}"
        }
        else -> {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            dateFormat.format(timestamp)
        }
    }
}


