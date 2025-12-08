package com.example.rememory.data.remote.api


import com.example.rememory.data.remote.dto.CapsuleListResponseDto
import com.example.rememory.data.remote.dto.CreateCapsuleRequest
import com.example.rememory.data.remote.dto.CreateCapsuleResponse
import retrofit2.http.GET
import retrofit2.http.Query
//목데이터를 위해
import com.example.rememory.data.remote.mock.CapsuleMockData
import kotlinx.coroutines.delay
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/**
 * 캡슐 관련 API 통신 서비스 인터페이스
 */
interface CapsuleService {

    // Query Parameter: ?type=sent 또는 ?type=received
    @GET("/capsules")
    suspend fun getCapsules(
        @Query("type") type: String // "sent" 또는 "received"
    ): CapsuleListResponseDto

    // 캡슐 생성 API
    @Multipart
    @POST("/capsules")
    suspend fun createCapsule(
        @Part("data") data: RequestBody,
        @Part imageFile: MultipartBody.Part? = null
    ): CreateCapsuleResponse

}


/**
 * MOCK DATA
 */
class MockCapsuleService : CapsuleService {

    private val MOCK_DELAY_MS = 500L

    override suspend fun getCapsules(type: String): CapsuleListResponseDto {
        delay(MOCK_DELAY_MS)

        return when (type.uppercase()) {
            "SENT" -> CapsuleMockData.getSentCapsulesDto()
            "RECEIVED" -> CapsuleMockData.getReceivedCapsulesDto()
            else -> throw IllegalArgumentException("Invalid capsule type: $type")
        }
    }

    override suspend fun createCapsule(
        data: RequestBody,
        imageFile: MultipartBody.Part?
    ): CreateCapsuleResponse {
        // 테스트용 mock 응답 반환
        return CreateCapsuleResponse(
            id = 999,
            title = "Mock Title",
            content = "Mock Content",
            imageUrl = "https://mock.image.url",
            openTime = "2025-01-01T00:00:00",
            recipientIds = listOf(1, 2),
            conditions = emptyList()
        )
    }
}