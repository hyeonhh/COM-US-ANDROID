package com.example.com_us.data.repository

import com.example.com_us.base.data.BaseResponse
import com.example.com_us.data.model.block.BlockCountResponse
import com.example.com_us.data.model.block.SaveBlockRequest
import com.example.com_us.data.model.home.Block

interface BlockRepository {
    suspend fun getAvailableCount()  : BaseResponse<BlockCountResponse>
    suspend fun saveBlock(body : SaveBlockRequest) : BaseResponse<Unit>
    suspend fun getBlock() : Result<List<Block>>
    suspend fun deleteBlock() : BaseResponse<Unit>
}