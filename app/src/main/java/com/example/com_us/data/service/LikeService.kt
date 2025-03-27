package com.example.com_us.data.service

import com.example.com_us.base.data.BaseResponse
import retrofit2.http.POST
import retrofit2.http.Path

interface LikeService {

    @POST("/api/question/{question_id}/like")
    suspend fun setLike(
        @Path("question_id") questionId :String
    ) : BaseResponse<Unit>
    @POST("/api/question/{question_id}/unlike")
    suspend fun setUnLike(
        @Path("question_id") questionId : String
    ): BaseResponse<Unit>
}