package com.example.protectify.data.remote.house

import retrofit2.Response
import retrofit2.http.*
import com.example.protectify.domain.house.CreateHouse
import com.example.protectify.domain.house.UpdateHouse

interface HouseService {
    @GET("houses")
    suspend fun getAllHouses(@Header("Authorization") token: String): Response<List<HouseDto>>

    @GET("houses/{id}")
    suspend fun getHouseById(@Path("id") id: Long, @Header("Authorization") token: String): Response<HouseDto>

    @GET("houses/owner/{ownerId}")
    suspend fun getHousesByOwnerId(@Path("ownerId") ownerId: Long, @Header("Authorization") token: String): Response<List<HouseDto>>

    @POST("houses")
    suspend fun createHouse(
        @Body house: CreateHouse,
        @Header("Authorization") token: String
    ): Response<HouseDto>

    @PUT("houses/{id}")
    suspend fun updateHouse(
        @Path("id") id: Long,
        @Body house: UpdateHouse,
        @Header("Authorization") token: String
    ): Response<HouseDto>

    @DELETE("houses/{id}")
    suspend fun deleteHouse(
        @Path("id") id: Long,
        @Header("Authorization") token: String
    ): Response<Unit>
}