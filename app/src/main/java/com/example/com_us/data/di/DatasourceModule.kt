package com.example.com_us.data.di

import com.example.com_us.data.default_source.DefaultAuthDataSource
import com.example.com_us.data.default_source.DefaultHomeDataSource
import com.example.com_us.data.default_source.DefaultProfileDataSource
import com.example.com_us.data.default_source.DefaultQuestionDataSource
import com.example.com_us.data.source.AuthDataSource
import com.example.com_us.data.source.HomeDataSource
import com.example.com_us.data.source.ProfileDataSource
import com.example.com_us.data.source.QuestionDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface DatasourceModule {

    @Binds
    fun provideHomeDataSource(defaultDataSource : DefaultHomeDataSource) : HomeDataSource

    @Binds
    fun provideProfileDataSource(defaultDataSource : DefaultProfileDataSource) : ProfileDataSource

    @Binds
    fun provideQuestionDataSource(defaultDataSource : DefaultQuestionDataSource) : QuestionDataSource

    @Binds
    fun provideAuthDataSource(defaultDataSource : DefaultAuthDataSource) : AuthDataSource


}