//package com.example.rememory.data.remote.mock
//
//import com.example.rememory.data.remote.dto.*
//
//object CapsuleDetailMockData {
//
//    // LOCKED 상태 Mock 데이터
//    fun getLockedCapsule(): CapsuleDetailResponseDto {
//        return CapsuleDetailResponseDto(
//            status = "LOCKED",
//            capsuleInfo = CapsuleDetailInfoDto(
//                capsuleId = 100,
//                title = "Graduation Celebration",
//                from = "Gain Lee",
//                openTime = "2025-12-25 at 09:00",
//                processPercent = 70,
//                content = null
//            ),
//            participants = listOf(
//                ParticipantDto(user_id = 1, userName = "Yeonwoo", isReady = null),
//                ParticipantDto(user_id = 2, userName = "Bowoon", isReady = null),
//                ParticipantDto(user_id = 3, userName = "YoungGyoung", isReady = null),
//                ParticipantDto(user_id = 4, userName = "Jiyo", isReady = null)
//            ),
//            conditions = listOf(
//                ConditionDto(type = "LOCATION", value = "Chung-Ang University", isUnlocked = null),
//                ConditionDto(type = "WEATHER", value = "Snow", isUnlocked = null),
//                ConditionDto(type = "ACTION", value = "Shake 3 times", isUnlocked = null)
//            )
//        )
//    }
//
//    // WAITING 상태 Mock 데이터 (조건 일부 충족)
//    fun getWaitingCapsule(): CapsuleDetailResponseDto {
//        return CapsuleDetailResponseDto(
//            status = "WAITING",
//            capsuleInfo = CapsuleDetailInfoDto(
//                capsuleId = 100,
//                title = "Graduation Celebration",
//                from = "Gain Lee",
//                openTime = "2025-12-25 at 09:00",
//                processPercent = null,
//                content = null
//            ),
//            participants = listOf(
//                ParticipantDto(user_id = 1, userName = "Yeonwoo", isReady = null),
//                ParticipantDto(user_id = 2, userName = "Bowoon", isReady = null),
//                ParticipantDto(user_id = 3, userName = "YoungGyoung", isReady = null),
//                ParticipantDto(user_id = 4, userName = "Jiyo", isReady = null)
//            ),
//            conditions = listOf(
//                ConditionDto(type = "LOCATION", value = "Chung-Ang University", isUnlocked = true),
//                ConditionDto(type = "WEATHER", value = "Snow", isUnlocked = false),
//                ConditionDto(type = "ACTION", value = "Shake 3 times", isUnlocked = false)
//            )
//        )
//    }
//
//    // WAITING 상태 Mock 데이터 (모든 조건 충족, Ready 대기)
//    fun getAllUnlockedCapsule(): CapsuleDetailResponseDto {
//        return CapsuleDetailResponseDto(
//            status = "WAITING",
//            capsuleInfo = CapsuleDetailInfoDto(
//                capsuleId = 100,
//                title = "Graduation Celebration",
//                from = "Gain Lee",
//                openTime = "2025-12-25 at 09:00",
//                processPercent = null,
//                content = null
//            ),
//            participants = listOf(
//                ParticipantDto(user_id = 1, userName = "Yeonwoo", isReady = null),
//                ParticipantDto(user_id = 2, userName = "Bowoon", isReady = null),
//                ParticipantDto(user_id = 3, userName = "YoungGyoung", isReady = null),
//                ParticipantDto(user_id = 4, userName = "Jiyo", isReady = null)
//            ),
//            conditions = listOf(
//                ConditionDto(type = "LOCATION", value = "Chung-Ang University", isUnlocked = true),
//                ConditionDto(type = "WEATHER", value = "Snow", isUnlocked = true),
//                ConditionDto(type = "ACTION", value = "Shake 3 times", isUnlocked = true)
//            )
//        )
//    }
//
//    // READY 상태 Mock 데이터 (일부만 Ready)
//    fun getReadyCapsule(): CapsuleDetailResponseDto {
//        return CapsuleDetailResponseDto(
//            status = "READY",
//            capsuleInfo = CapsuleDetailInfoDto(
//                capsuleId = 100,
//                title = "Graduation Celebration",
//                from = "Gain Lee",
//                openTime = "2025-12-25 at 09:00",
//                processPercent = null,
//                content = null
//            ),
//            participants = listOf(
//                ParticipantDto(user_id = 1, userName = "Yeonwoo", isReady = true),
//                ParticipantDto(user_id = 2, userName = "Bowoon", isReady = false),
//                ParticipantDto(user_id = 3, userName = "YoungGyoung", isReady = true),
//                ParticipantDto(user_id = 4, userName = "Jiyo", isReady = true)
//            ),
//            conditions = null
//        )
//    }
//
//    // READY 상태 Mock 데이터 (모두 Ready)
//    fun getAllReadyCapsule(): CapsuleDetailResponseDto {
//        return CapsuleDetailResponseDto(
//            status = "READY",
//            capsuleInfo = CapsuleDetailInfoDto(
//                capsuleId = 100,
//                title = "Graduation Celebration",
//                from = "Gain Lee",
//                openTime = "2025-12-25 at 09:00",
//                processPercent = null,
//                content = null
//            ),
//            participants = listOf(
//                ParticipantDto(user_id = 1, userName = "Yeonwoo", isReady = true),
//                ParticipantDto(user_id = 2, userName = "Bowoon", isReady = true),
//                ParticipantDto(user_id = 3, userName = "YoungGyoung", isReady = true),
//                ParticipantDto(user_id = 4, userName = "Jiyo", isReady = true)
//            ),
//            conditions = null
//        )
//    }
//
//    // OPENED 상태 Mock 데이터
//    fun getOpenedCapsule(): CapsuleDetailResponseDto {
//        return CapsuleDetailResponseDto(
//            status = "OPENED",
//            capsuleInfo = CapsuleDetailInfoDto(
//                capsuleId = 100,
//                title = "Graduation Celebration",
//                from = "Gain Lee",
//                openTime = "2025-12-25 at 09:00",
//                processPercent = null,
//                content = "Congratulations on our graduation! This is the moment we've been waiting for. Remember all the late nights studying, the laughter we shared, and the challenges we overcame together. Here's to new beginnings!"
//            ),
//            participants = listOf(
//                ParticipantDto(user_id = 1, userName = "Yeonwoo", isReady = true),
//                ParticipantDto(user_id = 2, userName = "Bowoon", isReady = true),
//                ParticipantDto(user_id = 3, userName = "YoungGyoung", isReady = true),
//                ParticipantDto(user_id = 4, userName = "Jiyo", isReady = true)
//            ),
//            conditions = listOf(
//                ConditionDto(type = "LOCATION", value = "Chung-Ang University", isUnlocked = true),
//                ConditionDto(type = "WEATHER", value = "Snow", isUnlocked = true),
//                ConditionDto(type = "ACTION", value = "Shake 3 times", isUnlocked = true)
//            )
//        )
//    }
//}