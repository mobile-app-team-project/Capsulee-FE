package com.example.rememory.data.remote.dto


import kotlinx.serialization.Serializable

@Serializable
data class UserSearchDto(
    val id: Int,
    val loginID: String, // 서버 응답 필드명과 일치시켜야 함
    val username: String,
    val status: String? // 서버에서는 null 또는 문자열("ACCEPTED" 등)로 옴
)

typealias UserSearchResponse = List<UserSearchDto> // 응답 전체는 리스트입니다.