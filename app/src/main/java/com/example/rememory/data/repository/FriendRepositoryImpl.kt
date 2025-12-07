package com.example.rememory.data.repository


import com.example.rememory.data.remote.api.FriendService
import com.example.rememory.data.remote.dto.FriendListDto
import com.example.rememory.data.remote.dto.FriendRequestDto
import com.example.rememory.data.remote.dto.FriendRequestProcessDto
import com.example.rememory.data.remote.dto.FriendRequestSendDto
import com.example.rememory.data.remote.mock.FriendMockData
import com.example.rememory.domain.model.FriendItemDomainModel
import com.example.rememory.domain.repository.FriendRepository
import kotlinx.coroutines.delay

class FriendRepositoryImpl (
    private val friendService: FriendService
): FriendRepository {

    // ----------------------------------------------------
    // Mapper 로직: DTO를 Domain Model로 변환
    // ----------------------------------------------------

    // 1. 요청 DTO -> Domain Model
    private fun FriendRequestDto.toDomainModel(): FriendItemDomainModel {
        return FriendItemDomainModel(
            friendShipId = this.friendShipId,
            userId = this.senderId,
            userLoginId = this.senderLoginId,
            username = this.senderUsername
        )
    }

    // 2. 목록 DTO -> Domain Model
    private fun FriendListDto.toDomainModel(): FriendItemDomainModel {
        return FriendItemDomainModel(
            friendShipId = this.friendShipId,
            userId = this.friendId,
            userLoginId = this.friendLoginId,
            username = this.friendUsername
        )
    }

    // ----------------------------------------------------
    // Repository 함수 구현 (API 호출)
    // ----------------------------------------------------

    override suspend fun getFriendList(): List<FriendItemDomainModel> {
        // 1. API 호출
        val response = friendService.getFriendListApi()
        // 2. DTO를 Domain Model로 변환하여 반환
        return response.map { it.toDomainModel() }
    }

    override suspend fun getFriendRequests(): List<FriendItemDomainModel> {
        // 1. API 호출
        val response = friendService.getFriendRequestsApi()
        // 2. DTO를 Domain Model로 변환하여 반환
        return response.map { it.toDomainModel() }
    }

    override suspend fun deleteFriend(friendshipId: Int) {
        // 1. API 호출 (DELETE 요청)
        friendService.deleteFriendApi(friendshipId)
    }

    // ✅ [추가] 친구 요청 보내기 기능 (POST)
    override suspend fun requestFriend(receiverLoginId: String): Boolean {
        val requestBody = FriendRequestSendDto(receiverLoginId = receiverLoginId)
        try {
            friendService.requestFriendApi(requestBody) // 🎯 FriendService 사용
            return true
        } catch (e: Exception) {
            return false
        }
    }

    // 친구 요청 처리 기능
    override suspend fun processFriendRequest(
        senderLoginId: String,
        actionStatus: String
    ): Boolean {
        val receiverLoginId = "user3" // 🚨 TODO: 현재 로그인한 사용자 ID로 대체해야 함

        val requestBody = FriendRequestProcessDto(
            senderLoginId = senderLoginId,
            receiverLoginId = receiverLoginId,
            status = actionStatus
        )

        try {
            // 1. API 호출
            friendService.processFriendRequestApi(requestBody)
            println("로그: 요청 처리 성공 ($senderLoginId -> $actionStatus)")
            return true
        } catch (e: Exception) {
            println("요청 처리 API 실패: ${e.message}")
            return false
        }
    }
}