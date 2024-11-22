package com.example.com_us.data.default_repository

import com.example.com_us.data.model.home.ResponseHomeDataDto
import com.example.com_us.data.repository.HomeRepository
import com.example.com_us.data.default_source.DefaultHomeDataSource
import com.example.com_us.data.model.BaseResponse
import com.example.com_us.data.model.toResult
import javax.inject.Inject



sealed class NetworkError : Exception(){
    data class ApiError(
        val statusCode : Int,
        override val message : String
    ) : NetworkError()

    data class  NetworkException(
        override val cause : Throwable,
    ) : NetworkError()

    data class NullDataError(
        override val message : String = "Data is null"
    ) : NetworkError()

}


class DefaultHomeRepository @Inject constructor(
    private val defaultHomeDataSource: DefaultHomeDataSource
) : HomeRepository {
    override suspend fun getHomeData(): Result<ResponseHomeDataDto> {
        return try {
            defaultHomeDataSource.getHomeData().toResult()
        } catch (e: Exception) {
            Result.failure(NetworkError.NetworkException(e))
        }
    }
}