package com.example.com_us.data.service

import com.example.com_us.data.response.BaseResponse
import com.example.com_us.data.response.home.ResponseHomeDataDto
import retrofit2.http.GET

interface HomeService {
    @GET("/api/user")
    suspend fun getHomeData(
    ): BaseResponse<ResponseHomeDataDto>
}