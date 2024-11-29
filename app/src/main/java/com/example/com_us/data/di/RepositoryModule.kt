package com.example.com_us.data.di

import com.example.com_us.data.default_repository.DefaultAuthRepository
import com.example.com_us.data.default_repository.DefaultHomeRepository
import com.example.com_us.data.default_repository.DefaultProfileRepository
import com.example.com_us.data.default_repository.DefaultQuestionRepository
import com.example.com_us.data.repository.AuthRepository
import com.example.com_us.data.repository.HomeRepository
import com.example.com_us.data.repository.ProfileRepository
import com.example.com_us.data.repository.QuestionRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {
    @Binds
    @Singleton
    fun provideHomeRepository(defaultRepository : DefaultHomeRepository) :HomeRepository

    @Binds
    fun provideProfileRepository(defaultRepository : DefaultProfileRepository) : ProfileRepository

    @Binds
    fun provideQuestionRepository(defaultRepository : DefaultQuestionRepository) : QuestionRepository

    @Binds
    fun provideAuthRepository(defaultRepository : DefaultAuthRepository) : AuthRepository

}