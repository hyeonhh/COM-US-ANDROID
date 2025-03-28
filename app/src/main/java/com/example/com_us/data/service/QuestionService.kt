package com.example.com_us.data.service

import com.example.com_us.data.model.question.request.RequestAnswerRequest
import com.example.com_us.base.data.BaseResponse
import com.example.com_us.data.model.question.request.DetailQuestionRequest
import com.example.com_us.data.model.question.response.question.ResponseAnswerDetailDto
import com.example.com_us.data.model.question.response.question.ResponseAnswerDetailWithDateDto
import com.example.com_us.data.model.question.response.question.ResponsePreviousAnswerDto
import com.example.com_us.data.model.question.response.question.ResponseQuestionDetailDto
import com.example.com_us.data.model.question.response.question.ResponseQuestionDto
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

    @GET("/api/question/detail")
    suspend fun getQuestionDetail(
        @Body body : DetailQuestionRequest,
    ): BaseResponse<ResponseQuestionDetailDto>

    @GET("/api/sign-language")
    suspend fun getAnswerDetail(
        @Query("answer") answer: String,
    ): BaseResponse<List<ResponseAnswerDetailDto>>

    @POST("/api/answer")
    suspend fun postAnswer(
    @Body body: RequestAnswerRequest,
    ): BaseResponse<ResponseAnswerDetailWithDateDto>

    @GET("/api/answer/{questionId}")
    suspend fun getPreviousAnswer(
        @Path("questionId") questionId: Long,
    ): BaseResponse<ResponsePreviousAnswerDto>
}