package com.example.com_us.data.service

import com.example.com_us.data.model.BaseResponse
import com.example.com_us.data.model.question.response.question.ResponseProfileDto
import retrofit2.http.GET

interface ProfileService {
    @GET("/api/user/mypage")
    suspend fun getProfileData(
    ): BaseResponse<ResponseProfileDto>
}