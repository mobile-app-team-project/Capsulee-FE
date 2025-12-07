package com.example.rememory.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MyInfoResponseDto(
    val id: Int,
    val loginID: String,
    val username: String,
    val stat: MyStatsDto,
    val okAlarm: Boolean
)

@Serializable
data class MyStatsDto(
    val total: Int,   // 내가 수신한 총 캡슐 수
    val opened: Int,  // 내가 수신한 열린 캡슐 수
    val friends: Int  // 친구 수
)