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
}