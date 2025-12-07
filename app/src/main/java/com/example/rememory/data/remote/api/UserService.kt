package com.example.rememory.data.remote.api


import com.example.rememory.data.remote.dto.UserSearchDto
import com.example.rememory.data.remote.dto.MyInfoResponseDto
import com.example.rememory.data.remote.dto.MyInfoUpdateDto
import retrofit2.http.Body

import retrofit2.http.GET
import retrofit2.http.PUT

interface UserService {
    /** 사용자 검색: GET */
    @GET("/users")
    suspend fun getAllUsersApi(): List<UserSearchDto>

    @GET("/users/me")
    suspend fun getMyInfoApi(): MyInfoResponseDto

    @PUT("/users/me")
    suspend fun updateMyInfoApi(@Body request: MyInfoUpdateDto): MyInfoResponseDto
}