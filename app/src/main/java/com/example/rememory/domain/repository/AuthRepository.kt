package com.example.rememory.domain.repository

import com.example.rememory.domain.model.AuthTokens

interface AuthRepository {
    suspend fun register(username: String, loginID: String, password: String)
    suspend fun login(id: String, password: String): AuthTokens
}