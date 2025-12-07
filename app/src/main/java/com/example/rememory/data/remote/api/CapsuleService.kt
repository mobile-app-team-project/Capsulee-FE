package com.example.rememory.data.remote.api


import com.example.rememory.data.remote.dto.CapsuleListResponseDto
import retrofit2.http.GET
import retrofit2.http.Query
//목데이터를 위해
import com.example.rememory.data.remote.mock.CapsuleMockData
import kotlinx.coroutines.delay

/**
 * 캡슐 관련 API 통신 서비스 인터페이스
 */
interface CapsuleService {

    // Query Parameter: ?type=sent 또는 ?type=received
    @GET("/capsules")
    suspend fun getCapsules(
        @Query("type") type: String // "sent" 또는 "received"
    ): CapsuleListResponseDto
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
}