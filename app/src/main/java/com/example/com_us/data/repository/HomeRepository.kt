package com.example.com_us.data.repository

import com.example.com_us.data.model.home.ResponseHomeDataDto

interface HomeRepository{
    suspend fun getHomeData(): Result<ResponseHomeDataDto>
}