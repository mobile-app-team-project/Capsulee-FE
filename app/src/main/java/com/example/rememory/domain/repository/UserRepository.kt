package com.example.rememory.domain.repository


import com.example.rememory.domain.model.FriendItemDomainModel // 사용자도 이 모델을 사용한다고 가정

interface UserRepository {
    suspend fun searchAllUsers(query: String): List<FriendItemDomainModel>
}