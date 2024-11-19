package com.example.com_us.data.source

import com.example.com_us.data.model.BaseResponse
import com.example.com_us.data.model.home.ResponseHomeDataDto

interface HomeDataSource {
    suspend fun getHomeData() : BaseResponse<ResponseHomeDataDto>
}