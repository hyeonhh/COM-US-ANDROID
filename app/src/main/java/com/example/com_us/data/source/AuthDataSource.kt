package com.example.com_us.data.source

import com.example.com_us.base.data.BaseResponse
import com.example.com_us.data.model.auth.LoginResponse

interface AuthDataSource {
    suspend fun login(code : String) :BaseResponse<LoginResponse>
}