package com.example.com_us.data.source

import com.example.com_us.base.data.BaseResponse
import com.example.com_us.data.model.modify.ModifyRequest
import com.example.com_us.data.model.modify.ModifyResponse

interface ModifyDataSource {
    suspend fun getModifyAnswer(body : ModifyRequest): BaseResponse<ModifyResponse>
}