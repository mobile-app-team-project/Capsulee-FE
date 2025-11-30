package com.example.rememory.data.remote.mock

import com.example.rememory.data.remote.dto.* // DTO 클래스를 사용합니다.

/**
 * 캡슐 목록에 대한 Mock 데이터 제공 클래스
 * 실제 서버 응답(DTO) 형태를 모방합니다.
 */
object CapsuleMockData {

    // --- 캡슐 개봉 조건 요약 ---
    private val mockConditionTime = ConditionInfoDto(type = "TIME", value = "2025-12-25, 09:00 AM")
    private val mockConditionRecipients = ConditionInfoDto(type = "RECIPIENTS", value = "Suginnn, Bonnie, Nicolas")
    private val mockConditionGeo = ConditionInfoDto(type = "GEO", value = "Chung-And Univ. Main Gate")

    // --- Sent (보낸 캡슐) 목록 ---
    private val sentCapsule1 = CapsuleItemDto(
        capsuleId = 1,
        title = "2024년 졸업 축하 캡슐",
        fromOrTo = "To Jisoo",
        opened = true,
        conditionSummaries = listOf(mockConditionTime, mockConditionRecipients, mockConditionGeo)
    )
    private val sentCapsule2 = CapsuleItemDto(
        capsuleId = 3,
        title = "내년의 나에게 쓰는 편지",
        fromOrTo = "To Me",
        opened = false,
        conditionSummaries = listOf(mockConditionTime)
    )

    // --- Received (받은 캡슐) 목록 ---
    private val receivedCapsule1 = CapsuleItemDto(
        capsuleId = 2,
        title = "지수에게 받은 생일 선물 기록",
        fromOrTo = "From Jisoo",
        opened = false,
        conditionSummaries = listOf(mockConditionGeo, mockConditionRecipients)
    )
    private val receivedCapsule2 = CapsuleItemDto(
        capsuleId = 4,
        title = "추억의 장소",
        fromOrTo = "From Minho",
        opened = true,
        conditionSummaries = listOf(mockConditionTime, mockConditionGeo)
    )

    // ----------------------------------------------------
    // 최종 Mock DTO Response 제공 함수
    // ----------------------------------------------------

    val mockStats = CapsuleStatsDto(total = 12, canOpen = 3, locked = 8)

    /**
     * 보낸 캡슐 목록 DTO를 반환합니다.
     */
    fun getSentCapsulesDto(): CapsuleListResponseDto {
        return CapsuleListResponseDto(
            stats = mockStats,
            capsules = listOf(sentCapsule1, sentCapsule2)
        )
    }

    /**
     * 받은 캡슐 목록 DTO를 반환합니다.
     */
    fun getReceivedCapsulesDto(): CapsuleListResponseDto {
        return CapsuleListResponseDto(
            stats = mockStats,
            capsules = listOf(receivedCapsule1, receivedCapsule2)
        )
    }
}