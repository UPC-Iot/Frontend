package com.example.protectify.data.repository.house

import com.example.protectify.common.Resource
import com.example.protectify.data.remote.house.HouseService
import com.example.protectify.data.remote.house.toHouse
import com.example.protectify.domain.house.CreateHouse
import com.example.protectify.domain.house.House
import com.example.protectify.domain.house.UpdateHouse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HouseRepository(
    private val houseService: HouseService
) {

    suspend fun getAllHouses(token: String): Resource<List<House>> = withContext(Dispatchers.IO) {
        if (token.isBlank()) return@withContext Resource.Error("Un token es requerido")
        val response = houseService.getAllHouses("Bearer $token")
        if (response.isSuccessful) {
            response.body()?.let { dtos ->
                return@withContext Resource.Success(dtos.map { it.toHouse() })
            }
            return@withContext Resource.Error("No se encontraron casas")
        }
        Resource.Error(response.message())
    }

    suspend fun getHouseById(id: Long, token: String): Resource<House> = withContext(Dispatchers.IO) {
        if (token.isBlank()) return@withContext Resource.Error("Un token es requerido")
        val response = houseService.getHouseById(id, "Bearer $token")
        if (response.isSuccessful) {
            response.body()?.let { dto -> return@withContext Resource.Success(dto.toHouse()) }
            return@withContext Resource.Error("No se encontró la casa")
        }
        Resource.Error(response.message())
    }

    suspend fun getHousesByOwnerId(ownerId: Long, token: String): Resource<List<House>> = withContext(Dispatchers.IO) {
        if (token.isBlank()) return@withContext Resource.Error("Un token es requerido")
        val response = houseService.getHousesByOwnerId(ownerId, "Bearer $token")
        if (response.isSuccessful) {
            response.body()?.let { dtos ->
                return@withContext Resource.Success(dtos.map { it.toHouse() })
            }
            return@withContext Resource.Error("No se encontraron casas para el propietario")
        }
        Resource.Error(response.message())
    }

    suspend fun createHouse(house: CreateHouse, token: String): Resource<House> = withContext(Dispatchers.IO) {
        if (token.isBlank()) return@withContext Resource.Error("Un token es requerido")
        val request = CreateHouse(
            ownerId = house.ownerId,
            address = house.address,
            name = house.name,
            description = house.description
        )
        val response = houseService.createHouse(request, "Bearer $token")
        if (response.isSuccessful) {
            response.body()?.let { dto -> return@withContext Resource.Success(dto.toHouse()) }
            return@withContext Resource.Error("Error al crear casa")
        }
        Resource.Error(response.message())
    }

    suspend fun updateHouse(id: Long, house: UpdateHouse, token: String): Resource<House> = withContext(Dispatchers.IO) {
        if (token.isBlank()) return@withContext Resource.Error("Un token es requerido")
        val request = UpdateHouse(
            address = house.address,
            name = house.name,
            description = house.description
        )
        val response = houseService.updateHouse(id, request, "Bearer $token")
        if (response.isSuccessful) {
            response.body()?.let { dto -> return@withContext Resource.Success(dto.toHouse()) }
            return@withContext Resource.Error("Error al actualizar casa")
        }
        Resource.Error(response.message())
    }

    suspend fun deleteHouse(id: Long, token: String): Resource<Unit> = withContext(Dispatchers.IO) {
        if (token.isBlank()) return@withContext Resource.Error("Un token es requerido")
        val response = houseService.deleteHouse(id, "Bearer $token")
        if (response.isSuccessful) {
            return@withContext Resource.Success(Unit)
        }
        Resource.Error(response.message())
    }
}