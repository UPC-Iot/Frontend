package com.example.protectify.domain.authentication

data class SignUpRequest(
    val username: String,
    val password: String,
    val roles: List<String>
)