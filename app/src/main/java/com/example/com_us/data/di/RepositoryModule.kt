package com.example.com_us.data.di

import android.content.Context
import com.example.com_us.data.repository.impl.AuthRepositoryImpl
import com.example.com_us.data.repository.impl.HomeRepositoryImpl
import com.example.com_us.data.repository.impl.ProfileRepositoryImpl
import com.example.com_us.data.repository.impl.QuestionRepositoryImpl
import com.example.com_us.data.repository.AuthRepository
import com.example.com_us.data.repository.HomeRepository
import com.example.com_us.data.repository.ProfileRepository
import com.example.com_us.data.repository.QuestionRepository
import com.example.com_us.data.repository.UserTokenRepository
import com.example.com_us.data.repository.impl.UserTokenRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {
    @Binds
    @Singleton
    fun provideHomeRepository(defaultRepository : HomeRepositoryImpl) :HomeRepository

    @Binds
    fun provideProfileRepository(defaultRepository : ProfileRepositoryImpl) : ProfileRepository

    @Binds
    fun provideQuestionRepository(defaultRepository : QuestionRepositoryImpl) : QuestionRepository

    @Binds
    fun provideAuthRepository(defaultRepository : AuthRepositoryImpl) : AuthRepository

    @Binds
    fun provideUserTokenRepository(
        defaultRepository: UserTokenRepositoryImpl,
    ) : UserTokenRepository

}