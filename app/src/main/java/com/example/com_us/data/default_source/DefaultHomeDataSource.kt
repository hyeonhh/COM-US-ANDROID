package com.example.com_us.data.default_source

import com.example.com_us.base.data.BaseResponse
import com.example.com_us.data.model.home.ResponseHomeDataDto
import com.example.com_us.data.service.HomeService
import com.example.com_us.data.source.HomeDataSource
import javax.inject.Inject

class DefaultHomeDataSource @Inject constructor(private val homeService : HomeService) : HomeDataSource {
    override suspend fun getHomeData(): BaseResponse<ResponseHomeDataDto> {
        return homeService.getHomeData()
    }
}