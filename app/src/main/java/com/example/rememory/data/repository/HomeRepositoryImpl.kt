package com.example.rememory.data.repository

import com.example.rememory.data.remote.api.CapsuleService
import com.example.rememory.data.remote.dto.CapsuleDetailResponseDto
import com.example.rememory.domain.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val api: CapsuleService
) : HomeRepository {
    override suspend fun getHomeData(): CapsuleDetailResponseDto {
//        delay(500) // 네트워크 지연 시뮬레이션
//        return HomeMockData.getHomeScreenData()
        return api.getHomeInfo()
    }
}