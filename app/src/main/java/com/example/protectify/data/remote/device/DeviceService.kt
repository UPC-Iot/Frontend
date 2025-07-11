package com.example.protectify.data.remote.device

import com.example.protectify.domain.device.CreateDevice
import com.example.protectify.domain.device.UpdateDevice
import retrofit2.Response
import retrofit2.http.*

interface DeviceService {
    @GET("devices")
    suspend fun getAllDevices(@Header("Authorization") token: String): Response<List<DeviceDto>>

    @GET("devices/{id}")
    suspend fun getDeviceById(
        @Path("id") id: Long,
        @Header("Authorization") token: String
    ): Response<DeviceDto>

    @GET("devices/house/{houseId}")
    suspend fun getAllDevicesByHouseId(
        @Path("houseId") houseId: Long,
        @Header("Authorization") token: String
    ): Response<List<DeviceDto>>

    @GET("devices/owner/{ownerId}")
    suspend fun getAllDevicesByOwnerId(
        @Path("ownerId") ownerId: Long,
        @Header("Authorization") token: String
    ): Response<List<DeviceDto>>

    @POST("devices")
    suspend fun createDevice(
        @Body device: CreateDevice,
        @Header("Authorization") token: String
    ): Response<DeviceDto>

    @PUT("devices/{id}")
    suspend fun updateDevice(
        @Path("id") id: Long,
        @Body device: UpdateDevice,
        @Header("Authorization") token: String
    ): Response<DeviceDto>

    @DELETE("devices/{id}")
    suspend fun deleteDevice(
        @Path("id") id: Long,
        @Header("Authorization") token: String
    ): Response<Unit>
}