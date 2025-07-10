package com.example.protectify.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale

@Composable
fun AlertsSection(
    alerts: List<Alert>,
    isLoading: Boolean,
    onAlertClick: ((Long) -> Unit)? = null,
    onMarkAsResolvedClick: ((Long) -> Unit)? = null,
    onViewAllClick: () -> Unit = {},
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
            // Header con título y botón ver todas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = "Alerts",
                        tint = Color(0xFFFF6B35),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Alertas Recientes",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }

                // Ver todas button
                TextButton(
                    onClick = onViewAllClick
                ) {
                    Text(
                        text = "Ver todas",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFFE17055)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Contenido: Loading o Lista de alertas
            if (isLoading) {
                LoadingAlertsContent()
            } else {
                AlertsContent(
                    alerts = alerts,
                    onAlertClick = onAlertClick,
                    onMarkAsResolvedClick = onMarkAsResolvedClick
                )
            }
        }
    }
}

@Composable
private fun LoadingAlertsContent() {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        repeat(3) {
            AlertLoadingItem()
        }
    }
}

@Composable
private fun AlertLoadingItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon placeholder
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    Color.Gray.copy(alpha = 0.3f),
                    RoundedCornerShape(8.dp)
                )
        )

        Spacer(modifier = Modifier.width(12.dp))

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
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(14.dp)
                    .background(
                        Color.Gray.copy(alpha = 0.2f),
                        RoundedCornerShape(7.dp)
                    )
            )
            Spacer(modifier = Modifier.height(4.dp))
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
}

@Composable
private fun AlertsContent(
    alerts: List<Alert>,
    onAlertClick: ((Long) -> Unit)?,
    onMarkAsResolvedClick: ((Long) -> Unit)?
) {
    if (alerts.isEmpty()) {
        EmptyAlertsState()
    } else {
        LazyColumn(
            modifier = Modifier.heightIn(max = 300.dp), // Altura máxima para evitar overflow
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(alerts.take(5)) { alert -> // Mostrar máximo 5 alertas
                AlertCard(
                    alert = alert,
                    onClick = onAlertClick,
                    onMarkAsResolvedClick = onMarkAsResolvedClick
                )
            }
        }
    }
}

@Composable
private fun EmptyAlertsState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.Warning,
            contentDescription = "No alerts",
            tint = Color.Green,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Sistema Seguro",
            style = MaterialTheme.typography.titleMedium.copy(
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "No hay alertas de seguridad activas",
            style = MaterialTheme.typography.bodySmall.copy(
                color = Color.Gray
            )
        )
    }
}

@Composable
private fun AlertCard(
    alert: Alert,
    onClick: ((Long) -> Unit)?,
    onMarkAsResolvedClick: ((Long) -> Unit)?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (onClick != null && alert.id != null) {
                    Modifier.clickable { onClick(alert.id) }
                } else Modifier
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = getAlertBackgroundColor(alert.type, alert.status)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Alert icon with image if available
            AlertIconSection(
                alert = alert
            )

            Spacer(modifier = Modifier.width(12.dp))

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
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = getAlertTextColor(alert.status)
                        )
                    )
                    Text(
                        text = formatAlertTime(alert.timestamp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color.Gray,
                            fontSize = 10.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Alert message
                Text(
                    text = alert.message,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = getAlertTextColor(alert.status).copy(alpha = 0.8f)
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Status and action
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AlertStatusChip(
                        status = alert.status
                    )

                    // Action button based on status
                    when (alert.status.uppercase()) {
                        "PENDING" -> {
                            if (onMarkAsResolvedClick != null && alert.id != null) {
                                TextButton(
                                    onClick = { onMarkAsResolvedClick(alert.id) },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "Confirmar",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color(0xFFE17055),
                                            fontSize = 10.sp
                                        )
                                    )
                                }
                            }
                        }
                        "CONFIRMED" -> {
                            if (onMarkAsResolvedClick != null && alert.id != null) {
                                TextButton(
                                    onClick = { onMarkAsResolvedClick(alert.id) },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "Marcar Falsa",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color.Gray,
                                            fontSize = 10.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
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
        modifier = Modifier.size(40.dp),
        contentAlignment = Alignment.Center
    ) {
        if (alert.imageUrl.isNotBlank()) {
            // Show image if available
            GlideImage(
                modifier = Modifier
                    .size(40.dp)
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
                            modifier = Modifier.size(16.dp),
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
            .size(40.dp)
            .background(color.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = type,
            tint = color,
            modifier = Modifier.size(24.dp)
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
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = getStatusDisplayName(status),
            style = MaterialTheme.typography.labelSmall.copy(
                color = textColor,
                fontWeight = FontWeight.Medium,
                fontSize = 10.sp
            )
        )
    }
}

// Helper functions - actualizar con los valores reales del backend

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

private fun formatAlertTime(timestamp: Date): String {
    val now = Date()
    val diffMillis = now.time - timestamp.time
    val minutesDiff = diffMillis / (60 * 1000)

    return when {
        minutesDiff < 1 -> "Ahora"
        minutesDiff < 60 -> "${minutesDiff}m"
        minutesDiff < 1440 -> "${minutesDiff / 60}h"
        else -> SimpleDateFormat("dd/MM", Locale.getDefault()).format(timestamp)
    }
}