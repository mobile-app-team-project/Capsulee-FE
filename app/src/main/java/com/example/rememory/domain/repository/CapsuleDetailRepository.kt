package com.example.rememory.domain.repository

import com.example.rememory.data.remote.dto.ActionConditionRequestDto
import com.example.rememory.data.remote.dto.LocationConditionRequestDto
import com.example.rememory.data.remote.dto.LocationConditionResponseDto
import com.example.rememory.domain.model.ActionConditionResult
import com.example.rememory.domain.model.CapsuleDetailData
import com.example.rememory.domain.model.LobbyStatus
import com.example.rememory.domain.model.LocationConditionResult

interface CapsuleDetailRepository {
    /**
     * 캡슐 상세 정보를 가져옵니다.
     * @param capsuleId 캡슐 ID
     */
    suspend fun getCapsuleDetail(capsuleId: Int): CapsuleDetailData

    /**
     * Ready 버튼을 눌렀을 때 호출합니다.
     * @param capsuleId 캡슐 ID
     * @return 업데이트된 캡슐 상세 정보
     */

    suspend fun checkLocationCondition(request: LocationConditionRequestDto): LocationConditionResponseDto
    suspend fun checkActionCondition(request: ActionConditionRequestDto): ActionConditionResult

    suspend fun setReady(capsuleId: Int, ready: Boolean): Boolean

    suspend fun checkLobbyStatus(capsuleId: Int): LobbyStatus

    suspend fun openCapsule(capsuleId: Int): CapsuleDetailData
}