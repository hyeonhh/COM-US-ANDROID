package com.example.com_us.data.datasource

import com.example.com_us.data.response.BaseResponse
import com.example.com_us.data.response.question.ResponseHomeDataDto

interface HomeDataSource {
    suspend fun getHomeData() : BaseResponse<ResponseHomeDataDto>
}