package com.example.rememory.data.repository

import com.example.rememory.data.remote.api.AuthService
import com.example.rememory.data.remote.dto.LoginRequestDto
import com.example.rememory.data.remote.dto.RegisterRequestDto
import com.example.rememory.domain.model.AuthTokens
import com.example.rememory.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val authService: AuthService
) : AuthRepository {

    override suspend fun register(username: String, loginID: String, password: String) {
        val request = RegisterRequestDto(username, loginID, password)
        val response = authService.register(request)

        if (!response.isSuccessful) {
            throw Exception("Registration failed (code: ${response.code()})")
        }
    }

    override suspend fun login(id: String, password: String): AuthTokens {
        val request = LoginRequestDto(id, password)
        val response = authService.login(request)
        return AuthTokens(response.accessToken, response.refreshToken)
    }
}