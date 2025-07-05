package com.example.protectify.data.repository.owner

import com.example.protectify.common.Resource
import com.example.protectify.data.remote.owner.OwnerService
import com.example.protectify.data.remote.owner.toOwner
import com.example.protectify.domain.owner.Owner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OwnerRepository(
    private val ownerService: OwnerService
) {

    suspend fun getOwner(userId: Long, token: String): Resource<Owner> = withContext(Dispatchers.IO) {
        if (token.isBlank()) return@withContext Resource.Error("Un token es requerido")
        val response = ownerService.getOwner(userId, "Bearer $token")
        if (response.isSuccessful) {
            response.body()?.let { dto -> return@withContext Resource.Success(dto.toOwner()) }
            return@withContext Resource.Error("No se encontró el propietario")
        }
        Resource.Error(response.message())
    }

    suspend fun getOwnerById(id: Long, token: String): Resource<Owner> = withContext(Dispatchers.IO) {
        if (token.isBlank()) return@withContext Resource.Error("Un token es requerido")
        val response = ownerService.getOwnerById(id, "Bearer $token")
        if (response.isSuccessful) {
            response.body()?.let { dto -> return@withContext Resource.Success(dto.toOwner()) }
            return@withContext Resource.Error("No se encontró el propietario")
        }
        Resource.Error(response.message())
    }
}