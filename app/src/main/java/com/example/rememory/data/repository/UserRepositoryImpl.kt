package com.example.rememory.data.repository


import com.example.rememory.data.remote.api.CapsuleService
import com.example.rememory.data.remote.api.UserService
import com.example.rememory.data.remote.dto.FriendRequestSendDto
import com.example.rememory.data.remote.dto.MyInfoResponseDto
import com.example.rememory.data.remote.dto.UserSearchDto
import com.example.rememory.domain.model.FriendItemDomainModel
import com.example.rememory.domain.model.MyInfoDomainModel
import com.example.rememory.domain.model.UserSearchDomainModel
import com.example.rememory.domain.model.UserStatus
import com.example.rememory.domain.repository.UserRepository
import kotlinx.coroutines.delay

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

private fun MyInfoResponseDto.toDomainModel(): MyInfoDomainModel {
    return MyInfoDomainModel(
        userId = this.id,
        loginId = this.loginID,
        nickname = this.username, // 서버 필드명은 username이지만 닉네임으로 매핑
        totalCapsules = this.stat.total,
        openedCapsules = this.stat.opened,
        totalFriends = this.stat.friends,
        isAlarmOn = this.okAlarm
    )
}

class UserRepositoryImpl(
    private val userService: UserService
): UserRepository {


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

    override suspend fun getMyInfo(): MyInfoDomainModel {
        // 1. API 호출
        val response = userService.getMyInfoApi()
        // 2. DTO를 Domain Model로 변환
        return response.toDomainModel()
    }
}