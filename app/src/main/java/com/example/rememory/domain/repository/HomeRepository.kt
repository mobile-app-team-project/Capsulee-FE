package com.example.rememory.domain.repository

import com.example.rememory.domain.model.HomeScreenData

interface HomeRepository {
    suspend fun getHomeData(): HomeScreenData
}