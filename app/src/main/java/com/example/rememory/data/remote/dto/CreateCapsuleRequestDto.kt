package com.example.rememory.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * 캡슐 생성 API 요청 바디 (Request Body)
 */
@Serializable
data class CreateCapsuleRequestDto(
    val title: String,
    val message: String,
    val openDate: String, // 서버가 요구하는 날짜 형식 (예: "2025-12-25T10:00:00")
    // ... 위치, 날씨 등 다른 조건들에 대한 필드 추가
    // val latitude: Double?,
    // val longitude: Double?,
    // val weather: String?,
    val recipientIds: List<String>? // 친구 id 목록 등
)

// 참고: 서버 응답을 위한 DTO 예시
/**
 * 캡슐 생성 API 응답 바디 (Response Body)
 */
@Serializable
data class CreateCapsuleResponseDto(
    val capsuleId: Int,
    val title: String,
    val message: String,
    val createdAt: String
)
