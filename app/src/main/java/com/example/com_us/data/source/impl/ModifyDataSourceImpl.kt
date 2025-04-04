package com.example.com_us.data.source.impl

import com.example.com_us.base.data.BaseResponse
import com.example.com_us.data.model.modify.ModifyRequest
import com.example.com_us.data.model.modify.ModifyResponse
import com.example.com_us.data.service.ModifyService
import com.example.com_us.data.source.ModifyDataSource
import javax.inject.Inject

class ModifyDataSourceImpl @Inject constructor(
    private val modifyService: ModifyService
) : ModifyDataSource{
    override suspend fun getModifyAnswer(body: ModifyRequest): BaseResponse<ModifyResponse> {
       return modifyService.getModifyAnswer(body)
    }
}