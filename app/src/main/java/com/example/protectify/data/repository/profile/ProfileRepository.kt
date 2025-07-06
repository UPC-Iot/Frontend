package com.example.protectify.data.repository.profile

import com.example.protectify.common.Resource
import com.example.protectify.data.remote.profile.ProfileService
import com.example.protectify.data.remote.profile.toProfile
import com.example.protectify.domain.profile.CreateProfile
import com.example.protectify.domain.profile.Profile
import com.example.protectify.domain.profile.UpdateProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfileRepository(
    private val profileService: ProfileService
) {

    suspend fun getProfileById(id: Long, token: String): Resource<Profile> = withContext(Dispatchers.IO) {
        if (token.isBlank()) return@withContext Resource.Error("Un token es requerido")
        val response = profileService.getProfileById(id, "Bearer $token")
        if (response.isSuccessful) {
            response.body()?.let { dto -> return@withContext Resource.Success(dto.toProfile()) }
            return@withContext Resource.Error("No se encontró el perfil")
        }
        Resource.Error(response.message())
    }

    suspend fun getProfileByUserId(userId: Long, token: String): Resource<Profile> = withContext(Dispatchers.IO) {
        if (token.isBlank()) return@withContext Resource.Error("Un token es requerido")
        val response = profileService.getProfileByUserId(userId, "Bearer $token")
        if (response.isSuccessful) {
            response.body()?.let { dto -> return@withContext Resource.Success(dto.toProfile()) }
            return@withContext Resource.Error("No se encontró el perfil")
        }
        Resource.Error(response.message())
    }

    suspend fun createProfile(profile: CreateProfile, token: String): Resource<Profile> = withContext(Dispatchers.IO) {
        if (token.isBlank()) return@withContext Resource.Error("Un token es requerido")
        val request = CreateProfile(
            userId = profile.userId,
            firstName = profile.firstName,
            lastName = profile.lastName,
            birthDate = profile.birthDate,
            description = profile.description,
            photo = profile.photo,
            phone = profile.phone,
            address = profile.address
        )
        val response = profileService.createProfile(request, "Bearer $token")
        if (response.isSuccessful) {
            response.body()?.let { dto -> return@withContext Resource.Success(dto.toProfile()) }
            return@withContext Resource.Error("Error al crear perfil")
        }
        Resource.Error(response.message())
    }

    suspend fun updateProfile(id: Long, profile: UpdateProfile, token: String): Resource<Profile> = withContext(Dispatchers.IO) {
        if (token.isBlank()) return@withContext Resource.Error("Un token es requerido")
        val request = UpdateProfile(
            firstName = profile.firstName,
            lastName = profile.lastName,
            birthDate = profile.birthDate,
            description = profile.description,
            photo = profile.photo,
            phone = profile.phone,
            address = profile.address
        )
        val response = profileService.updateProfile(id, request, "Bearer $token")
        if (response.isSuccessful) {
            response.body()?.let { dto -> return@withContext Resource.Success(dto.toProfile()) }
            return@withContext Resource.Error("Error al actualizar perfil")
        }
        Resource.Error(response.message())
    }

    suspend fun deleteProfile(id: Long, token: String): Resource<Unit> = withContext(Dispatchers.IO) {
        if (token.isBlank()) return@withContext Resource.Error("Un token es requerido")
        val response = profileService.deleteProfile(id, "Bearer $token")
        if (response.isSuccessful) {
            return@withContext Resource.Success(Unit)
        }
        Resource.Error(response.message())
    }
}