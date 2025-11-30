package com.example.rememory.domain.repository

import com.example.rememory.domain.model.CapsuleDomainModel
import com.example.rememory.domain.model.CapsuleStatsDomainModel

/**
 * 캡슐 관련 데이터 접근 인터페이스 (도메인 규칙 정의)
 */
interface CapsuleRepository {

    /**
     * 캡슐 목록을 가져옵니다.
     * @param isSent true면 보낸 캡슐(sent), false면 받은 캡슐(received)을 요청합니다.
     */
    suspend fun getCapsuleList(isSent: Boolean): CapsuleListDomain
}

/**
 * 도메인 계층에서 사용할 캡슐 목록 전체 응답 모델
 */
data class CapsuleListDomain(
    val stats: CapsuleStatsDomainModel,
    val capsules: List<CapsuleDomainModel>
)