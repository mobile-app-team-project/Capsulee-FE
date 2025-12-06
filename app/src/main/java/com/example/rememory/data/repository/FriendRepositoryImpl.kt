package com.example.rememory.data.repository


import com.example.rememory.data.remote.dto.FriendListDto
import com.example.rememory.data.remote.dto.FriendRequestDto
import com.example.rememory.data.remote.mock.FriendMockData
import com.example.rememory.domain.model.FriendItemDomainModel
import com.example.rememory.domain.repository.FriendRepository
import kotlinx.coroutines.delay

class FriendRepositoryImpl : FriendRepository {

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
}