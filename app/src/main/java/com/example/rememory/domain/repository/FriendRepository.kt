package com.example.rememory.domain.repository


import com.example.rememory.domain.model.FriendItemDomainModel

interface FriendRepository {
    // 1. GET /friends (친구 목록 조회)
    suspend fun getFriendList(): List<FriendItemDomainModel>

    // 2. GET /friends/pending (친구 요청 목록 조회)
    suspend fun getFriendRequests(): List<FriendItemDomainModel>

    // 3. DELETE /friends/{friendShipId} (친구 삭제)
    suspend fun deleteFriend(friendshipId: Int)

    // 4. POST /friends/request (친구 요청 보내기)
    suspend fun requestFriend(receiverLoginId: String): Boolean

    // 5. PUT /friends/response (친구 요청 응답/처리)
    suspend fun processFriendRequest(
        senderLoginId: String,
        actionStatus: String // "ACCEPTED" 또는 "REJECTED"
    ): Boolean
}