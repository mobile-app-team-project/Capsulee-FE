package com.example.rememory.data.remote.api

import com.example.rememory.data.remote.dto.FriendListDto
import com.example.rememory.data.remote.dto.FriendRequestDto
import com.example.rememory.data.remote.dto.FriendRequestProcessDto
import com.example.rememory.data.remote.dto.FriendRequestResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface FriendService {

    /** 친구 목록 조회: GET /friends/list */
    @GET("/friends/list")
    suspend fun getFriendListApi(): List<FriendListDto>

    /** 친구 요청 목록 조회: GET /friends/requests */
    @GET("/friends/requests")
    suspend fun getFriendRequestsApi(): List<FriendRequestDto>

    /** 친구 삭제: DELETE /friends/{friendshipId} */
    @DELETE("/friends/{friendshipId}")
    suspend fun deleteFriendApi(@Path("friendshipId") friendshipId: Int)

    /** 친구 요청 처리: PUT /friends */
    @PUT("/friends")
    suspend fun processFriendRequestApi(@Body request: FriendRequestProcessDto): FriendRequestResponseDto
}