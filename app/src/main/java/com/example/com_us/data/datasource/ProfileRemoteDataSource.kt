package com.example.com_us.data.datasource

import com.example.com_us.data.response.BaseResponse
import com.example.com_us.data.response.question.ResponseProfileDto
import com.example.com_us.data.service.ProfileService

class ProfileRemoteDataSource(private val profileService: ProfileService) : ProfileDataSource {
    override suspend fun getProfileData(): BaseResponse<ResponseProfileDto> {
        return profileService.getProfileData()
    }
}