package com.example.rememory.data.repository

import com.example.rememory.data.remote.api.CapsuleService
import com.example.rememory.data.remote.dto.CapsuleItemDto
import com.example.rememory.data.remote.dto.CapsuleStatsDto
import com.example.rememory.data.remote.dto.ConditionInfoDto
import com.example.rememory.domain.model.CapsuleDomainModel
import com.example.rememory.domain.model.CapsuleStatsDomainModel
import com.example.rememory.domain.model.ConditionDomainModel
import com.example.rememory.domain.model.ConditionType
import com.example.rememory.domain.repository.CapsuleListDomain
import com.example.rememory.domain.repository.CapsuleRepository
import javax.inject.Inject

/**
 * Domain Repository의 구현체, 서버 통신 담당
 */
class CapsuleRepositoryImpl @Inject constructor(
    private val capsuleService: CapsuleService
) : CapsuleRepository {

    override suspend fun getCapsuleList(isSent: Boolean): CapsuleListDomain {

        // 1. API 호출 (DTO 반환)
        val type = if (isSent) "sent" else "received"
        val responseDto = capsuleService.getCapsules(type)

        // 2. DTO를 Domain Model로 변환
        val domainCapsules = responseDto.capsules.map { it.toDomainModel() }
        val domainStats = responseDto.stats.toDomainModel()

        return CapsuleListDomain(
            stats = domainStats,
            capsules = domainCapsules
        )
    }
}

// -------------------------------------------------------------------------
// 매핑(Mapping) 함수 - DTO를 Domain Model로 변환
// -------------------------------------------------------------------------

private fun CapsuleItemDto.toDomainModel(): CapsuleDomainModel {
    return CapsuleDomainModel(
        id = this.capsuleId,
        title = this.title ?: "no title",
        relationText = this.fromOrTo ?: "",
        isOpened = this.opened,
        conditionSummary = this.conditionSummaries?.map { it.toDomainModel() } ?: emptyList()
    )
}

private fun ConditionInfoDto.toDomainModel(): ConditionDomainModel {
    val actualType = this.type?.uppercase() ?: "ACTION"
    val actualValue = this.value ?: ""

    return ConditionDomainModel(
        // 하나의 when 문으로 모든 조건을 처리
        type = when (actualType) {
            "LOCATION" -> ConditionType.LOCATION
            "TIME" -> ConditionType.TIME
            "WEATHER" -> ConditionType.WEATHER
            else -> ConditionType.ACTION // 그 외의 모든 경우는 ACTION으로 처리
        },
        value = actualValue
    )
}

private fun CapsuleStatsDto.toDomainModel(): CapsuleStatsDomainModel {
    return CapsuleStatsDomainModel(
        total = this.total,
        canOpen = this.canOpen,
        locked = this.locked
    )
}