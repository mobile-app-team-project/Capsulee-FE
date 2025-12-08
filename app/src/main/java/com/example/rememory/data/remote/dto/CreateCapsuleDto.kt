package com.example.rememory.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateCapsuleRequest(
    val title: String,
    val content: String,
    val openTime: String,
    val recipientIds: List<Int>,
    val conditions: List<ConditionDto>
)

@Serializable
data class ConditionDto(
    val type: String,
    val value: String
)

@Serializable
data class CreateCapsuleResponse(
    val id: Int,
    val title: String,
    val content: String,
    val imageUrl: String?,
    val openTime: String,
    val recipientIds: List<Int>,
    val conditions: List<ConditionDto>
)