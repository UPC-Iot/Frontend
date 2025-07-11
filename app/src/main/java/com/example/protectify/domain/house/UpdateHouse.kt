package com.example.protectify.domain.house

data class UpdateHouse(
    val address: String,
    val name: String,
    val description: String? = null
)