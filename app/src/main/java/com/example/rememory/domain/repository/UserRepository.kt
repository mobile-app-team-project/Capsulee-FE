package com.example.rememory.domain.repository

import com.example.rememory.domain.model.UserSearchDomainModel


interface UserRepository {
    suspend fun searchAllUsers(query: String): List<UserSearchDomainModel>
}