package com.example.rememory.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class FriendRequestDto(
    val friendShipId: Int,
    val senderId: Int,
    val senderLoginId: String,
    val senderUsername: String
)


@Serializable
data class FriendListDto(
    val friendShipId: Int,
    val friendId: Int,
    val friendLoginId: String,
    val friendUsername: String
)