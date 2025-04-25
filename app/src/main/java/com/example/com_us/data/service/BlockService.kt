package com.example.com_us.data.service

import com.example.com_us.base.data.BaseResponse
import com.example.com_us.data.model.block.BlockCountResponse
import com.example.com_us.data.model.block.SaveBlockRequest
import com.example.com_us.data.model.home.Block
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface BlockService {
    @GET("/api/block/available-count")
    suspend fun getAvailableCount()  : BaseResponse<BlockCountResponse>

    @POST("/api/block")
    suspend fun saveBlock(
        @Body body : SaveBlockRequest) : BaseResponse<Unit>

    @GET("/api/block")
    suspend fun getBlock() : BaseResponse<Block>
    @DELETE("/api/block")
    suspend fun deleteBlock() : BaseResponse<Unit>
}