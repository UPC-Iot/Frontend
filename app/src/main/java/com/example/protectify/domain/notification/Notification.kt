package com.example.protectify.domain.notification

import org.threeten.bp.LocalDateTime

data class Notification(
    val id: Long? = null,
    val userId: Long,
    val title: String,
    val message: String,
    val date: LocalDateTime
)