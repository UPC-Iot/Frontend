package com.example.protectify.data.remote.notification

import retrofit2.Response
import retrofit2.http.*
import com.example.protectify.domain.notification.CreateNotification
import com.example.protectify.domain.notification.UpdateNotification

interface NotificationService {
    @GET("notifications")
    suspend fun getAllNotifications(@Header("Authorization") token: String): Response<List<NotificationDto>>

    @GET("notifications/{id}")
    suspend fun getNotificationById(@Path("id") id: Long, @Header("Authorization") token: String): Response<NotificationDto>

    @GET("notifications/user/{userId}")
    suspend fun getAllNotificationsByUserId(@Path("userId") userId: Long, @Header("Authorization") token: String): Response<List<NotificationDto>>

    @POST("notifications")
    suspend fun createNotification(
        @Body notification: CreateNotification,
        @Header("Authorization") token: String
    ): Response<NotificationDto>

    @PUT("notifications/{id}")
    suspend fun updateNotification(
        @Path("id") id: Long,
        @Body notification: UpdateNotification,
        @Header("Authorization") token: String
    ): Response<NotificationDto>

    @DELETE("notifications/{id}")
    suspend fun deleteNotification(
        @Path("id") id: Long,
        @Header("Authorization") token: String
    ): Response<Unit>
}