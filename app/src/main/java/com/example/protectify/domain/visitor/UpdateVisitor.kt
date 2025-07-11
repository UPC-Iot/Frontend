package com.example.protectify.domain.visitor

import java.time.LocalDate

data class UpdateVisitor(
    val id: Long,
    val firstname: String,
    val lastname: String,
    val photo: String,
    val role: String,
    val lastVisit: LocalDate?
)