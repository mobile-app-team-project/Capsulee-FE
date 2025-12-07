package com.example.rememory.data.remote.api

import com.example.rememory.data.remote.dto.AuthResponseDto
import com.example.rememory.data.remote.dto.LoginRequestDto
import com.example.rememory.data.remote.dto.RegisterRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/auth/register")
    suspend fun register(@Body request: RegisterRequestDto): Response<Unit>

    @POST("/auth/login")
    suspend fun login(@Body request: LoginRequestDto): AuthResponseDto
}