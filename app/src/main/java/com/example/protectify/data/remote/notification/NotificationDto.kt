package com.example.protectify.data.remote.notification

import com.example.protectify.domain.notification.Notification
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

data class NotificationDto(
    val id: Long? = null,
    val userId: Long,
    val title: String,
    val message: String,
    val date: String
)

fun NotificationDto.toNotification() = Notification(
    id = id,
    userId = userId,
    title = title,
    message = message,
    date = LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME)
)