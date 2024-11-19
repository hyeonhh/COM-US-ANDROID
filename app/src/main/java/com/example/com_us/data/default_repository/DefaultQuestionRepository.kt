package com.example.com_us.data.default_repository

import com.example.com_us.data.model.question.request.RequestAnswerRequest
import com.example.com_us.data.model.question.response.question.ResponseAnswerDetailDto
import com.example.com_us.data.model.question.response.question.ResponseAnswerDetailWithDateDto
import com.example.com_us.data.model.question.response.question.ResponsePreviousAnswerDto
import com.example.com_us.data.model.question.response.question.ResponseQuestionDetailDto
import com.example.com_us.data.model.question.response.question.ResponseQuestionDto
import com.example.com_us.data.repository.QuestionRepository
import com.example.com_us.data.default_source.DefaultQuestionDataSource
import javax.inject.Inject

class DefaultQuestionRepository @Inject constructor(
    private val defaultQuestionDataSource: DefaultQuestionDataSource
)  : QuestionRepository {
   override suspend fun getQuestionListByCate(category: String): Result<List<ResponseQuestionDto>> {
        return runCatching { defaultQuestionDataSource.getQuestionListByCate(category).data!! }
    }
   override suspend fun getQuestionDetail(questionId: Long): Result<ResponseQuestionDetailDto> {
        return runCatching { defaultQuestionDataSource.getQuestionDetail(questionId).data!! }
    }
    override suspend fun getAnswerDetail(answer: String): Result<List<ResponseAnswerDetailDto>> {
        return runCatching { defaultQuestionDataSource.getAnswerDetail(answer).data!! }
    }
   override suspend fun postAnswer(body: RequestAnswerRequest): Result<ResponseAnswerDetailWithDateDto> {
        return runCatching { defaultQuestionDataSource.postAnswer(body).data!! }
    }
   override suspend fun getPreviousAnswer(questionId: Long ): Result<ResponsePreviousAnswerDto> {
        return runCatching { defaultQuestionDataSource.getPreviousAnswer(questionId).data!! }
    }
}