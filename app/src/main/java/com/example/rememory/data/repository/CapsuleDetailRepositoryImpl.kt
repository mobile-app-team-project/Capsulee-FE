package com.example.rememory.data.repository

import com.example.rememory.data.remote.api.CapsuleService
import com.example.rememory.data.remote.dto.ActionConditionRequestDto
import com.example.rememory.data.remote.dto.CapsuleDetailResponseDto
import com.example.rememory.data.remote.dto.LocationConditionRequestDto
import com.example.rememory.data.remote.dto.LocationConditionResponseDto
import com.example.rememory.data.remote.dto.ReadyRequestDto
import com.example.rememory.domain.model.*
import com.example.rememory.domain.repository.CapsuleDetailRepository

class CapsuleDetailRepositoryImpl(
    private val capsuleService: CapsuleService
) : CapsuleDetailRepository {

    override suspend fun getCapsuleDetail(capsuleId: Int): CapsuleDetailData {
        val responseDto = capsuleService.getCapsuleDetail(capsuleId)
        return responseDto.toDomainModel()
    }

    override suspend fun checkLocationCondition(
        request: LocationConditionRequestDto
    ): LocationConditionResponseDto {
        return capsuleService.checkLocationCondition(request.capsuleId, request)
    }

    override suspend fun checkActionCondition(
        request: ActionConditionRequestDto
    ): ActionConditionResult {
        val response = capsuleService.checkActionCondition(request.capsuleId, request)

        return ActionConditionResult(
            matched = response.actionCondition?.matched ?: true,
            isReadyAvailable = response.isReadyAvailable
        )
    }

    override suspend fun setReady(capsuleId: Int, ready: Boolean): Boolean {
        val request = ReadyRequestDto(ready)
        val response = capsuleService.setReady(capsuleId, request)
        return response.ready
    }

    override suspend fun checkLobbyStatus(capsuleId: Int): LobbyStatus {
        val response = capsuleService.checkLobbyStatus(capsuleId)

        return LobbyStatus(
            opened = response.opened,
            participants = response.participantsStatus.map { dto ->
                ParticipantStatus(
                    userId = dto.userId,
                    userName = dto.userName,
                    ready = dto.ready,
                    statusText = dto.statusText
                )
            }
        )
    }

    override suspend fun openCapsule(capsuleId: Int): CapsuleDetailData {
        return capsuleService.openCapsule(capsuleId).toDomainModel()
    }
}

private fun CapsuleDetailResponseDto.toDomainModel(): CapsuleDetailData {
    val status = when (this.status.uppercase()) {
        "LOCKED" -> CapsuleDetailStatus.LOCKED
        "WAITING" -> CapsuleDetailStatus.WAITING
        "READY" -> CapsuleDetailStatus.READY
        "OPENED" -> CapsuleDetailStatus.OPENED
        else -> CapsuleDetailStatus.LOCKED
    }

    val capsuleInfo = CapsuleDetailInfo(
        capsuleId = this.capsuleDetail.capsuleInfo.capsuleId,
        title = this.capsuleDetail.capsuleInfo.title,
        from = this.capsuleDetail.capsuleInfo.from,
        openTime = this.capsuleDetail.capsuleInfo.openTime,
        processPercent = this.capsuleDetail.capsuleInfo.processPercent,
        content = this.capsuleDetail.capsuleInfo.content
    )

    val participants = this.capsuleDetail.participants.map { dto ->
        CapsuleParticipant(
            userId = dto.userId,
            userName = dto.userName,
            isReady = dto.status?.uppercase() == "READY"
        )
    }

    val conditions = this.capsuleDetail.conditions?.map { dto ->
        CapsuleCondition(
            type = dto.type,
            value = dto.value,
            isUnlocked = dto.matched ?: false
        )
    } ?: emptyList()

    return CapsuleDetailData(
        status = status,
        capsuleInfo = capsuleInfo,
        participants = participants,
        conditions = conditions,
        readyCount = this.capsuleDetail.readyProgress?.readyCount ?: 0,
        totalCount = this.capsuleDetail.readyProgress?.totalCount ?: participants.size
    )
}