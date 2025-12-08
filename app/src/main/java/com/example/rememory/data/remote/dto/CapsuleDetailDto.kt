package com.example.rememory.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * 캡슐 상세 조회 API 응답 DTO
 */
@Serializable
data class CapsuleDetailResponseDto(
    val status: String,  // "LOCKED", "WAITING", "READY", "OPENED"
    val capsuleDetail: CapsuleDetailWrapperDto
)

@Serializable
data class CapsuleDetailWrapperDto(
    val capsuleInfo: CapsuleDetailInfoDto,
    val participants: List<ParticipantDto>,
    val conditions: List<ConditionDto>? = null,
    val readyProgress: ProgressDto? = null
)

@Serializable
data class CapsuleDetailInfoDto(
    val capsuleId: Int,
    val title: String,
    val from: String,
    val openTime: String,
    val processPercent: Int? = null,
    val content: String? = null
)

@Serializable
data class ParticipantDto(
    val userId: Int,
    val userName: String,
    val status: String? = null
)

@Serializable
data class ConditionDto(
    val type: String,
    val value: String,
    val matched: Boolean? = null
)

@Serializable
data class ProgressDto(
    val readyCount: Int,
    val totalCount: Int
)

@Serializable
data class LocationConditionRequestDto(
    val capsuleId: Int,
    val latitude: Double,
    val longitude: Double
)

@Serializable
data class LocationConditionResponseDto(
    val locationCondition: LocationConditionDto? = null,
    val weatherCondition: WeatherConditionDto? = null,
    val isReadyAvailable: Boolean
)

@Serializable
data class LocationConditionDto(
    val matched: Boolean,
    val distance: Double,
    val userLat: Double,
    val userLon: Double,
    val conditionValue: String
)

@Serializable
data class WeatherConditionDto(
    val matched: Boolean,
    val currentWeather: String,
    val requiredWeather: String
)

// 행동 조건 체크 요청
@Serializable
data class ActionConditionRequestDto(
    val capsuleId: Int,
    val matched: Boolean = true
)

// 행동 조건 체크 응답
@Serializable
data class ActionConditionResponseDto(
    val actionCondition: ActionConditionDetailDto? = null,
    val isReadyAvailable: Boolean
)

@Serializable
data class ActionConditionDetailDto(
    val matched: Boolean,
    val actionType: String,
    val requiredAction: String
)

// Ready 설정 요청
@Serializable
data class ReadyRequestDto(
    val ready: Boolean
)

// Ready 설정 응답
@Serializable
data class ReadyResponseDto(
    val ready: Boolean
)

// 로비 상태 조회 응답
@Serializable
data class LobbyStatusResponseDto(
    val opened: Boolean,
    val participantsStatus: List<ParticipantStatusDto>
)

@Serializable
data class ParticipantStatusDto(
    val userId: Int,
    val userName: String,
    val ready: Boolean,
    val statusText: String
)