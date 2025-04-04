package com.example.com_us.data.di

import com.example.com_us.data.service.AuthService
import com.example.com_us.data.service.BlockService
import com.example.com_us.data.service.HomeService
import com.example.com_us.data.service.LikeService
import com.example.com_us.data.service.ModifyService
import com.example.com_us.data.service.ProfileService
import com.example.com_us.data.service.QuestionService
import com.example.com_us.data.service.TokenService
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

    @Provides
    @Singleton
    fun provideLikeService(retrofit : Retrofit) : LikeService = retrofit.create(LikeService::class.java)


    @Provides
    @Singleton
    fun provideTokenService(retrofit: Retrofit) : TokenService = retrofit.create(TokenService::class.java)

    @Provides
    @Singleton
    fun provideBlockService(retrofit: Retrofit) : BlockService = retrofit.create(BlockService::class.java)

    @Provides
    @Singleton
    fun provideModifyService(@ModifyRetrofit retrofit: Retrofit) : ModifyService = retrofit.create(ModifyService::class.java)
}