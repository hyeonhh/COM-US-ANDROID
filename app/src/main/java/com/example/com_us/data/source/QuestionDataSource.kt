package com.example.com_us.data.source

import com.example.com_us.data.model.question.request.RequestAnswerRequest
import com.example.com_us.base.data.BaseResponse
import com.example.com_us.data.model.question.response.question.ResponseAnswerDetailDto
import com.example.com_us.data.model.question.response.question.ResponseAnswerDetailWithDateDto
import com.example.com_us.data.model.question.response.question.ResponsePreviousAnswerDto
import com.example.com_us.data.model.question.response.question.ResponseQuestionDetailDto
import com.example.com_us.data.model.question.response.question.ResponseQuestionDto

interface QuestionDataSource {
    suspend fun getQuestionListByCate(category: String) : BaseResponse<List<ResponseQuestionDto>>
    suspend fun getQuestionDetail(questionId: Long) : BaseResponse<ResponseQuestionDetailDto>
    suspend fun getAnswerDetail(answer: String) : BaseResponse<List<ResponseAnswerDetailDto>>
    suspend fun postAnswer(body: RequestAnswerRequest) : BaseResponse<ResponseAnswerDetailWithDateDto>
    suspend fun getPreviousAnswer(questionId: Long) : BaseResponse<ResponsePreviousAnswerDto>
}