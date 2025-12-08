package com.example.rememory.data.remote.api


import com.example.rememory.data.remote.dto.ActionConditionRequestDto
import com.example.rememory.data.remote.dto.ActionConditionResponseDto
import com.example.rememory.data.remote.dto.CapsuleDetailResponseDto
import com.example.rememory.data.remote.dto.CapsuleListResponseDto
import com.example.rememory.data.remote.dto.LobbyStatusResponseDto
import com.example.rememory.data.remote.dto.LocationConditionRequestDto
import com.example.rememory.data.remote.dto.LocationConditionResponseDto
import com.example.rememory.data.remote.dto.ReadyRequestDto
import com.example.rememory.data.remote.dto.ReadyResponseDto
import com.example.rememory.data.remote.dto.CreateCapsuleRequest
import com.example.rememory.data.remote.dto.CreateCapsuleResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Body
//목데이터를 위해
import com.example.rememory.data.remote.mock.CapsuleMockData
import kotlinx.coroutines.delay
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface CapsuleService {

    // Query Parameter: ?type=sent 또는 ?type=received
    @GET("/capsules")
    suspend fun getCapsules(
        @Query("type") type: String // "sent" 또는 "received"
    ): CapsuleListResponseDto

    // 캡슐 상세 조회
    @GET("/capsules/{capsuleId}")
    suspend fun getCapsuleDetail(
        @Path("capsuleId") capsuleId: Int
    ): CapsuleDetailResponseDto

    // ========== 조건 체크 API ==========

    // 위치/날씨 조건 체크
    @POST("/capsules/{capsuleId}/conditions/location")
    suspend fun checkLocationCondition(
        @Path("capsuleId") capsuleId: Int,
        @Body request: LocationConditionRequestDto
    ): LocationConditionResponseDto

    // 행동 조건 체크
    @POST("/capsules/{capsuleId}/conditions/action")
    suspend fun checkActionCondition(
        @Path("capsuleId") capsuleId: Int,
        @Body request: ActionConditionRequestDto
    ): ActionConditionResponseDto

    // ========== Ready/Open 관련 ==========

    // Ready 상태 설정
    @POST("/unlock/ready/{capsuleId}")
    suspend fun setReady(
        @Path("capsuleId") capsuleId: Int,
        @Body request: ReadyRequestDto
    ): ReadyResponseDto

    // 로비 상태 조회 (폴링용)
    @GET("/unlock/check/{capsuleId}")
    suspend fun checkLobbyStatus(
        @Path("capsuleId") capsuleId: Int
    ): LobbyStatusResponseDto

    // 캡슐 생성 API
    @Multipart
    @POST("/capsules")
    suspend fun createCapsule(
        @Part("data") data: RequestBody,
        @Part imageFile: MultipartBody.Part? = null
    ): CreateCapsuleResponse

}