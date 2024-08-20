package com.example.com_us.data.repository

import com.example.com_us.data.datasource.HomeRemoteDataSource
import com.example.com_us.data.response.home.ResponseHomeDataDto

class HomeRepository(
    private val homeRemoteDataSource: HomeRemoteDataSource
) {
    suspend fun getHomeData(): Result<ResponseHomeDataDto> {
        return runCatching { homeRemoteDataSource.getHomeData().data!! }
    }
}