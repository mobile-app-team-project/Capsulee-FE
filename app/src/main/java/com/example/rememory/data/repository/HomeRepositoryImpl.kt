package com.example.rememory.data.repository

import com.example.rememory.data.remote.api.CapsuleService
import com.example.rememory.data.remote.dto.CapsuleDetailResponseDto
import com.example.rememory.domain.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val api: CapsuleService
) : HomeRepository {
    override suspend fun getHomeData(): CapsuleDetailResponseDto? {
        val response = api.getHomeInfoResponse() // Response<CapsuleDetailResponseDto>
        return if (response.isSuccessful && response.body() != null) {
            response.body()
        } else {
            null // 홈 화면에서는 homeData == null 처리로 UI 구성 가능
        }
    }
}