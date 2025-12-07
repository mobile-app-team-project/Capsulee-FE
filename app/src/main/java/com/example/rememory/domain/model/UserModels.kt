package com.example.rememory.domain.model

enum class UserStatus {
    NONE,       // 친구 요청을 주고받은 적 없음 (null) 또는 나 자신 -> request
    ACCEPTED,   // 요청 수락됨 (친구 상태) -> connected
    PENDING,    // 신청 대기 중 (내가 요청을 보낸 상태) -> pending..
    REJECTED   // 요청 거절 됨 -> request
}

data class UserSearchDomainModel(
    val userId: Int,
    val userLoginId: String,
    val username: String,
    val status: UserStatus // 친구 상태/요청 상태/나 자신 표시
)
