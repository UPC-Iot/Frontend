package com.example.protectify.data.remote.authentication

import com.example.protectify.domain.authentication.AuthenticationRequest
import com.example.protectify.domain.authentication.AuthenticationResponse
import com.example.protectify.domain.authentication.SignUpRequest
import com.example.protectify.domain.authentication.SignUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationService {
    @POST("authentication/sign-up")
    suspend fun signUp(@Body request: SignUpRequest): Response<SignUpResponse>

    @POST("authentication/sign-in")
    suspend fun signIn(@Body request: AuthenticationRequest): Response<AuthenticationResponse>
}