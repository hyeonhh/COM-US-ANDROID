package com.example.com_us.data.repository

import com.example.com_us.base.data.BaseResponse
import com.example.com_us.data.model.question.Data
import com.example.com_us.data.model.question.request.DetailQuestionRequest
import com.example.com_us.data.model.question.request.RequestAnswerRequest
import com.example.com_us.data.model.question.response.question.ResponseAnswerDetailDto
import com.example.com_us.data.model.question.response.question.ResponseAnswerDetailWithDateDto
import com.example.com_us.data.model.question.response.question.ResponsePreviousAnswerDto
import com.example.com_us.data.model.question.response.question.ResponseQuestionDetailDto
import com.example.com_us.data.model.question.response.question.ResponseQuestionDto

interface QuestionRepository {
    // 카테고리별 질문 리스트
    suspend fun getQuestionListByCate(category: String,sort:String): Result<List<ResponseQuestionDto>>

    // 질문 상세 보기
    suspend fun getQuestionDetail(body: DetailQuestionRequest): Result<ResponseQuestionDetailDto>

    // 답변 상세 보기
    suspend fun getAnswerDetail(answer: String): Result<List<ResponseAnswerDetailDto>>

    //
    suspend fun postAnswer(body: RequestAnswerRequest): Result<ResponseAnswerDetailWithDateDto>

    suspend fun getAnswer(): Result<List<Data>>

    // 이전 답변 보기
    suspend fun getPreviousAnswer(questionId: Int ): Result<ResponsePreviousAnswerDto>
}