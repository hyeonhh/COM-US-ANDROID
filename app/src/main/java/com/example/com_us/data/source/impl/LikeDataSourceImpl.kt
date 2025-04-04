package com.example.com_us.data.source.impl

import com.example.com_us.base.data.BaseResponse
import com.example.com_us.data.service.LikeService
import com.example.com_us.data.source.LikeDataSource
import javax.inject.Inject

class LikeDataSourceImpl @Inject constructor(
    private val likeService : LikeService
) : LikeDataSource {
    override suspend fun setLike(questionId : String): BaseResponse<Unit> {
       return likeService.setLike(questionId)
    }

    override suspend fun setUnlike(questionId :String): BaseResponse<Unit> {
       return likeService.setUnLike(questionId)
    }

}