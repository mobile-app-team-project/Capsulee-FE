package com.example.rememory.data.remote.api


import com.example.rememory.data.remote.dto.UserSearchDto
import com.example.rememory.data.remote.dto.FriendRequestSendDto
import com.example.rememory.data.remote.dto.FriendRequestResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserService {
    /** 사용자 검색: GET */
    @GET("/users")
    suspend fun getAllUsersApi(): List<UserSearchDto>
}