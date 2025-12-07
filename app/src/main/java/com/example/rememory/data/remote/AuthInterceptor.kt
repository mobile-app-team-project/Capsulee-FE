package com.example.rememory.data.remote


import okhttp3.Interceptor
import okhttp3.Response
// 🚨 TODO: SharedPreferences 또는 DataStore에서 토큰을 가져오는 로직이 필요합니다.

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // 🎯 토큰을 가져오는 임시 로직 (실제 앱의 토큰 저장소로 대체해야 함)
        val token = getCurrentUserToken()

        val newRequest = chain.request().newBuilder()
            .apply {
                if (token.isNotEmpty()) {
                    // ✅ 모든 요청의 헤더에 Authorization 토큰 추가
                    header("Authorization", "Bearer $token")
                }
            }
            .build()

        return chain.proceed(newRequest)
    }

    // 🚨 TODO: 실제 토큰을 가져오는 함수로 대체해야 합니다.
    private fun getCurrentUserToken(): String {
        // 예시: 실제로는 SharedPreferences나 DataStore에서 가져와야 함
        return "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3NjUwOTc0ODAsImV4cCI6MTc2NTEwMTA4MCwibG9naW5JRCI6InVzZXIzIn0.UruS4m1KgrMjBTEziXkMHV_TBwqjpEg9AEit8yQmCOU"
    }
}