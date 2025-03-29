package com.example.com_us.data.source

import com.example.com_us.base.data.BaseResponse
import com.example.com_us.data.model.auth.LoginResponse

interface TokenDataSource {
    suspend fun reissueToken(body : LoginResponse): BaseResponse<LoginResponse>
}