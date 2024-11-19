package com.example.com_us.data.repository

import com.example.com_us.data.model.question.request.RequestAnswerRequest
import com.example.com_us.data.model.question.response.question.ResponseAnswerDetailDto
import com.example.com_us.data.model.question.response.question.ResponseAnswerDetailWithDateDto
import com.example.com_us.data.model.question.response.question.ResponsePreviousAnswerDto
import com.example.com_us.data.model.question.response.question.ResponseQuestionDetailDto
import com.example.com_us.data.model.question.response.question.ResponseQuestionDto

interface QuestionRepository {
    suspend fun getQuestionListByCate(category: String): Result<List<ResponseQuestionDto>>
    suspend fun getQuestionDetail(questionId: Long): Result<ResponseQuestionDetailDto>
    suspend fun getAnswerDetail(answer: String): Result<List<ResponseAnswerDetailDto>>
    suspend fun postAnswer(body: RequestAnswerRequest): Result<ResponseAnswerDetailWithDateDto>
    suspend fun getPreviousAnswer(questionId: Long ): Result<ResponsePreviousAnswerDto>
}