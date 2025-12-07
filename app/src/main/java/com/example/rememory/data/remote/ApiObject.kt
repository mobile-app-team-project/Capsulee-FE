package com.example.rememory.data.remote

import com.example.rememory.data.remote.api.FriendService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * 앱 전체에서 사용될 Retrofit 인스턴스 및 API 서비스 제공 싱글톤 객체.
 * BASE_URL, OkHttpClient 설정, Converter 설정을 담당합니다.
 */
object ApiObject {

    // 1. BASE URL 정의
    // 🚨 TODO: 실제 서버의 기본 URL로 변경해야 합니다.
    private const val BASE_URL = "https://api.yourrememoryapp.com/"

    // 2. OkHttpClient 설정 (선택 사항: 로깅 및 타임아웃)
    private val getOkHttpClient by lazy {
        // HTTP 통신 로그를 보기 위한 인터셉터 (디버깅 시 유용)
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            // TODO: 실제 배포 시 Level.NONE으로 변경하거나 BuildConfig를 사용해야 합니다.
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS) // 연결 시간 초과
            .readTimeout(15, TimeUnit.SECONDS)    // 읽기 시간 초과
            .addInterceptor(loggingInterceptor)
            // TODO: 사용자 인증(JWT 등)이 필요한 경우, Token Interceptor를 여기에 추가합니다.
            .build()
    }

    // 3. Retrofit 인스턴스 지연 초기화
    private val getRetrofit by lazy {
        // Retrofit Builder
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getOkHttpClient)
            // JSON 처리 Converter 선택 (Gson과 Kotlinx Serialization 중 하나만 사용해야 합니다.)
            // Gson 사용:
            .addConverterFactory(GsonConverterFactory.create())
            // Kotlinx Serialization 사용 (필요 시 주석 해제):
            // val contentType = "application/json".toMediaType()
            // .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
    }

    // 4. API Service 인스턴스 제공

    // ✅ 친구 관련 서비스 인스턴스
    val friendService: FriendService by lazy {
        getRetrofit.create(FriendService::class.java)
    }

    // ✅ 캡슐 관련 서비스 인스턴스
    // 🚨 TODO: CapsuleService 인터페이스를 정의한 후 사용해야 합니다.
    // val capsuleService: CapsuleService by lazy {
    //     getRetrofit.create(CapsuleService::class.java)
    // }

    // ⚠️ 경고: 현재 코드에서는 Gson과 Kotlinx Serialization Converter가 모두 주석 처리되어 있습니다.
    //    실제 사용 시 둘 중 하나를 선택하고 활성화해야 합니다.
}