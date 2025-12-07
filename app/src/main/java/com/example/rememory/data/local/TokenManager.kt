package com.example.rememory.data.local

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun saveTokens(accessToken: String, refreshToken: String) {
        prefs.edit().apply {
            putString("access_token", accessToken)
            putString("refresh_token", refreshToken)
            apply()
        }
    }

    fun getAccessToken(): String {
        return prefs.getString("access_token", "") ?: ""
    }

    fun getRefreshToken(): String {
        return prefs.getString("refresh_token", "") ?: ""
    }

    fun clearTokens() {
        prefs.edit { clear() }
        //prefs.edit().clear().apply()
    }
}