package com.example.rememory.domain.model

/**
 * 캡슐 상세 화면의 상태
 */
enum class CapsuleDetailStatus {
    LOCKED,    // 오픈 날짜 이전
    WAITING,   // 당일이지만 조건 미충족 또는 Ready 대기
    READY,     // 사용자가 Ready 누름
    OPENED     // 캡슐 열림
}

/**
 * 캡슐 상세 정보
 */
data class CapsuleDetailInfo(
    val capsuleId: Int,
    val title: String,
    val from: String,
    val openTime: String,
    val processPercent: Int? = null,  // LOCKED 상태일 때만
    val content: String? = null,      // OPENED 상태일 때만
    val imageUrl: String? = null      // OPENED 상태일 때만
)

/**
 * 참여자 정보
 */
data class CapsuleParticipant(
    val userId: Int,
    val userName: String,
    val isReady: Boolean = false  // WAITING/READY 상태에서 사용
)

/**
 * 조건 정보
 */
data class CapsuleCondition(
    val type: String,  // "LOCATION", "WEATHER", "ACTION", "TIME"
    val value: String,
    val isUnlocked: Boolean = false  // WAITING 상태에서 조건 충족 여부
)

/**
 * 캡슐 상세 화면 전체 데이터
 */
data class CapsuleDetailData(
    val status: CapsuleDetailStatus,
    val capsuleInfo: CapsuleDetailInfo,
    val participants: List<CapsuleParticipant>,
    val conditions: List<CapsuleCondition> = emptyList(),
    val readyCount: Int = 0,
    val totalCount: Int = 0
)

data class LocationConditionResult(
    val locationMatched: Boolean,
    val weatherMatched: Boolean,
    val distance: Double? = null,
    val currentWeather: String? = null,
    val requiredWeather: String? = null,
    val isReadyAvailable: Boolean
)

data class ActionConditionResult(
    val matched: Boolean,
    val isReadyAvailable: Boolean
)

data class LobbyStatus(
    val opened: Boolean,
    val participants: List<ParticipantStatus>
)

data class ParticipantStatus(
    val userId: Int,
    val userName: String,
    val ready: Boolean,
    val statusText: String
)