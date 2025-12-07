package com.example.rememory.di


import com.example.rememory.data.local.TokenManager
import com.example.rememory.data.remote.AuthInterceptor
import com.example.rememory.data.remote.api.AuthService
import com.example.rememory.data.remote.api.CapsuleService
import com.example.rememory.data.remote.api.FriendService
import com.example.rememory.data.remote.api.UserService
import com.example.rememory.data.repository.AuthRepositoryImpl
import com.example.rememory.data.repository.CapsuleRepositoryImpl
import com.example.rememory.data.repository.FriendRepositoryImpl
import com.example.rememory.data.repository.HomeRepositoryImpl
import com.example.rememory.data.repository.UserRepositoryImpl
import com.example.rememory.domain.repository.AuthRepository
import com.example.rememory.domain.repository.CapsuleRepository
import com.example.rememory.domain.repository.FriendRepository
import com.example.rememory.domain.repository.HomeRepository
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
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "http://capsulee.p-e.kr:8080/"

    // 0. AuthInterceptor 인스턴스 제공
    @Provides
    @Singleton
    fun provideAuthInterceptor(tokenManager: TokenManager): AuthInterceptor {
        return AuthInterceptor(tokenManager)
    }

    // HttpLoggingInterceptor 인스턴스 제공
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            // TODO: 실제 배포 시 Level.NONE으로 변경
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    // 1. OkHttpClient 제공 (로깅 및 인터셉터 설정)
    @Provides
    @Singleton
    fun provideHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor
    ): OkHttpClient {

        return OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
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

    @Provides
    @Singleton
    fun provideCapsuleService(retrofit: Retrofit): CapsuleService {
        return retrofit.create(CapsuleService::class.java)
    }

    // 4. Repository 구현체 제공 (Service 인스턴스 주입받아 생성)
    @Provides
    @Singleton
    fun provideFriendRepository(service: FriendService): FriendRepository {
        return FriendRepositoryImpl(service)
    }

    @Provides
    @Singleton
    fun provideUserRepository(service: UserService, tokenManager: TokenManager): UserRepository {
        return UserRepositoryImpl(
            service,
            tokenManager = tokenManager
        )
    }

    //    Hilt는 이 함수를 통해 CapsuleRepository 인터페이스를 충족하는 구현체를 찾습니다.
    @Provides
    @Singleton
    fun provideCapsuleRepository(service: CapsuleService): CapsuleRepository {
        //  CapsuleRepositoryImpl 생성자가 CapsuleService를 인자로 받는다고 가정
        return CapsuleRepositoryImpl(service)
    }

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(service: AuthService): AuthRepository {
        return AuthRepositoryImpl(service)
    }

    @Provides
    @Singleton
    fun provideHomeRepository(): HomeRepository {
        return HomeRepositoryImpl()
    }
}