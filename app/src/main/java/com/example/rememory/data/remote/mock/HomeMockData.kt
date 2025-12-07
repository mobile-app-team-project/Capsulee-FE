package com.example.rememory.data.remote.mock

import com.example.rememory.domain.model.*

object HomeMockData {
    fun getHomeScreenData(): HomeScreenData {
        return HomeScreenData(
            status = CapsuleStatus.LOCKED,
            capsuleInfo = HomeCapsuleInfo(
                capsuleId = 100,
                title = "Graduation Celebration",
                from = "Gain Lee",
                openTime = "2025-12-31 at 14:30",
                processPercent = 70
            ),
            participants = listOf(
                ParticipantUser(1, "Gain Lee"),
                ParticipantUser(2, "Jisoo Kim"),
                ParticipantUser(3, "Minho Park")
            ),
            conditions = listOf(
                HomeCondition("LOCATION", "Chung-Ang University"),
                HomeCondition("TIME", "2025-03-15 at 14:30"),
                HomeCondition("ACTION", "Shake 3 times")
            )
        )
    }
}