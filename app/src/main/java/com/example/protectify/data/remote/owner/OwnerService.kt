package com.example.protectify.data.remote.owner

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface OwnerService {
    @GET("owners/{userId}/user")
    suspend fun getOwnerByUserId(@Path("userId") userId: Long, @Header("Authorization") token: String): Response<OwnerDto>
    @GET("owners/{id}")
    suspend fun getOwnerById(@Path("id") id: Long, @Header("Authorization") token: String): Response<OwnerDto>
}