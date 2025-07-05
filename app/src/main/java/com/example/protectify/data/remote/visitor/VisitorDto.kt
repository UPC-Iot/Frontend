package com.example.protectify.data.remote.visitor

import com.example.protectify.domain.visitor.Visitor
import java.time.LocalDate

data class VisitorDto(
    val id: Long? = null,
    val houseId: Long,
    val firstname: String,
    val lastname: String,
    val photo: String,
    val role: String,
    val lastVisit: LocalDate?,
)

fun VisitorDto.toVisitor() = Visitor(
    id = id,
    firstname = firstname,
    lastname = lastname,
    photo = photo,
    role = role,
    lastVisit = lastVisit,
    houseId = houseId
)