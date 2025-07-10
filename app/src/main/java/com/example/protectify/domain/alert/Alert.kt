package com.example.protectify.domain.alert

import com.example.protectify.domain.house.House
import java.time.LocalDateTime
import java.util.Date

data class Alert(
    val id: Long? = null,
    val houseId: Long,
    val type: String,
    val message: String,
    val status: String,
    val timestamp: Date,
    val imageUrl: String

)