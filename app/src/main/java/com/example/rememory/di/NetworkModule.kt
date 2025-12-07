package com.example.rememory.di


import com.example.rememory.data.remote.api.FriendService
import com.example.rememory.data.remote.api.UserService
import com.example.rememory.data.repository.FriendRepositoryImpl
import com.example.rememory.data.repository.UserRepositoryImpl
import com.example.rememory.domain.repository.FriendRepository
import com.example.rememory.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // ✅ 앱 수명 주기 동안 싱글톤 인스턴스 유지
object NetworkModule {

    private const val BASE_URL = "http://capsulee.p-e.kr:8080/"

    // 1. OkHttpClient 제공 (로깅 및 인터셉터 설정)
    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            // 인증 Interceptor 추가 위치
            .build()
    }

    // 2. Retrofit 인스턴스 제공
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()) // Gson 사용 가정
            .build()
    }

    // 3. API Service 인터페이스 제공 (Retrofit 인스턴스 주입받아 생성)
    @Provides
    @Singleton
    fun provideFriendService(retrofit: Retrofit): FriendService {
        return retrofit.create(FriendService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    // 4. Repository 구현체 제공 (Service 인스턴스 주입받아 생성)
    @Provides
    @Singleton
    fun provideFriendRepository(service: FriendService): FriendRepository {
        return FriendRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideUserRepository(service: UserService): UserRepository {
        return UserRepositoryImpl()
    }
}