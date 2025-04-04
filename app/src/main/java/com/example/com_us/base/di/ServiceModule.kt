package com.example.com_us.base.di

import android.content.Context
import com.example.com_us.R
import com.example.com_us.data.di.BaseUrl
import com.example.com_us.data.di.ModifyBaseUrl
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @BaseUrl
    fun provideBaseUrl(
        @ApplicationContext context: Context
    ) : String = context.getString(R.string.baseUrl)

    @Provides
    @ModifyBaseUrl
    fun provideModifyBaseUrl(
        @ApplicationContext context: Context
    ):String = context.getString(R.string.modify_base_url)

}