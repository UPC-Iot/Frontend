package com.example.protectify.data.remote.owner

import com.example.protectify.domain.owner.Owner

data class OwnerDto(
    val id: Long,
    val userId: Long
)

fun OwnerDto.toOwner() = Owner(id, userId)