package com.example.com_us.data.network.interceptor.di

import com.example.com_us.base.AppInterceptor
import com.example.com_us.data.di.AuthInterceptor
import com.example.com_us.data.repository.UserTokenRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import javax.inject.Provider

@Module
@InstallIn(SingletonComponent::class)
object InterceptorModule {
    @AuthInterceptor
    @Provides
    fun provideAuthInterceptor(
        userTokenRepository: Provider<UserTokenRepository>,
    ): Interceptor =
        AppInterceptor(
            userTokenRepository,
        )
}