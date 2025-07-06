package com.example.protectify.domain.house

import com.example.protectify.domain.owner.Owner

data class House(
    val id: Long? = null,
    val ownerId: Long,
    val address: String,
    val name: String,
    val description: String? = null
)