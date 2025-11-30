package com.example.rememory.data.remote.dto

// 라이브러리 (예: Gson, Moshi, kotlinx.serialization)를 사용해 JSON을 자동 변환하기 위해 필요합니다.
// 여기서는 kotlinx.serialization을 가정합니다.
import kotlinx.serialization.Serializable

/**
 * 캡슐 목록 API의 최종 응답 DTO
 */
@Serializable
data class CapsuleListResponseDto(
    val stats: CapsuleStatsDto,
    val capsules: List<CapsuleItemDto>
)

@Serializable
data class CapsuleStatsDto(
    val total: Int,
    val canOpen: Int,
    val locked: Int
)

@Serializable
data class CapsuleItemDto(
    val capsuleId: Int,
    val title: String,
    val fromOrTo: String,
    val opened: Boolean,
    val conditionSummaries: List<ConditionInfoDto>,
)

@Serializable
data class ConditionInfoDto (
    val type: String,
    val value: String
)