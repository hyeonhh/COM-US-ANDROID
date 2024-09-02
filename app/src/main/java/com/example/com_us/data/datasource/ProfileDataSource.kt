package com.example.com_us.data.datasource

import com.example.com_us.data.response.BaseResponse
import com.example.com_us.data.response.question.ResponseProfileDto

interface ProfileDataSource {
    suspend fun getProfileData() : BaseResponse<ResponseProfileDto>
}