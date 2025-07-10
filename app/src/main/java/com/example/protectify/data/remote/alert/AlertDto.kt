package com.example.protectify.data.remote.alert

import com.example.protectify.domain.alert.Alert
import java.time.LocalDateTime
import java.util.Date

data class AlertDto(
    val id: Long? = null,
    val houseId: Long,
    val type: String,
    val message: String,
    val status: String,
    val timestamp: Date,
    val imageUrl: String,
)

fun AlertDto.toAlert() = Alert(
    id = id,
    houseId = houseId,
    type = type,
    message = message,
    status = status,
    timestamp = timestamp,
    imageUrl = imageUrl,
)