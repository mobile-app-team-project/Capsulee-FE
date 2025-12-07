package com.example.rememory.data.repository

import com.example.rememory.data.remote.mock.HomeMockData
import com.example.rememory.domain.model.HomeScreenData
import com.example.rememory.domain.repository.HomeRepository
import kotlinx.coroutines.delay

class HomeRepositoryImpl : HomeRepository {
    override suspend fun getHomeData(): HomeScreenData {
        delay(500) // 네트워크 지연 시뮬레이션
        return HomeMockData.getHomeScreenData()
    }
}