package com.example.protectify.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity (
    @PrimaryKey
    val id: Long,
    @ColumnInfo(name = "token")
    val token: String,
)