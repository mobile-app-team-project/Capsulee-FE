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

    private companion object {
        const val USER_LOGIN_ID_KEY = "user_login_id"
    }

    fun saveTokens(accessToken: String, refreshToken: String) {
        prefs.edit().apply {
            putString("access_token", accessToken)
            putString("refresh_token", refreshToken)
            apply()
        }
    }

    fun saveLoggedInUserLoginId(loginId: String) {
        prefs.edit {
            putString(USER_LOGIN_ID_KEY, loginId)
        }
    }

    fun getLoggedInUserLoginId(): String? {
        return prefs.getString(USER_LOGIN_ID_KEY, null)
    }

    fun getAccessToken(): String {
        return prefs.getString("access_token", "") ?: ""
    }

    fun getRefreshToken(): String {
        return prefs.getString("refresh_token", "") ?: ""
    }

    fun clearTokens() {
        prefs.edit { clear() }
    }
}