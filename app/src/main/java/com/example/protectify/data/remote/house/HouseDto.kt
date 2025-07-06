package com.example.protectify.data.remote.house

import com.example.protectify.domain.house.House
import com.example.protectify.domain.owner.Owner

data class HouseDto(
    val id: Long? = null,
    val ownerId: Long,
    val address: String,
    val name: String,
    val description: String? = null
)

fun HouseDto.toHouse() = House(
    id = id,
    ownerId = ownerId,
    address = address,
    name = name,
    description = description,
)