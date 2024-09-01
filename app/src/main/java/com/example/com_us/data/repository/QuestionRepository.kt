package com.example.com_us.data.repository

import com.example.com_us.data.datasource.QuestionRemoteDataSource
import com.example.com_us.data.request.question.RequestAnswerDto
import com.example.com_us.data.response.question.ResponseAnswerDetailDto
import com.example.com_us.data.response.question.ResponseAnswerDetailWithDateDto
import com.example.com_us.data.response.question.ResponsePreviousAnswerDto
import com.example.com_us.data.response.question.ResponseQuestionDetailDto
import com.example.com_us.data.response.question.ResponseQuestionDto

class QuestionRepository(
    private val questionRemoteDataSource: QuestionRemoteDataSource
) {
    suspend fun getQuestionListByCate(category: String): Result<List<ResponseQuestionDto>> {
        return runCatching { questionRemoteDataSource.getQuestionListByCate(category).data!! }
    }
    suspend fun getQuestionDetail(questionId: Long): Result<ResponseQuestionDetailDto> {
        return runCatching { questionRemoteDataSource.getQuestionDetail(questionId).data!! }
    }
    suspend fun getAnswerDetail(answer: String): Result<List<ResponseAnswerDetailDto>> {
        return runCatching { questionRemoteDataSource.getAnswerDetail(answer).data!! }
    }
    suspend fun postAnswer(body: RequestAnswerDto): Result<ResponseAnswerDetailWithDateDto> {
        return runCatching { questionRemoteDataSource.postAnswer(body).data!! }
    }
    suspend fun getPreviousAnswer(questionId: Long ): Result<ResponsePreviousAnswerDto> {
        return runCatching { questionRemoteDataSource.getPreviousAnswer(questionId).data!! }
    }
}