package com.example.protectify.data.repository.alert

import com.example.protectify.common.Resource
import com.example.protectify.data.remote.alert.AlertService
import com.example.protectify.data.remote.alert.toAlert
import com.example.protectify.domain.alert.Alert
import com.example.protectify.domain.alert.CreateAlert
import com.example.protectify.domain.alert.UpdateAlert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AlertRepository(
    private val alertService: AlertService
) {

    suspend fun getAllAlerts(token: String): Resource<List<Alert>> = withContext(Dispatchers.IO) {
        if (token.isBlank()) return@withContext Resource.Error("Un token es requerido")
        val response = alertService.getAllAlerts("Bearer $token")
        if (response.isSuccessful) {
            response.body()?.let { dtos ->
                return@withContext Resource.Success(dtos.map { it.toAlert() })
            }
            return@withContext Resource.Error("No se encontraron alertas")
        }
        Resource.Error(response.message())
    }

    suspend fun getAlertById(id: Long, token: String): Resource<Alert> = withContext(Dispatchers.IO) {
        if (token.isBlank()) return@withContext Resource.Error("Un token es requerido")
        val response = alertService.getAlertById(id, "Bearer $token")
        if (response.isSuccessful) {
            response.body()?.let { dto -> return@withContext Resource.Success(dto.toAlert()) }
            return@withContext Resource.Error("No se encontró la alerta")
        }
        Resource.Error(response.message())
    }

    suspend fun getAllAlertsByHouseId(houseId: Long, token: String): Resource<List<Alert>> = withContext(Dispatchers.IO) {
        if (token.isBlank()) return@withContext Resource.Error("Un token es requerido")
        val response = alertService.getAllAlertsByHouseId(houseId, "Bearer $token")
        if (response.isSuccessful) {
            response.body()?.let { dtos ->
                return@withContext Resource.Success(dtos.map { it.toAlert() })
            }
            return@withContext Resource.Error("No se encontraron alertas para la casa")
        }
        Resource.Error(response.message())
    }

    suspend fun getAllAlertsByOwnerId(ownerId: Long, token: String): Resource<List<Alert>> = withContext(Dispatchers.IO) {
        if (token.isBlank()) return@withContext Resource.Error("Un token es requerido")
        val response = alertService.getAllAlertsByOwnerId(ownerId, "Bearer $token")
        if (response.isSuccessful) {
            response.body()?.let { dtos ->
                return@withContext Resource.Success(dtos.map { it.toAlert() })
            }
            return@withContext Resource.Error("No se encontraron alertas para el propietario")
        }
        Resource.Error(response.message())
    }

    suspend fun createAlert(alert: CreateAlert, token: String): Resource<Alert> = withContext(Dispatchers.IO) {
        if (token.isBlank()) return@withContext Resource.Error("Un token es requerido")
        val request = CreateAlert(
            houseId = alert.houseId,
            type = alert.type,
            message = alert.message,
            status = alert.status,
            timestamp = alert.timestamp,
            imageUrl = alert.imageUrl
        )
        val response = alertService.createAlert(request, "Bearer $token")
        if (response.isSuccessful) {
            response.body()?.let { dto -> return@withContext Resource.Success(dto.toAlert()) }
            return@withContext Resource.Error("Error al crear alerta")
        }
        Resource.Error(response.message())
    }

    suspend fun updateAlert(id: Long, alert: UpdateAlert, token: String): Resource<Alert> = withContext(Dispatchers.IO) {
        if (token.isBlank()) return@withContext Resource.Error("Un token es requerido")
        val request = UpdateAlert(
            type = alert.type,
            message = alert.message,
            status = alert.status,
            timestamp = alert.timestamp,
            imageUrl = alert.imageUrl
        )
        val response = alertService.updateAlert(id, request, "Bearer $token")
        if (response.isSuccessful) {
            response.body()?.let { dto -> return@withContext Resource.Success(dto.toAlert()) }
            return@withContext Resource.Error("Error al actualizar alerta")
        }
        Resource.Error(response.message())
    }

    suspend fun deleteAlert(id: Long, token: String): Resource<Unit> = withContext(Dispatchers.IO) {
        if (token.isBlank()) return@withContext Resource.Error("Un token es requerido")
        val response = alertService.deleteAlert(id, "Bearer $token")
        if (response.isSuccessful) {
            return@withContext Resource.Success(Unit)
        }
        Resource.Error(response.message())
    }
}