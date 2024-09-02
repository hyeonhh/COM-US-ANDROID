package com.example.com_us.data.repository

import com.example.com_us.data.datasource.ProfileRemoteDataSource
import com.example.com_us.data.response.question.ResponseProfileDto

class ProfileRepository(
    private val profileRemoteDataSource: ProfileRemoteDataSource
) {
    suspend fun getProfileData(): Result<ResponseProfileDto> {
        return runCatching { profileRemoteDataSource.getProfileData().data!! }
    }
}