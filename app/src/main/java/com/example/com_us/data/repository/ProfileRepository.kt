package com.example.com_us.data.repository

import com.example.com_us.data.model.question.response.question.ResponseProfileDto

interface ProfileRepository {
    suspend fun getProfileData(): Result<ResponseProfileDto>
}