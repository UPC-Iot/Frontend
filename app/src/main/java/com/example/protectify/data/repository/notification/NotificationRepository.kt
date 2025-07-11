package com.example.protectify.data.repository.notification

import com.example.protectify.common.Resource
import com.example.protectify.data.remote.notification.NotificationService
import com.example.protectify.data.remote.notification.toNotification
import com.example.protectify.domain.notification.CreateNotification
import com.example.protectify.domain.notification.Notification
import com.example.protectify.domain.notification.UpdateNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NotificationRepository(
    private val notificationService: NotificationService
) {

    suspend fun getAllNotifications(token: String): Resource<List<Notification>> = withContext(Dispatchers.IO) {
        if (token.isBlank()) return@withContext Resource.Error("Un token es requerido")
        val response = notificationService.getAllNotifications("Bearer $token")
        if (response.isSuccessful) {
            response.body()?.let { dtos ->
                return@withContext Resource.Success(dtos.map { it.toNotification() })
            }
            return@withContext Resource.Error("No se encontraron notificaciones")
        }
        Resource.Error(response.message())
    }

    suspend fun getNotificationById(id: Long, token: String): Resource<Notification> = withContext(Dispatchers.IO) {
        if (token.isBlank()) return@withContext Resource.Error("Un token es requerido")
        val response = notificationService.getNotificationById(id, "Bearer $token")
        if (response.isSuccessful) {
            response.body()?.let { dto ->
                return@withContext Resource.Success(dto.toNotification())
            }
            return@withContext Resource.Error("No se encontró la notificación")
        }
        Resource.Error(response.message())
    }

    suspend fun getAllNotificationsByUserId(userId: Long, token: String): Resource<List<Notification>> = withContext(Dispatchers.IO) {
        if (token.isBlank()) return@withContext Resource.Error("Un token es requerido")
        val response = notificationService.getAllNotificationsByUserId(userId, "Bearer $token")
        if (response.isSuccessful) {
            response.body()?.let { dtos ->
                return@withContext Resource.Success(dtos.map { it.toNotification() })
            }
            return@withContext Resource.Error("No se encontraron notificaciones")
        }
        Resource.Error(response.message())
    }

    suspend fun createNotification(notification: CreateNotification, token: String): Resource<Notification> = withContext(Dispatchers.IO) {
        if (token.isBlank()) return@withContext Resource.Error("Un token es requerido")
        val request = CreateNotification(
            userId = notification.userId,
            title = notification.title,
            message = notification.message,
            date = notification.date
        )
        val response = notificationService.createNotification(request, "Bearer $token")
        if (response.isSuccessful) {
            response.body()?.let { dto -> return@withContext Resource.Success(dto.toNotification()) }
            return@withContext Resource.Error("Error al crear notificación")
        }
        Resource.Error(response.message())
    }

    suspend fun updateNotification(id: Long, notification: UpdateNotification, token: String): Resource<Notification> = withContext(Dispatchers.IO) {
        if (token.isBlank()) return@withContext Resource.Error("Un token es requerido")
        val request = UpdateNotification(
            title = notification.title,
            message = notification.message,
            date = notification.date
        )
        val response = notificationService.updateNotification(id, request, "Bearer $token")
        if (response.isSuccessful) {
            response.body()?.let { dto -> return@withContext Resource.Success(dto.toNotification()) }
            return@withContext Resource.Error("Error al actualizar notificación")
        }
        Resource.Error(response.message())
    }

    suspend fun deleteNotification(id: Long, token: String): Resource<Unit> = withContext(Dispatchers.IO) {
        if (token.isBlank()) return@withContext Resource.Error("Un token es requerido")
        val response = notificationService.deleteNotification(id, "Bearer $token")
        if (response.isSuccessful) {
            return@withContext Resource.Success(Unit)
        }
        Resource.Error(response.message())
    }
}