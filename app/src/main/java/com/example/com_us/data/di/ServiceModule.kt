package com.example.com_us.data.di

import com.example.com_us.data.service.AuthService
import com.example.com_us.data.service.HomeService
import com.example.com_us.data.service.ProfileService
import com.example.com_us.data.service.QuestionService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object ServiceModule {
    @Provides
    @Singleton
    fun provideHomeService(retrofit: Retrofit): HomeService =
        retrofit.create(HomeService::class.java)

    @Provides
    @Singleton
    fun provideProfileService(retrofit: Retrofit): ProfileService =
        retrofit.create(ProfileService::class.java)

    @Provides
    @Singleton
    fun provideQuestionService(retrofit: Retrofit): QuestionService =
        retrofit.create(QuestionService::class.java)
    @Provides
    @Singleton

    fun provideAuthService(retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)
}