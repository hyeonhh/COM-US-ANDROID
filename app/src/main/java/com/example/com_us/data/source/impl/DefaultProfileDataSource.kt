package com.example.com_us.data.source.impl

import com.example.com_us.base.data.BaseResponse
import com.example.com_us.data.model.question.response.question.ResponseProfileDto
import com.example.com_us.data.service.ProfileService
import com.example.com_us.data.source.ProfileDataSource
import javax.inject.Inject

class DefaultProfileDataSource @Inject constructor(private val profileService: ProfileService) : ProfileDataSource {
    override suspend fun getProfileData(): BaseResponse<ResponseProfileDto> {
        return profileService.getProfileData()
    }
}