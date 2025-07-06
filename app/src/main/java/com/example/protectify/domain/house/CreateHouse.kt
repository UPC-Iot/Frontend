package com.example.protectify.domain.house

data class CreateHouse(
    val ownerId: Long,
    val address: String,
    val name: String,
    val description: String? = null
)