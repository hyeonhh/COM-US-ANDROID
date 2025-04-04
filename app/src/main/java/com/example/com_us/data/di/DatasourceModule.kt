package com.example.com_us.data.di

import com.example.com_us.data.source.impl.DefaultAuthDataSource
import com.example.com_us.data.source.impl.DefaultHomeDataSource
import com.example.com_us.data.source.impl.DefaultProfileDataSource
import com.example.com_us.data.source.impl.DefaultQuestionDataSource
import com.example.com_us.data.source.impl.LikeDataSourceImpl
import com.example.com_us.data.source.impl.TokenDataSourceImpl
import com.example.com_us.data.source.AuthDataSource
import com.example.com_us.data.source.BlockDataSource
import com.example.com_us.data.source.HomeDataSource
import com.example.com_us.data.source.LikeDataSource
import com.example.com_us.data.source.ModifyDataSource
import com.example.com_us.data.source.ProfileDataSource
import com.example.com_us.data.source.QuestionDataSource
import com.example.com_us.data.source.TokenDataSource
import com.example.com_us.data.source.impl.BlockDataSourceImpl
import com.example.com_us.data.source.impl.ModifyDataSourceImpl
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

    @Binds
    fun provideLikeDataSource(defaultDataSource : LikeDataSourceImpl) : LikeDataSource

    @Binds
    fun provideTokenDataSource(defaultDataSource : TokenDataSourceImpl) : TokenDataSource

    @Binds
    fun provideBlockDataSource(defaultDataSource : BlockDataSourceImpl) : BlockDataSource

    @Binds
    fun provideModifyDataSource(defaultDataSource : ModifyDataSourceImpl) : ModifyDataSource
}