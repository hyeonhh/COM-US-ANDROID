package com.example.com_us.data.datasource

import com.example.com_us.data.response.BaseResponse
import com.example.com_us.data.response.question.ResponseHomeDataDto
import com.example.com_us.data.service.HomeService

class HomeRemoteDataSource(private val homeService : HomeService) : HomeDataSource {
    override suspend fun getHomeData(): BaseResponse<ResponseHomeDataDto> {
        return homeService.getHomeData()
    }
}