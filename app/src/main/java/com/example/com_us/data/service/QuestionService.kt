package com.example.com_us.data.service

import com.example.com_us.data.request.question.RequestAnswerDto
import com.example.com_us.data.response.BaseResponse
import com.example.com_us.data.response.BaseResponseNoData
import com.example.com_us.data.response.question.ResponseAnswerDetailDto
import com.example.com_us.data.response.question.ResponseQuestionDetailDto
import com.example.com_us.data.response.question.ResponseQuestionDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface QuestionService {
    @GET("/api/question")
    suspend fun getQuestionListByCate(
        @Query("category") category: String,
    ): BaseResponse<List<ResponseQuestionDto>>

    @GET("/api/question/{question_id}/multiple-choice")
    suspend fun getQuestionDetail(
        @Path("question_id") questionId: Long,
    ): BaseResponse<ResponseQuestionDetailDto>

    @GET("/api/sign-language")
    suspend fun getAnswerDetail(
        @Query("answer") answer: String,
    ): BaseResponse<List<ResponseAnswerDetailDto>>

    @POST("/api/answer")
    suspend fun postAnswer(
        @Body body: RequestAnswerDto,
    ): BaseResponseNoData
}