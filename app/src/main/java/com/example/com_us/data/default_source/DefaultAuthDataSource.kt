package com.example.com_us.data.default_source

import com.example.com_us.base.data.BaseResponse
import com.example.com_us.data.model.auth.LoginRequest
import com.example.com_us.data.model.auth.LoginResponse
import com.example.com_us.data.service.AuthService
import com.example.com_us.data.source.AuthDataSource
import javax.inject.Inject

class DefaultAuthDataSource  @Inject constructor(private val authService: AuthService) : AuthDataSource{
    override suspend fun login(request: LoginRequest): BaseResponse<LoginResponse> {
        return authService.login(request)
    }

}