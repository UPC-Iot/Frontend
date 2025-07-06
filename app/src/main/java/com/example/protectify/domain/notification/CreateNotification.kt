package com.example.protectify.domain.notification

import java.time.LocalDateTime

data class CreateNotification(
    val userId: Long,
    val title: String,
    val message: String,
    val date: LocalDateTime
)