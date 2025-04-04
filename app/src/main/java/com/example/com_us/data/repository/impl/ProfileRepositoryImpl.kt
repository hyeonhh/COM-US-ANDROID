package com.example.com_us.data.repository.impl

import com.example.com_us.base.data.NetworkError
import com.example.com_us.data.model.question.response.question.ResponseProfileDto
import com.example.com_us.data.repository.ProfileRepository
import com.example.com_us.data.source.impl.DefaultProfileDataSource
import com.example.com_us.base.data.toResult
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileRemoteDataSource: DefaultProfileDataSource
)  : ProfileRepository {
   override suspend fun getProfileData(): Result<ResponseProfileDto> {
        return try {
            profileRemoteDataSource.getProfileData().toResult()
        }catch (e: Exception){
            Result.failure(NetworkError.HttpException(e.message.toString()))
        }
   }
}