package com.example.rememory.di

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
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindFriendRepository(
        impl: FriendRepositoryImpl
    ): FriendRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindCapsuleRepository(
        impl: CapsuleRepositoryImpl
    ): CapsuleRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindHomeRepository(
        impl: HomeRepositoryImpl
    ): HomeRepository
}