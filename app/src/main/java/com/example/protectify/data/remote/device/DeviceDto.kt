package com.example.protectify.data.remote.device

import com.example.protectify.domain.device.Device


data class DeviceDto(
    val id: Long? = null,
    val houseId: Long,
    val name: String,
    val type: String,
    val ipAddress: String,
    val port: Int,
    val status: String,
    val active: Boolean,
    val apiKey: String
)

fun DeviceDto.toDevice() = Device(
    id = id,
    houseId = houseId,
    name = name,
    type = type,
    ipAddress = ipAddress,
    port = port,
    status = status,
    active = active,
    apiKey = apiKey
)