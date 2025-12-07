package com.example.rememory.domain.model

data class FriendItemDomainModel(
    val friendShipId: Int,
    val userId: Int, // 친구/요청 보낸 사람의 ID
    val userLoginId: String,
    val username: String,
)

/**
 * 친구 관리 화면의 전체 도메인 응답
 */
data class FriendDataDomain(
    val friends: List<FriendItemDomainModel> = emptyList(),
    val requests: List<FriendItemDomainModel> = emptyList() // 요청 목록을 별도로 관리
)