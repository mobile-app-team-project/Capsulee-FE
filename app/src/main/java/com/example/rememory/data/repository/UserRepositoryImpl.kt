package com.example.rememory.data.repository


import com.example.rememory.data.remote.dto.UserSearchDto
import com.example.rememory.domain.model.FriendItemDomainModel
import com.example.rememory.domain.model.UserSearchDomainModel
import com.example.rememory.domain.model.UserStatus
import com.example.rememory.domain.repository.UserRepository
import kotlinx.coroutines.delay

class UserRepositoryImpl: UserRepository {

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
    override suspend fun searchAllUsers(query: String): List<UserSearchDomainModel> {
        if (query.isBlank()) return emptyList()

        // Mock Logic 업데이트
        val mockData = when {
            query.contains("yeonwoo", ignoreCase = true) -> listOf(
                // 1. 친구 요청을 보낸 적도, 받은 적도 없는 경우 (null -> NONE)
                UserSearchDto(id = 1, loginID = "none_login", username = "NONE user", status = null),
                // 2. 친구 상태인 경우 (ACCEPTED)
                UserSearchDto(id = 2, loginID = "accepted_login", username = "ACCEPTED user", status = "ACCEPTED"),
                // 3. 내가 요청을 보내고 상대가 수락 대기 중인 경우 (PENDING)
                UserSearchDto(id = 3, loginID = "pending_login", username = "PENDING user", status = "PENDING"),
                // 4. 요청이 거절된 경우 (REJECTED)
                UserSearchDto(id = 4, loginID = "rejected_login", username = "REJECTED user", status = "REJECTED"),
                // 5. 나에게 친구 요청이 온 경우 (RECEIVED - 가정)
                UserSearchDto(id = 5, loginID = "received_login", username = "RECEIVED user", status = "RECEIVED")
            )
            else -> emptyList()
        }

        return mockData.map { it.toDomainModel() }
    }
}