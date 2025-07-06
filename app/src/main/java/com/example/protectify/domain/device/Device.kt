package com.example.protectify.domain.device

import com.example.protectify.domain.house.House
import java.io.Serializable

data class Device(
    val id: Long? = null,
    val houseId: Long,
    val name: String,
    val type: String,
    val ipAddress: String,
    val port: Int,
    val status: String,
    val active: Boolean,
    val apiKey: String
) : Serializable