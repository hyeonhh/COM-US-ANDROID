package com.example.com_us.data.repository.impl

import com.example.com_us.base.data.NetworkError
import com.example.com_us.base.data.toResult
import com.example.com_us.data.model.auth.LoginRequest
import com.example.com_us.data.model.auth.LoginResponse
import com.example.com_us.data.repository.AuthRepository
import com.example.com_us.data.source.AuthDataSource
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource
) : AuthRepository {
    override suspend fun login(request: LoginRequest): Result<LoginResponse> {
        return try {
            authDataSource.login(request).toResult()
        } catch(e:Exception) {
            Result.failure(NetworkError.HttpException(e.message.toString()))
        }


    }
}