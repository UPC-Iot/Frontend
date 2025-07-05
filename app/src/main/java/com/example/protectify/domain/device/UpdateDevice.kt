package com.example.protectify.domain.device

data class UpdateDevice(
    val name: String,
    val type: String,
    val ipAddress: String,
    val port: Int,
    val status: String,
    val active: Boolean,
    val apiKey: String
)