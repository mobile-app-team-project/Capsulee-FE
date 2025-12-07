package com.example.rememory.data.remote.api

import com.example.rememory.data.remote.dto.FriendListDto
import com.example.rememory.data.remote.dto.FriendRequestDto
import com.example.rememory.data.remote.dto.FriendRequestProcessDto
import com.example.rememory.data.remote.dto.FriendRequestResponseDto
import com.example.rememory.data.remote.dto.FriendRequestSendDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface FriendService {

    /** 친구 목록 조회: GET /friends/list */
    @GET("/friends")
    suspend fun getFriendListApi(): List<FriendListDto>

    /** 친구 요청 목록 조회: GET  */
    @GET("/friends/pending")
    suspend fun getFriendRequestsApi(): List<FriendRequestDto>

    /** 친구 삭제: DELETE /friends/{friendshipId} */
    @DELETE("/friends/{friendshipId}")
    suspend fun deleteFriendApi(@Path("friendshipId") friendshipId: Int)

    //  [추가] 친구 요청 처리: PUT /friends/response
    @PUT("/friends/response")
    suspend fun processFriendRequestApi(@Body request: FriendRequestProcessDto): FriendRequestResponseDto

    // [추가] 친구 요청 보내기: POST /friends/request
    @POST("/friends/request")
    suspend fun requestFriendApi(@Body request: FriendRequestSendDto): FriendRequestResponseDto}