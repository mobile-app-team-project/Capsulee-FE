package com.example.rememory.data.repository

import android.R.attr.type
import com.example.rememory.data.remote.api.CapsuleService
import com.example.rememory.data.remote.dto.CapsuleItemDto
import com.example.rememory.data.remote.dto.CapsuleStatsDto
import com.example.rememory.data.remote.dto.ConditionInfoDto
import com.example.rememory.data.remote.mock.CapsuleMockData
import com.example.rememory.domain.model.CapsuleDomainModel
import com.example.rememory.domain.model.CapsuleStatsDomainModel
import com.example.rememory.domain.model.ConditionDomainModel
import com.example.rememory.domain.model.ConditionType
import com.example.rememory.domain.repository.CapsuleListDomain
import com.example.rememory.domain.repository.CapsuleRepository
import kotlinx.coroutines.delay

/**
 * Domain Repository의 구현체, 서버 통신 담당
 */
class CapsuleRepositoryImpl(
    private val apiService: CapsuleService // Retrofit 서비스 주입
) : CapsuleRepository {

    override suspend fun getCapsuleList(isSent: Boolean): CapsuleListDomain {

        // 1. API 호출 (DTO 반환)
        val type = if (isSent) "sent" else "received"
        val responseDto = apiService.getCapsules(type)

 /*  //실제 api 호출시
        // 2. DTO를 Domain Model로 변환 (Mapping)
        val domainCapsules = responseDto.capsules.map { it.toDomainModel() }
        val domainStats = responseDto.stats.toDomainModel()
*/


        // 네트워크 지연 시뮬레이션: UI에서 로딩 상태를 확인할 수 있도록 잠시 기다립니다.
        delay(800)
/*
        // 1. Mock 데이터 호출
        val responseDto = if (isSent) {
            CapsuleMockData.getSentCapsulesDto()
        } else {
            CapsuleMockData.getReceivedCapsulesDto()
        }
 */

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
        title = this.title,
        relationText = this.fromOrTo,
        isOpened = this.opened,
        conditionSummary = this.conditionSummaries.map { it.toDomainModel() }
    )
}

private fun ConditionInfoDto.toDomainModel(): ConditionDomainModel {
    return ConditionDomainModel(
        type = when (this.type.uppercase()) {
            "LOCATION" -> ConditionType.LOCATION
            "TIME" -> ConditionType.TIME
            else -> ConditionType.ACTION
        },
        value = this.value
    )
}

private fun CapsuleStatsDto.toDomainModel(): CapsuleStatsDomainModel {
    return CapsuleStatsDomainModel(
        total = this.total,
        canOpen = this.canOpen,
        locked = this.locked
    )
}