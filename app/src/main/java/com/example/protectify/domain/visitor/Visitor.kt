package com.example.protectify.domain.visitor

import com.example.protectify.domain.house.House
import java.time.LocalDate

data class Visitor(
    val id: Long? = null,
    val houseId: Long,
    val firstname: String,
    val lastname: String,
    val photo: String,
    val role: String,
    val lastVisit: LocalDate?,
)