package com.example.com_us.data.source

import com.example.com_us.base.data.BaseResponse

interface LikeDataSource {
    suspend fun setLike(questionId: String) : BaseResponse<Unit>
    suspend fun setUnlike(questionId: String) : BaseResponse<Unit>
}