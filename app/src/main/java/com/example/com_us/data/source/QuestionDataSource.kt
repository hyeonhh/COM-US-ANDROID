package com.example.com_us.data.source

import com.example.com_us.data.model.question.request.RequestAnswerRequest
import com.example.com_us.base.data.BaseResponse
import com.example.com_us.data.model.question.Data
import com.example.com_us.data.model.question.request.DetailQuestionRequest
import com.example.com_us.data.model.question.response.question.ResponseAnswerDetailDto
import com.example.com_us.data.model.question.response.question.ResponseAnswerDetailWithDateDto
import com.example.com_us.data.model.question.response.question.ResponsePreviousAnswerDto
import com.example.com_us.data.model.question.response.question.ResponseQuestionDetailDto
import com.example.com_us.data.model.question.response.question.ResponseQuestionDto

interface QuestionDataSource {
    suspend fun getQuestionListByCate(category: String,sort:String) : BaseResponse<List<ResponseQuestionDto>>
    suspend fun getQuestionDetail(body: DetailQuestionRequest) : BaseResponse<ResponseQuestionDetailDto>
    suspend fun getAnswerDetail(answer: String) : BaseResponse<List<ResponseAnswerDetailDto>>
    suspend fun postAnswer(body: RequestAnswerRequest) : BaseResponse<ResponseAnswerDetailWithDateDto>
    suspend fun getAnswer(): BaseResponse<List<Data>>
    suspend fun getPreviousAnswer(questionId: Int) : BaseResponse<ResponsePreviousAnswerDto>
}