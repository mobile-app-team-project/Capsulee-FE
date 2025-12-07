package com.example.rememory.data.remote.api


import com.example.rememory.data.remote.dto.UserSearchDto
import com.example.rememory.data.remote.dto.FriendRequestSendDto
import com.example.rememory.data.remote.dto.FriendRequestResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserService {
    /** 사용자 검색: GET /user?query={query} */
    @GET("/user")
    suspend fun searchAllUsersApi(@Query("query") query: String): List<UserSearchDto>

    /** 친구 요청: POST /friends (토큰 헤더 필요) */
    @POST("/friends")
    suspend fun requestFriendApi(@Body request: FriendRequestSendDto): FriendRequestResponseDto
}