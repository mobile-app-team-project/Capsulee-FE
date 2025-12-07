package com.example.rememory.domain.repository


import com.example.rememory.domain.model.FriendItemDomainModel

interface FriendRepository {
    /**
     * 친구 목록 (리스트)을 가져옵니다.
     */
    suspend fun getFriendList(): List<FriendItemDomainModel>


    /**
     * 친구 요청 목록 (요청)을 가져옵니다.
     */
    suspend fun getFriendRequests(): List<FriendItemDomainModel>

    suspend fun deleteFriend(friendshipId: Int)

    /**
     * ✅ 추가: 친구 요청 수락 또는 거절 API 호출
     * @param senderLoginId 요청을 보낸 사람의 ID
     * @param actionStatus "ACCEPTED" 또는 "REJECTED"
     * @return 성공 여부 (Boolean)
     */
    suspend fun processFriendRequest(
        senderLoginId: String,
        actionStatus: String
    ): Boolean
}