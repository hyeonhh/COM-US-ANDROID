package com.example.com_us.data.default_repository

import com.example.com_us.base.data.NetworkError
import com.example.com_us.data.model.home.ResponseHomeDataDto
import com.example.com_us.data.repository.HomeRepository
import com.example.com_us.data.default_source.DefaultHomeDataSource
import com.example.com_us.base.data.toResult
import javax.inject.Inject



class DefaultHomeRepository @Inject constructor(
    private val defaultHomeDataSource: DefaultHomeDataSource
) : HomeRepository {
    override suspend fun getHomeData(): Result<ResponseHomeDataDto> {
        return try {
            defaultHomeDataSource.getHomeData().toResult()
        } catch (e: Exception) {
            Result.failure(NetworkError.HttpException(e.message.toString()))
        }
    }
}