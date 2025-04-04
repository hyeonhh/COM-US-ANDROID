package com.example.com_us.data.repository.impl

import com.example.com_us.base.data.BaseResponse
import com.example.com_us.base.data.NetworkError
import com.example.com_us.base.data.toResult
import com.example.com_us.data.model.block.BlockCountResponse
import com.example.com_us.data.model.block.SaveBlockRequest
import com.example.com_us.data.model.home.Block
import com.example.com_us.data.repository.BlockRepository
import com.example.com_us.data.source.BlockDataSource
import javax.inject.Inject

class BlockRepositoryImpl @Inject constructor(
    private val blockDataSource: BlockDataSource,
): BlockRepository {
    override suspend fun deleteBlock(): BaseResponse<Unit> {
        return blockDataSource.deleteBlock()
    }
    override suspend fun saveBlock(body: SaveBlockRequest): BaseResponse<Unit> {
        return blockDataSource.saveBlock(body)
    }

    override suspend fun getBlock(): Result<List<Block>> {
        return  try {
            blockDataSource.getBlock().toResult()
        }catch (e:Exception){
            Result.failure(NetworkError.HttpException(e.message.toString()))
        }
    }

    override suspend fun getAvailableCount(): BaseResponse<BlockCountResponse> {
       return  blockDataSource.getBlockCount()
    }
}