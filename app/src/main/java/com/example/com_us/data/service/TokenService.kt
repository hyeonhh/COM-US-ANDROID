package com.example.com_us.data.service

import com.example.com_us.base.data.BaseResponse
import com.example.com_us.data.model.auth.LoginResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface TokenService {
    @POST("/api/user/reissue")
    @Headers("NO-AUTH: true")
    suspend fun reissueToken(
        @Body body : LoginResponse
    ) : BaseResponse<LoginResponse>
}