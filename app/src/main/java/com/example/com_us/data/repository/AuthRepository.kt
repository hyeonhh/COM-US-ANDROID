package com.example.com_us.data.repository

import com.example.com_us.base.data.BaseResponse
import com.example.com_us.data.model.auth.LoginResponse

interface AuthRepository {
        suspend fun login(code : String) : Result<LoginResponse>
}