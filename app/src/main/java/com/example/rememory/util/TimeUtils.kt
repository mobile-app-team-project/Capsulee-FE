package com.example.rememory.util

import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {
    /**
     * "2025-12-25 at 09:00" 형식의 문자열을 파싱하여 남은 시간을 계산합니다.
     * @return Pair<Int, String> - (남은 일수, "HH:MM" 형식의 시간)
     */
    fun calculateTimeRemaining(openTimeString: String): Triple<Int, String, Int> {
        try {
            // "2025-12-25 at 09:00" -> "2025-12-25 09:00"
            val cleanedString = openTimeString.replace(" at ", " ")

            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val targetDate = dateFormat.parse(cleanedString) ?: return Triple(0, "00:00", 0)

            val currentTime = Calendar.getInstance().time
            val diffInMillis = targetDate.time - currentTime.time

            if (diffInMillis < 0) {
                return Triple(0, "00:00", 0)
            }

            val days = (diffInMillis / (1000 * 60 * 60 * 24)).toInt()
            val hours = ((diffInMillis / (1000 * 60 * 60)) % 24).toInt()
            val minutes = ((diffInMillis / (1000 * 60)) % 60).toInt()
            val seconds = ((diffInMillis / 1000) % 60).toInt()

            val timeString = String.format("%02d:%02d", hours, minutes)

            return Triple(days, timeString, seconds)
        } catch (e: Exception) {
            e.printStackTrace()
            return Triple(0, "00:00", 0)
        }
    }
}