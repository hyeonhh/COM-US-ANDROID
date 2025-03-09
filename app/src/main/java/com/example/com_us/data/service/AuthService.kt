package com.example.com_us.data.service

import com.example.com_us.base.data.BaseResponse
import com.example.com_us.data.model.auth.LoginRequest
import com.example.com_us.data.model.auth.LoginResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthService {
    @POST("/api/user/login")
    @Headers("NO-AUTH: true")
    suspend fun login(@Body request:LoginRequest) : BaseResponse<LoginResponse>
}