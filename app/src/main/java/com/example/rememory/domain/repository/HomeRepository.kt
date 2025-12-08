package com.example.rememory.domain.repository

import com.example.rememory.data.remote.dto.CapsuleDetailResponseDto
import com.example.rememory.domain.model.HomeScreenData

interface HomeRepository {
    suspend fun getHomeData(): CapsuleDetailResponseDto
}