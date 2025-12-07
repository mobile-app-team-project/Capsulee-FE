package com.example.rememory.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequestDto(
    val username: String,
    val loginID: String,
    val password: String
)

@Serializable
data class LoginRequestDto(
    val loginID: String,
    val password: String
)

@Serializable
data class AuthResponseDto(
    val accessToken: String,
    val refreshToken: String
)