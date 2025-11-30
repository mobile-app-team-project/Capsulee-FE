package com.example.rememory.data.remote.api


import com.example.rememory.data.remote.dto.CapsuleListResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 캡슐 관련 API 통신 서비스 인터페이스
 */
interface CapsuleService {

    // Query Parameter: ?type=sent 또는 ?type=received
    @GET("/v1/capsules")
    suspend fun getCapsules(
        @Query("type") type: String // "sent" 또는 "received"
    ): CapsuleListResponseDto
}