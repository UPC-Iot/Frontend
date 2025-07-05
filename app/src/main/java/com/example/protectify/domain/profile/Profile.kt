package com.example.protectify.domain.profile

data class Profile(
    val id: Long?,
    val userId: Long,
    val firstName: String,
    val lastName: String,
    val birthDate: String,
    val description: String?,
    val photo: String?,
    val address: String?,
    val phone: String?
)