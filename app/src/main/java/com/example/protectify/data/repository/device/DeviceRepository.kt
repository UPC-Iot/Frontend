package com.example.protectify.data.repository.device

import com.example.protectify.common.Resource
import com.example.protectify.data.remote.device.DeviceService
import com.example.protectify.data.remote.device.toDevice
import com.example.protectify.domain.device.CreateDevice
import com.example.protectify.domain.device.Device
import com.example.protectify.domain.device.UpdateDevice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeviceRepository(
    private val deviceService: DeviceService
) {

    suspend fun getAllDevices(token: String): Resource<List<Device>> = withContext(Dispatchers.IO) {
        if (token.isBlank()) return@withContext Resource.Error("Un token es requerido")
        val response = deviceService.getAllDevices("Bearer $token")
        if (response.isSuccessful) {
            response.body()?.let { dtos ->
                return@withContext Resource.Success(dtos.map { it.toDevice() })
            }
            return@withContext Resource.Error("No se encontraron dispositivos")
        }
        Resource.Error(response.message())
    }

    suspend fun getDeviceById(id: Long, token: String): Resource<Device> = withContext(Dispatchers.IO) {
        if (token.isBlank()) return@withContext Resource.Error("Un token es requerido")
        val response = deviceService.getDeviceById(id, "Bearer $token")
        if (response.isSuccessful) {
            response.body()?.let { dto -> return@withContext Resource.Success(dto.toDevice()) }
            return@withContext Resource.Error("No se encontró el dispositivo")
        }
        Resource.Error(response.message())
    }

    suspend fun getAllDevicesByHouseId(houseId: Long, token: String): Resource<List<Device>> = withContext(Dispatchers.IO) {
        if (token.isBlank()) return@withContext Resource.Error("Un token es requerido")
        val response = deviceService.getAllDevicesByHouseId(houseId, "Bearer $token")
        if (response.isSuccessful) {
            response.body()?.let { dtos ->
                return@withContext Resource.Success(dtos.map { it.toDevice() })
            }
            return@withContext Resource.Error("No se encontraron dispositivos para la casa")
        }
        Resource.Error(response.message())
    }

    suspend fun getAllDevicesByOwnerId(ownerId: Long, token: String): Resource<List<Device>> = withContext(Dispatchers.IO) {
        if (token.isBlank()) return@withContext Resource.Error("Un token es requerido")
        val response = deviceService.getAllDevicesByOwnerId(ownerId, "Bearer $token")
        if (response.isSuccessful) {
            response.body()?.let { dtos ->
                return@withContext Resource.Success(dtos.map { it.toDevice() })
            }
            return@withContext Resource.Error("No se encontraron dispositivos para el propietario")
        }
        Resource.Error(response.message())
    }

    suspend fun createDevice(device: CreateDevice, token: String): Resource<Device> = withContext(Dispatchers.IO) {
        if (token.isBlank()) return@withContext Resource.Error("Un token es requerido")
        val request = CreateDevice(
            houseId = device.houseId,
            name = device.name,
            type = device.type,
            ipAddress = device.ipAddress,
            port = device.port,
            status = device.status,
            active = device.active,
            apiKey = device.apiKey
        )
        val response = deviceService.createDevice(request, "Bearer $token")
        if (response.isSuccessful) {
            response.body()?.let { dto -> return@withContext Resource.Success(dto.toDevice()) }
            return@withContext Resource.Error("Error al crear dispositivo")
        }
        Resource.Error(response.message())
    }

    suspend fun updateDevice(id: Long, device: UpdateDevice, token: String): Resource<Device> = withContext(Dispatchers.IO) {
        if (token.isBlank()) return@withContext Resource.Error("Un token es requerido")
        val request = UpdateDevice(
            name = device.name,
            type = device.type,
            ipAddress = device.ipAddress,
            port = device.port,
            status = device.status,
            active = device.active,
            apiKey = device.apiKey
        )
        val response = deviceService.updateDevice(id, request, "Bearer $token")
        if (response.isSuccessful) {
            response.body()?.let { dto -> return@withContext Resource.Success(dto.toDevice()) }
            return@withContext Resource.Error("Error al actualizar dispositivo")
        }
        Resource.Error(response.message())
    }

    suspend fun deleteDevice(id: Long, token: String): Resource<Unit> = withContext(Dispatchers.IO) {
        if (token.isBlank()) return@withContext Resource.Error("Un token es requerido")
        val response = deviceService.deleteDevice(id, "Bearer $token")
        if (response.isSuccessful) {
            return@withContext Resource.Success(Unit)
        }
        Resource.Error(response.message())
    }
}