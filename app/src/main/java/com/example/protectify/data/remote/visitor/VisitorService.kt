package com.example.protectify.data.remote.visitor

import com.example.protectify.domain.visitor.CreateVisitor
import com.example.protectify.domain.visitor.UpdateVisitor
import retrofit2.Response
import retrofit2.http.*

interface VisitorService {
    @GET("visitors")
    suspend fun getAllVisitors(@Header("Authorization") token: String): Response<List<VisitorDto>>

    @GET("visitors/{id}")
    suspend fun getVisitorById(
        @Path("id") id: Long,
        @Header("Authorization") token: String
    ): Response<VisitorDto>

    @GET("visitors/house/{houseId}")
    suspend fun getAllVisitorsByHouseId(
        @Path("houseId") houseId: Long,
        @Header("Authorization") token: String
    ): Response<List<VisitorDto>>

    @GET("visitors/owner/{ownerId}")
    suspend fun getAllVisitorsByOwnerId(
        @Path("ownerId") ownerId: Long,
        @Header("Authorization") token: String
    ): Response<List<VisitorDto>>

    @POST("visitors")
    suspend fun createVisitor(
        @Body visitor: CreateVisitor,
        @Header("Authorization") token: String
    ): Response<VisitorDto>

    @PUT("visitors/{id}")
    suspend fun updateVisitor(
        @Path("id") id: Long,
        @Body visitor: UpdateVisitor,
        @Header("Authorization") token: String
    ): Response<VisitorDto>

    @DELETE("visitors/{id}")
    suspend fun deleteVisitor(
        @Path("id") id: Long,
        @Header("Authorization") token: String
    ): Response<Unit>
}