package com.example.com_us.data.repository.impl

import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import com.example.com_us.base.data.BaseResponse
import com.example.com_us.base.data.NetworkError
import com.example.com_us.base.data.toResult
import com.example.com_us.data.model.modify.ModifyRequest
import com.example.com_us.data.model.modify.ModifyResponse
import com.example.com_us.data.repository.ModifyRepository
import com.example.com_us.data.source.ModifyDataSource
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

class ModifyRepositoryImpl @Inject constructor(
    private val modifyDataSource: ModifyDataSource
): ModifyRepository {
    override suspend fun getModifyAnswer(body: ModifyRequest): Result<ModifyResponse> {
        return try {
            modifyDataSource.getModifyAnswer(body).toResult()
        } catch (e: Exception) {
            Result.failure(NetworkError.HttpException(e.message.toString()))
        }
    }
}