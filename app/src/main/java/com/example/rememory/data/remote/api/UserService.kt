package com.example.rememory.data.remote.api


import com.example.rememory.data.remote.dto.UserSearchDto
import com.example.rememory.data.remote.dto.MyInfoResponseDto

import retrofit2.http.GET

interface UserService {
    /** 사용자 검색: GET */
    @GET("/users")
    suspend fun getAllUsersApi(): List<UserSearchDto>

    @GET("/users/me")
    suspend fun getMyInfoApi(): MyInfoResponseDto
}