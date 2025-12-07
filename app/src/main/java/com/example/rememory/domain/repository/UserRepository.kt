package com.example.rememory.domain.repository

import com.example.rememory.domain.model.MyInfoDomainModel
import com.example.rememory.domain.model.UserSearchDomainModel


interface UserRepository {
    suspend fun searchAllUsers(query: String): List<UserSearchDomainModel>

    /**
     * 친구 요청 API 호출 및 성공 여부/업데이트된 상태 반환
     */
    suspend fun requestFriend(receiverLoginId: String): Boolean

    suspend fun getMyInfo(): MyInfoDomainModel
}