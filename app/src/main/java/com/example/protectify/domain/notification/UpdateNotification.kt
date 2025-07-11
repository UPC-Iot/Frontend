package com.example.protectify.domain.notification

import java.time.LocalDateTime

data class UpdateNotification(
    val title: String,
    val message: String,
    val date: LocalDateTime
)