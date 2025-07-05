package com.example.protectify.data.remote.alert

import com.example.protectify.domain.alert.CreateAlert
import com.example.protectify.domain.alert.UpdateAlert
import retrofit2.Response
import retrofit2.http.*

interface AlertService {
    @GET("alerts")
    suspend fun getAllAlerts(@Header("Authorization") token: String): Response<List<AlertDto>>

    @GET("alerts/{id}")
    suspend fun getAlertById(
        @Path("id") id: Long,
        @Header("Authorization") token: String
    ): Response<AlertDto>

    @GET("alerts/house/{houseId}")
    suspend fun getAllAlertsByHouseId(
        @Path("houseId") houseId: Long,
        @Header("Authorization") token: String
    ): Response<List<AlertDto>>

    @GET("alerts/owner/{ownerId}")
    suspend fun getAllAlertsByOwnerId(
        @Path("ownerId") ownerId: Long,
        @Header("Authorization") token: String
    ): Response<List<AlertDto>>

    @POST("alerts")
    suspend fun createAlert(
        @Body alert: CreateAlert,
        @Header("Authorization") token: String
    ): Response<AlertDto>

    @PUT("alerts/{id}")
    suspend fun updateAlert(
        @Path("id") id: Long,
        @Body alert: UpdateAlert,
        @Header("Authorization") token: String
    ): Response<AlertDto>

    @DELETE("alerts/{id}")
    suspend fun deleteAlert(
        @Path("id") id: Long,
        @Header("Authorization") token: String
    ): Response<Unit>
}