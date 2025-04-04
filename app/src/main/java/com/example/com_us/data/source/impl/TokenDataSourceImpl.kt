package com.example.com_us.data.source.impl

import com.example.com_us.base.data.BaseResponse
import com.example.com_us.data.model.auth.LoginResponse
import com.example.com_us.data.service.TokenService
import com.example.com_us.data.source.TokenDataSource
import javax.inject.Inject

class TokenDataSourceImpl @Inject constructor(
    private val tokenService: TokenService,
): TokenDataSource {
    override suspend fun reissueToken(body: LoginResponse): BaseResponse<LoginResponse> {
       return tokenService.reissueToken(body)
    }
}