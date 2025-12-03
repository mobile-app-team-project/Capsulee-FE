package com.example.rememory.data.remote.dto

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