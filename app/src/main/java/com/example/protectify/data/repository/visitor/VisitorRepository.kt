package com.example.protectify.data.repository.visitor

import com.example.protectify.common.Resource
import com.example.protectify.data.remote.visitor.VisitorService
import com.example.protectify.data.remote.visitor.toVisitor
import com.example.protectify.domain.visitor.CreateVisitor
import com.example.protectify.domain.visitor.UpdateVisitor
import com.example.protectify.domain.visitor.Visitor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VisitorRepository(
    private val visitorService: VisitorService
) {

    suspend fun getAllVisitors(token: String): Resource<List<Visitor>> = withContext(Dispatchers.IO) {
        if (token.isBlank()) return@withContext Resource.Error("Un token es requerido")
        val response = visitorService.getAllVisitors("Bearer $token")
        if (response.isSuccessful) {
            response.body()?.let { dtos ->
                return@withContext Resource.Success(dtos.map { it.toVisitor() })
            }
            return@withContext Resource.Error("No se encontraron visitantes")
        }
        Resource.Error(response.message())
    }

    suspend fun getVisitorById(id: Long, token: String): Resource<Visitor> = withContext(Dispatchers.IO) {
        if (token.isBlank()) return@withContext Resource.Error("Un token es requerido")
        val response = visitorService.getVisitorById(id, "Bearer $token")
        if (response.isSuccessful) {
            response.body()?.let { dto -> return@withContext Resource.Success(dto.toVisitor()) }
            return@withContext Resource.Error("No se encontró el visitante")
        }
        Resource.Error(response.message())
    }

    suspend fun getAllVisitorsByHouseId(houseId: Long, token: String): Resource<List<Visitor>> = withContext(Dispatchers.IO) {
        if (token.isBlank()) return@withContext Resource.Error("Un token es requerido")
        val response = visitorService.getAllVisitorsByHouseId(houseId, "Bearer $token")
        if (response.isSuccessful) {
            response.body()?.let { dtos ->
                return@withContext Resource.Success(dtos.map { it.toVisitor() })
            }
            return@withContext Resource.Error("No se encontraron visitantes para la casa")
        }
        Resource.Error(response.message())
    }

    suspend fun getAllVisitorsByOwnerId(ownerId: Long, token: String): Resource<List<Visitor>> = withContext(Dispatchers.IO) {
        if (token.isBlank()) return@withContext Resource.Error("Un token es requerido")
        val response = visitorService.getAllVisitorsByOwnerId(ownerId, "Bearer $token")
        if (response.isSuccessful) {
            response.body()?.let { dtos ->
                return@withContext Resource.Success(dtos.map { it.toVisitor() })
            }
            return@withContext Resource.Error("No se encontraron visitantes para el propietario")
        }
        Resource.Error(response.message())
    }

    suspend fun createVisitor(visitor: CreateVisitor, token: String): Resource<Visitor> = withContext(Dispatchers.IO) {
        if (token.isBlank()) return@withContext Resource.Error("Un token es requerido")
        val request = CreateVisitor(
            houseId = visitor.houseId,
            firstname = visitor.firstname,
            lastname = visitor.lastname,
            photo = visitor.photo,
            role = visitor.role,
            lastVisit = visitor.lastVisit
        )
        val response = visitorService.createVisitor(request, "Bearer $token")
        if (response.isSuccessful) {
            response.body()?.let { dto -> return@withContext Resource.Success(dto.toVisitor()) }
            return@withContext Resource.Error("Error al crear visitante")
        }
        Resource.Error(response.message())
    }

    suspend fun updateVisitor(id: Long, visitor: UpdateVisitor, token: String): Resource<Visitor> = withContext(Dispatchers.IO) {
        if (token.isBlank()) return@withContext Resource.Error("Un token es requerido")
        val request = UpdateVisitor(
            id = visitor.id,
            firstname = visitor.firstname,
            lastname = visitor.lastname,
            photo = visitor.photo,
            role = visitor.role,
            lastVisit = visitor.lastVisit
        )
        val response = visitorService.updateVisitor(id, request, "Bearer $token")
        if (response.isSuccessful) {
            response.body()?.let { dto -> return@withContext Resource.Success(dto.toVisitor()) }
            return@withContext Resource.Error("Error al actualizar visitante")
        }
        Resource.Error(response.message())
    }

    suspend fun deleteVisitor(id: Long, token: String): Resource<Unit> = withContext(Dispatchers.IO) {
        if (token.isBlank()) return@withContext Resource.Error("Un token es requerido")
        val response = visitorService.deleteVisitor(id, "Bearer $token")
        if (response.isSuccessful) {
            return@withContext Resource.Success(Unit)
        }
        Resource.Error(response.message())
    }
}