package com.example.protectify.domain.alert

import java.time.LocalDateTime

data class UpdateAlert(
    val type: String,
    val message: String,
    val status: String,
    val timestamp: LocalDateTime,
    val imageUrl: String
)