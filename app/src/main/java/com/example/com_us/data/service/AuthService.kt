package com.example.com_us.data.service

import com.example.com_us.base.data.BaseResponse
import com.example.com_us.data.model.auth.LoginResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface AuthService {
    @GET("api/user/google-login?code={code}")
    suspend fun login(
        @Path(value = "code") code:String) : BaseResponse<LoginResponse>
}