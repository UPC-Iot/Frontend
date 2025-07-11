package com.example.protectify.data.remote.profile

import retrofit2.Response
import retrofit2.http.*
import com.example.protectify.domain.profile.CreateProfile
import com.example.protectify.domain.profile.UpdateProfile

interface ProfileService {
    @GET("profiles")
    suspend fun getAllProfiles(@Header("Authorization") token: String): Response<List<ProfileDto>>

    @GET("profiles/{id}")
    suspend fun getProfileById(@Path("id") id: Long, @Header("Authorization") token: String): Response<ProfileDto>

    @GET("profiles/{userId}/user")
    suspend fun getProfileByUserId(@Path("userId") userId: Long, @Header("Authorization") token: String): Response<ProfileDto>

    @POST("profiles")
    suspend fun createProfile(
        @Body profile: CreateProfile,
        @Header("Authorization") token: String
    ): Response<ProfileDto>

    @PUT("profiles/{id}")
    suspend fun updateProfile(
        @Path("id") id: Long,
        @Body profile: UpdateProfile,
        @Header("Authorization") token: String
    ): Response<ProfileDto>

    @DELETE("profiles/{id}")
    suspend fun deleteProfile(
        @Path("id") id: Long,
        @Header("Authorization") token: String
    ): Response<Unit>
}