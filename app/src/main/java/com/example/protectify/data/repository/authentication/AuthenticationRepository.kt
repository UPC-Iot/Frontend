package com.example.protectify.data.repository.authentication

import com.example.protectify.common.Resource
import com.example.protectify.data.local.UserDao
import com.example.protectify.data.local.UserEntity
import com.example.protectify.data.remote.authentication.AuthenticationService
import com.example.protectify.domain.authentication.AuthenticationRequest
import com.example.protectify.domain.authentication.AuthenticationResponse
import com.example.protectify.domain.authentication.SignUpRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthenticationRepository(
    private val authenticationService: AuthenticationService,
    private val userDao: UserDao
) {

    suspend fun signUp(username: String, password: String, roles: List<String>) = withContext(Dispatchers.IO) {
        val response = authenticationService.signUp(SignUpRequest(username, password, roles))
        if (response.isSuccessful) {
            response.body()?.let { signUpResponse ->
                return@withContext Resource.Success(signUpResponse)
            }
            return@withContext Resource.Error(message = "Error al registrar usuario")
        }
        return@withContext Resource.Error(message = response.message().ifEmpty { "Error al registrar usuario" })
    }

    suspend fun signIn(username: String, password: String) = withContext(Dispatchers.IO) {
        val response = authenticationService.signIn(AuthenticationRequest(username, password))
        if (response.isSuccessful) {
            response.body()?.let { loginResponse ->
                return@withContext Resource.Success(loginResponse)
            }
            return@withContext Resource.Error(message = "Error al iniciar sesión")
        }
        return@withContext Resource.Error(message = response.message().ifEmpty { "Usuario o contraseña incorrecto" })
    }

    suspend fun insertUser(auth: AuthenticationResponse) = withContext(Dispatchers.IO) {
        userDao.insert(UserEntity(auth.id, auth.token))
    }

    suspend fun deleteUser(auth: AuthenticationResponse) = withContext(Dispatchers.IO) {
        userDao.delete(UserEntity(auth.id, auth.token))
    }

    suspend fun getUser(): Resource<UserEntity> = withContext(Dispatchers.IO) {
        val users = userDao.getAll()
        if (users.isNotEmpty()) {
            return@withContext Resource.Success(users[0])
        }
        return@withContext Resource.Error(message = "No se encontró usuario")
    }
}