package com.example.com_us.data.repository

import com.example.com_us.base.data.BaseResponse
import com.example.com_us.data.model.modify.ModifyRequest
import com.example.com_us.data.model.modify.ModifyResponse

interface ModifyRepository {
    suspend fun getModifyAnswer(body : ModifyRequest) : Result<ModifyResponse>
}