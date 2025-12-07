package com.example.rememory.data.remote.mock

import com.example.rememory.data.remote.dto.FriendListDto
import com.example.rememory.data.remote.dto.FriendRequestDto

object FriendMockData {
    val mockFriendListDto = listOf(
        FriendListDto(friendShipId = 101, friendId = 1, friendLoginId = "user_y", friendUsername = "윤우"),
        FriendListDto(friendShipId = 102, friendId = 2, friendLoginId = "user_j", friendUsername = "지수")
    )

    val mockRequestListDto = listOf(
        FriendRequestDto(friendShipId = 201, senderId = 3, senderLoginId = "user_m", senderUsername = "민호"),
        FriendRequestDto(friendShipId = 202, senderId = 4, senderLoginId = "user_s", senderUsername = "서현")
    )
}