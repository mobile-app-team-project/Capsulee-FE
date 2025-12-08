package com.example.rememory.data.repository


import com.example.rememory.data.remote.api.CapsuleService
import com.example.rememory.data.remote.api.UserService
import com.example.rememory.data.remote.dto.FriendRequestSendDto
import com.example.rememory.data.remote.dto.UserSearchDto
import com.example.rememory.domain.model.FriendItemDomainModel
import com.example.rememory.domain.model.UserSearchDomainModel
import com.example.rememory.domain.model.UserStatus
import com.example.rememory.domain.repository.UserRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userService: UserService
): UserRepository {

    // ----------------------------------------------------
    // Mapper 로직: UserSearchDto -> UserSearchDomainModel
    // ----------------------------------------------------
    private fun UserSearchDto.toDomainModel(): UserSearchDomainModel {

        // 서버의 String 상태를 앱의 UserStatus Enum으로 변환
        val userStatus = when (this.status?.uppercase()) {
            "ACCEPTED" -> UserStatus.ACCEPTED
            "PENDING" -> UserStatus.PENDING
            "REJECTED" -> UserStatus.REJECTED
            else -> UserStatus.NONE
        }

        return UserSearchDomainModel(
            userId = this.id,
            userLoginId = this.loginID,
            username = this.username,
            status = userStatus
        )
    }

    //  검색 API 호출 활성화
    override suspend fun getAllUsers(query: String): List<UserSearchDomainModel> {

        try {
            // 1. API 호출: UserService의 getAllUsersApi를 호출합니다.
            val response = userService.getAllUsersApi()

            // 2. DTO 리스트를 Domain Model로 변환합니다.
            val allUsers = response.map { it.toDomainModel() }

            //    쿼리가 있을 경우에만 필터링을 적용합니다.
            if (query.isBlank()) {
                return allUsers
            }

            return allUsers.filter { user ->
                // userLoginId와 username 모두에서 쿼리를 포함하는지 확인
                user.userLoginId.contains(query, ignoreCase = true) ||
                        user.username.contains(query, ignoreCase = true)
            }

        } catch (e: Exception) {
            println("사용자 검색 API 호출 실패: ${e.message}")
            throw e
        }
    }
}