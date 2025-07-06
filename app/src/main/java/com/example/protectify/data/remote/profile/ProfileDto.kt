package com.example.protectify.data.remote.profile

import com.example.protectify.domain.profile.Profile

data class ProfileDto(
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

fun ProfileDto.toProfile() = Profile(
    id = id,
    userId = userId,
    firstName = firstName,
    lastName = lastName,
    birthDate = birthDate,
    description = description,
    photo = photo,
    address = address,
    phone = phone
)