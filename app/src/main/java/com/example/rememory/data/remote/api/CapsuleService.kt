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
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
//목데이터를 위해
import retrofit2.http.Body

/**
 * 캡슐 관련 API 통신 서비스 인터페이스
 */
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
}


///**
// * MOCK DATA
// */
//class MockCapsuleService : CapsuleService {
//
//    private val MOCK_DELAY_MS = 500L
//    private var currentMockState = 5 // 테스트용: 0=LOCKED, 1=WAITING, 2=ALL_UNLOCKED, 3=READY, 4=ALL_READY, 5=OPENED
//
//    override suspend fun getCapsules(type: String): CapsuleListResponseDto {
//        delay(MOCK_DELAY_MS)
//
//        return when (type.uppercase()) {
//            "SENT" -> CapsuleMockData.getSentCapsulesDto()
//            "RECEIVED" -> CapsuleMockData.getReceivedCapsulesDto()
//            else -> throw IllegalArgumentException("Invalid capsule type: $type")
//        }
//    }
//
//    override suspend fun getCapsuleDetail(capsuleId: Int): CapsuleDetailResponseDto {
//        delay(MOCK_DELAY_MS)
//
//        // 테스트를 위해 다양한 상태 반환
//        return when (currentMockState) {
//            0 -> CapsuleDetailMockData.getLockedCapsule()
//            1 -> CapsuleDetailMockData.getWaitingCapsule()
//            2 -> CapsuleDetailMockData.getAllUnlockedCapsule()
//            3 -> CapsuleDetailMockData.getReadyCapsule()
//            4 -> CapsuleDetailMockData.getAllReadyCapsule()
//            else -> CapsuleDetailMockData.getOpenedCapsule()
//        }
//    }
//
//    override suspend fun markAsReady(capsuleId: Int): CapsuleDetailResponseDto {
//        delay(MOCK_DELAY_MS)
//
//        // Ready 버튼을 누르면 상태를 다음 단계로 진행
//        when (currentMockState) {
//            2 -> currentMockState = 3 // ALL_UNLOCKED -> READY
//            3 -> currentMockState = 4 // READY -> ALL_READY
//            4 -> currentMockState = 5 // ALL_READY -> OPENED (✅ 추가!)
//        }
//
//        return getCapsuleDetail(capsuleId)
//    }
//}