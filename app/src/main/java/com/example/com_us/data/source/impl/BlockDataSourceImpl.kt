package com.example.com_us.data.source.impl

import com.example.com_us.base.data.BaseResponse
import com.example.com_us.data.model.block.BlockCountResponse
import com.example.com_us.data.model.block.SaveBlockRequest
import com.example.com_us.data.model.home.Block
import com.example.com_us.data.service.BlockService
import com.example.com_us.data.source.BlockDataSource
import javax.inject.Inject

class BlockDataSourceImpl @Inject constructor(
    private val blockService: BlockService,
) : BlockDataSource{
    override suspend fun deleteBlock(): BaseResponse<Unit> {
        return blockService.deleteBlock()
    }

    override suspend fun saveBlock(body: SaveBlockRequest): BaseResponse<Unit> = blockService.saveBlock(body)
    override suspend fun getBlock(): BaseResponse<List<Block>> = blockService.getBlock()
    override suspend fun getBlockCount(): BaseResponse<BlockCountResponse> = blockService.getAvailableCount()
}