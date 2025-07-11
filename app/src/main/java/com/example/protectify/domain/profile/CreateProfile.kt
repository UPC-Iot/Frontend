package com.example.protectify.domain.profile

import java.time.LocalDate

data class CreateProfile(
    val userId: Long,
    val firstName: String,
    val lastName: String,
    val birthDate: String,
    val description: String?,
    val photo: String?,
    val phone: String?,
    val address: String?
)
