package com.example.rememory.domain.model

import android.R

/**
 * 캡슐 목록 전체 응답 모델 (Domain)
 */
data class CapsuleListDomain(
    val stats: CapsuleStatsDomainModel,
    val capsules: List<CapsuleDomainModel>
)

/**
 * 캡슐 목록 아이템의 핵심 도메인 모델
 */
data class CapsuleDomainModel(
    val id: Int,
    val title: String,
    val relationText: String, // fromOrTo 텍스트
    val isOpened: Boolean,
    val conditionSummary: List<ConditionDomainModel>,
)

/**
 * 캡슐 개봉 조건 요약 도메인 모델
 */
data class ConditionDomainModel(
    val type: ConditionType,
    val value: String
)

/**
 * 캡슐 상태 요약 도메인 모델
 */
data class CapsuleStatsDomainModel(
    val total: Int,
    val canOpen: Int,
    val locked: Int
)

enum class ConditionType {
    LOCATION,
    WEATHER,
    ACTION, // 서버에서 정의되지 않은 타입 처리
    TIME,
}

enum class WeatherCondition {
    CLEAR, RAINY, SNOWY, CLOUD
}

enum class ActionCondition(val label: String) {
    SHAKE("Shake 3 times"),
    SOUND("Sound"),
    TAP("Tap 3 times"),
    COMPASS("Point to Your\n“North”")
}

data class Recipient(
    val id: Int,            // userId
    val username: String,   // 실제 이름
    val loginId: String,    // 로그인 ID
    val selected: Boolean = false
)