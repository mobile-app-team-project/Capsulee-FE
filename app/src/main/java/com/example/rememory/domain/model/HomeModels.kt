package com.example.rememory.domain.model

data class HomeScreenData(
    val status: CapsuleStatus,
    val capsuleInfo: HomeCapsuleInfo,
    val participants: List<ParticipantUser>,
    val conditions: List<HomeCondition>
)

enum class CapsuleStatus {
    LOCKED,
    UNLOCKED
}

data class HomeCapsuleInfo(
    val capsuleId: Int,
    val title: String,
    val from: String,
    val openTime: String, // "2025-12-25 at 09:00" 형식
    val processPercent: Int
)

data class ParticipantUser(
    val userId: Int,
    val userName: String
)

data class HomeCondition(
    val type: String, // "LOCATION", "TIME", "ACTION"
    val value: String
)