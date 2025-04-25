package com.example.com_us.data.source

import com.example.com_us.base.data.BaseResponse
import com.example.com_us.data.model.block.BlockCountResponse
import com.example.com_us.data.model.block.SaveBlockRequest
import com.example.com_us.data.model.home.Block

interface BlockDataSource {
    suspend fun getBlockCount() : BaseResponse<BlockCountResponse>
    suspend fun saveBlock(body : SaveBlockRequest) : BaseResponse<Unit>
    suspend fun getBlock() : BaseResponse<Block>
    suspend fun deleteBlock() : BaseResponse<Unit>
}