package com.example.rememory.data.remote.dto


import kotlinx.serialization.Serializable

/**
 * 친구 요청 API 요청 바디 (Request Body)
 */
@Serializable
data class FriendRequestSendDto(
    val receiverLoginId: String
)

/**
 * 친구 요청 API 응답 바디 (Response Body)
 */
@Serializable
data class FriendRequestResponseDto(
    val friendShipId: Int,
    val status: String,
    val senderLoginId: String,
    val receiverLoginId: String
)
/**
 * 친구 요청 처리 API 요청 바디 (Request Body - PUT)
 */
@Serializable
data class FriendRequestProcessDto(
    val senderLoginId: String,
    val receiverLoginId: String,
    val status: String // "ACCEPTED" 또는 "REJECTED"
)