package com.example.protectify.domain.authentication

data class AuthenticationResponse(
    val id: Long,
    val username: String,
    val token: String
)