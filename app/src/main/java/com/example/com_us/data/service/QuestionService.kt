package com.example.com_us.data.service

import com.example.com_us.data.response.BaseResponse
import com.example.com_us.data.response.question.ResponseQuestionDto
import retrofit2.http.GET
import retrofit2.http.Query

interface QuestionService {
    @GET("/api/question")
    suspend fun getQuestionListByCate(
        @Query("category") category: String,
    ): BaseResponse<List<ResponseQuestionDto>>
}