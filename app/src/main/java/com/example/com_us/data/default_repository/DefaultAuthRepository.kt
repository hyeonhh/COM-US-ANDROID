package com.example.com_us.data.default_repository

import com.example.com_us.base.data.BaseResponse
import com.example.com_us.base.data.NetworkError
import com.example.com_us.base.data.toResult
import com.example.com_us.data.model.auth.LoginResponse
import com.example.com_us.data.repository.AuthRepository
import com.example.com_us.data.source.AuthDataSource
import javax.inject.Inject

class DefaultAuthRepository @Inject constructor(
    private val authDataSource: AuthDataSource
) : AuthRepository {
    override suspend fun login(code: String): Result<LoginResponse> {
        return try {
            authDataSource.login(code).toResult()
        } catch(e:Exception) {
            Result.failure(NetworkError.HttpException(e.message.toString()))
        }


    }
}