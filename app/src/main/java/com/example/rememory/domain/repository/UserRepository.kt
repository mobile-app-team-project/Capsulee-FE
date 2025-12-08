package com.example.rememory.domain.repository

import com.example.rememory.domain.model.MyInfoDomainModel
import com.example.rememory.domain.model.UserSearchDomainModel


interface UserRepository {
    suspend fun getAllUsers(query: String): List<UserSearchDomainModel>

    suspend fun getMyInfo(): MyInfoDomainModel

    suspend fun updateMyInfo(nickname: String, loginId: String): MyInfoDomainModel

    suspend fun logoutUser()
}