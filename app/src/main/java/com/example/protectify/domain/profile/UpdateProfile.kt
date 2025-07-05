package com.example.protectify.domain.profile

import java.time.LocalDate

data class UpdateProfile(
    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate,
    val description: String?,
    val photo: String?,
    val phone: String?,
    val address: String?
)