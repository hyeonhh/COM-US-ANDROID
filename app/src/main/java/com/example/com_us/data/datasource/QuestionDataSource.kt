package com.example.com_us.data.datasource

import com.example.com_us.data.request.question.RequestAnswerDto
import com.example.com_us.data.response.BaseResponse
import com.example.com_us.data.response.BaseResponseNoData
import com.example.com_us.data.response.question.ResponseAnswerDetailDto
import com.example.com_us.data.response.question.ResponseAnswerDetailWithDateDto
import com.example.com_us.data.response.question.ResponsePreviousAnswerDto
import com.example.com_us.data.response.question.ResponseQuestionDetailDto
import com.example.com_us.data.response.question.ResponseQuestionDto

interface QuestionDataSource {
    suspend fun getQuestionListByCate(category: String) : BaseResponse<List<ResponseQuestionDto>>
    suspend fun getQuestionDetail(questionId: Long) : BaseResponse<ResponseQuestionDetailDto>
    suspend fun getAnswerDetail(answer: String) : BaseResponse<List<ResponseAnswerDetailDto>>
    suspend fun postAnswer(body: RequestAnswerDto) : BaseResponse<ResponseAnswerDetailWithDateDto>
    suspend fun getPreviousAnswer(questionId: Long) : BaseResponse<ResponsePreviousAnswerDto>
}