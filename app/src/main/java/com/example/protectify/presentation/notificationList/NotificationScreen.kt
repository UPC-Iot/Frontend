package com.example.protectify.presentation.notificationList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.protectify.domain.notification.Notification
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale

@Composable
fun NotificationScreen(
    viewModel: NotificationViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state

    // Load notifications when screen is first composed
    LaunchedEffect(Unit) {
        viewModel.getNotificationList()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF26272C))
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
            Spacer(modifier = Modifier.height(24.dp))

            // Notifications Section
            when {
                state.isLoading -> {
                    LoadingNotificationsList()
                }
                state.data.isNullOrEmpty() -> {
                    EmptyNotificationsState()
                }
                else -> {
                    NotificationsList(
                        notifications = state.data!!,
                        onDeleteClick = { notificationId ->
                            viewModel.deleteNotification(notificationId)
                        }
                    )
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
                text = "Notificaciones",
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
private fun LoadingNotificationsList() {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(5) {
            NotificationCardLoading()
        }
    }
}

@Composable
private fun NotificationCardLoading() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2A2A2A)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Title placeholder
            Box(
                modifier = Modifier
                    .width(150.dp)
                    .height(16.dp)
                    .background(
                        Color.Gray.copy(alpha = 0.3f),
                        RoundedCornerShape(8.dp)
                    )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Message placeholder
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
                    .width(200.dp)
                    .height(14.dp)
                    .background(
                        Color.Gray.copy(alpha = 0.2f),
                        RoundedCornerShape(7.dp)
                    )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Date placeholder
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
    }
}

@Composable
private fun ErrorMessage(
    message: String
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
                Icons.Default.Warning,
                contentDescription = "Error",
                tint = Color.Red,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White
                ),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
private fun EmptyNotificationsState() {
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
                Icons.Default.Warning,
                contentDescription = "No notifications",
                tint = Color.Gray,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No hay notificaciones",
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Cuando recibas notificaciones aparecerán aquí",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Gray.copy(alpha = 0.7f)
                ),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
private fun NotificationsList(
    notifications: List<Notification>,
    onDeleteClick: (Long) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(notifications) { notification ->
            NotificationCard(
                notification = notification,
                onDeleteClick = onDeleteClick
            )
        }
    }
}

@Composable
private fun NotificationCard(
    notification: Notification,
    onDeleteClick: (Long) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2A2A2A)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header with title and delete button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Notification icon and title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        getNotificationIcon(notification.title),
                        contentDescription = "Notification type",
                        tint = getNotificationColor(notification.title),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = notification.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Delete button
                if (notification.id != null) {
                    IconButton(
                        onClick = { onDeleteClick(notification.id) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete notification",
                            tint = Color.Gray,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Message
            Text(
                text = notification.message,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White.copy(alpha = 0.8f),
                    lineHeight = 20.sp
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Date and time
            Text(
                text = formatNotificationDate(notification.date),
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color.Gray,
                    fontSize = 11.sp
                )
            )
        }
    }
}

private fun getNotificationIcon(title: String): androidx.compose.ui.graphics.vector.ImageVector {
    return when {
        title.contains("Seguridad", ignoreCase = true) ||
                title.contains("Alerta", ignoreCase = true) -> Icons.Default.Person
        title.contains("Visitante", ignoreCase = true) ||
                title.contains("Persona", ignoreCase = true) -> Icons.Default.Person
        title.contains("Cámara", ignoreCase = true) ||
                title.contains("Camera", ignoreCase = true) -> Icons.Default.AccountCircle
        title.contains("Casa", ignoreCase = true) ||
                title.contains("Home", ignoreCase = true) -> Icons.Default.Home
        title.contains("Sistema", ignoreCase = true) -> Icons.Default.Settings
        else -> Icons.Default.Notifications
    }
}

private fun getNotificationColor(title: String): Color {
    return when {
        title.contains("Seguridad", ignoreCase = true) ||
                title.contains("Alerta", ignoreCase = true) -> Color.Red
        title.contains("Visitante", ignoreCase = true) ||
                title.contains("Persona", ignoreCase = true) -> Color(0xFFE17055)
        title.contains("Cámara", ignoreCase = true) ||
                title.contains("Camera", ignoreCase = true) -> Color.Blue
        title.contains("Casa", ignoreCase = true) ||
                title.contains("Home", ignoreCase = true) -> Color.Green
        title.contains("Sistema", ignoreCase = true) -> Color.Gray
        else -> Color.White
    }
}

private fun formatNotificationDate(date: org.threeten.bp.LocalDateTime): String {
    val now = org.threeten.bp.LocalDateTime.now()
    val daysDiff = org.threeten.bp.temporal.ChronoUnit.DAYS.between(date.toLocalDate(), now.toLocalDate())

    return when {
        daysDiff == 0L -> {
            // Today - show time
            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
            "Hoy a las ${date.format(timeFormatter)}"
        }
        daysDiff == 1L -> {
            // Yesterday - show time
            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
            "Ayer a las ${date.format(timeFormatter)}"
        }
        daysDiff < 7L -> {
            // This week - show day and time
            val dayFormatter = DateTimeFormatter.ofPattern("EEEE", Locale("es", "ES"))
            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
            "${date.format(dayFormatter).capitalize()} a las ${date.format(timeFormatter)}"
        }
        else -> {
            // Older - show full date
            val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
            date.format(dateFormatter)
        }
    }
}


