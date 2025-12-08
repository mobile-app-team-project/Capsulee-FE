package com.example.rememory.data.repository

import com.example.rememory.data.remote.api.CapsuleService
import com.example.rememory.data.remote.dto.ActionConditionRequestDto
import com.example.rememory.data.remote.dto.CapsuleDetailResponseDto
import com.example.rememory.data.remote.dto.LocationConditionRequestDto
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
        capsuleId: Int,
        latitude: Double,
        longitude: Double
    ): LocationConditionResult {
        val request = LocationConditionRequestDto(latitude, longitude)
        val response = capsuleService.checkLocationCondition(capsuleId, request)

        return LocationConditionResult(
            locationMatched = response.locationCondition?.matched ?: false,
            weatherMatched = response.weatherCondition?.matched ?: false,
            distance = response.locationCondition?.distance,
            currentWeather = response.weatherCondition?.currentWeather,
            requiredWeather = response.weatherCondition?.requiredWeather,
            isReadyAvailable = response.isReadyAvailable
        )
    }

    override suspend fun checkActionCondition(
        capsuleId: Int,
        actionType: String
    ): ActionConditionResult {
        val request = ActionConditionRequestDto(actionType)
        val response = capsuleService.checkActionCondition(capsuleId, request)

        return ActionConditionResult(
            matched = response.actionCondition.matched,
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

//    override suspend fun markAsReady(capsuleId: Int): CapsuleDetailData {
//        val responseDto = capsuleService.markAsReady(capsuleId)
//        return responseDto.toDomainModel()
//    }
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
        capsuleId = this.capsuleInfo.capsuleId,
        title = this.capsuleInfo.title,
        from = this.capsuleInfo.from,
        openTime = this.capsuleInfo.openTime,
        processPercent = this.capsuleInfo.processPercent,
        content = this.capsuleInfo.content
    )

    val participants = this.participants.map { dto ->
        CapsuleParticipant(
            userId = dto.user_id,
            userName = dto.userName,
            isReady = dto.status == "Ready" // ✅ status 문자열로 판단
        )
    }

    val conditions = this.conditions?.map { dto ->
        CapsuleCondition(
            type = dto.type,
            value = dto.value,
            isUnlocked = dto.isUnlocked ?: false
        )
    } ?: emptyList()

    return CapsuleDetailData(
        status = status,
        capsuleInfo = capsuleInfo,
        participants = participants,
        conditions = conditions,
        readyCount = this.progress?.readyCount ?: 0, // ✅ 추가
        totalCount = this.progress?.totalCount ?: participants.size // ✅ 추가
    )
}

// DTO를 Domain Model로 변환하는 확장 함수
//private fun CapsuleDetailResponseDto.toDomainModel(): CapsuleDetailData {
//    val status = when (this.status.uppercase()) {
//        "LOCKED" -> CapsuleDetailStatus.LOCKED
//        "WAITING" -> CapsuleDetailStatus.WAITING
//        "READY" -> CapsuleDetailStatus.READY
//        "OPENED" -> CapsuleDetailStatus.OPENED
//        else -> CapsuleDetailStatus.LOCKED
//    }
//
//    val capsuleInfo = CapsuleDetailInfo(
//        capsuleId = this.capsuleInfo.capsuleId,
//        title = this.capsuleInfo.title,
//        from = this.capsuleInfo.from,
//        openTime = this.capsuleInfo.openTime,
//        processPercent = this.capsuleInfo.processPercent,
//        content = this.capsuleInfo.content
//    )
//
//    val participants = this.participants.map { dto ->
//        CapsuleParticipant(
//            userId = dto.user_id,
//            userName = dto.userName,
//            isReady = dto.isReady ?: false
//        )
//    }
//
//    val conditions = this.conditions?.map { dto ->
//        CapsuleCondition(
//            type = dto.type,
//            value = dto.value,
//            isUnlocked = dto.isUnlocked ?: false
//        )
//    } ?: emptyList()
//
//    return CapsuleDetailData(
//        status = status,
//        capsuleInfo = capsuleInfo,
//        participants = participants,
//        conditions = conditions
//    )
//}