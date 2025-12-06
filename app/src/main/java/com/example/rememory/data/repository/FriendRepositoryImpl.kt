package com.example.rememory.data.repository


import com.example.rememory.data.remote.api.FriendService
import com.example.rememory.data.remote.dto.FriendListDto
import com.example.rememory.data.remote.dto.FriendRequestDto
import com.example.rememory.data.remote.mock.FriendMockData
import com.example.rememory.domain.model.FriendItemDomainModel
import com.example.rememory.domain.repository.FriendRepository
import kotlinx.coroutines.delay

class FriendRepositoryImpl (
    //private val friendService: FriendService
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
    // Repository 함수 구현 (Mock 데이터 사용)
    // ----------------------------------------------------

    override suspend fun getFriendList(): List<FriendItemDomainModel> {
        delay(500)
        return FriendMockData.mockFriendListDto.map { it.toDomainModel() } // 목록 DTO 사용
    }

    override suspend fun getFriendRequests(): List<FriendItemDomainModel> {
        delay(500)
        return FriendMockData.mockRequestListDto.map { it.toDomainModel() } // 요청 DTO 사용
    }

    // ----------------------------------------------------
    // Repository 함수 구현 (API 호출)
    // ----------------------------------------------------
/*
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
*/
    override suspend fun deleteFriend(friendshipId: Int) {
        // 1. API 호출 (DELETE 요청)
        //friendService.deleteFriendApi(friendshipId)
        // 응답 본문이 없거나 처리할 데이터가 없으므로 반환하지 않습니다.
    }
}