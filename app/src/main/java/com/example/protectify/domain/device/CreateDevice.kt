package com.example.protectify.domain.device

data class CreateDevice(
    val houseId: Long,
    val name: String,
    val type: String,
    val ipAddress: String,
    val port: Int,
    val status: String,
    val active: Boolean,
    val apiKey: String
)