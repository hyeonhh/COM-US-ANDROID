package com.example.com_us.data.default_source

import com.example.com_us.data.model.question.request.RequestAnswerRequest
import com.example.com_us.base.data.BaseResponse
import com.example.com_us.data.model.question.response.question.ResponseAnswerDetailDto
import com.example.com_us.data.model.question.response.question.ResponseAnswerDetailWithDateDto
import com.example.com_us.data.model.question.response.question.ResponsePreviousAnswerDto
import com.example.com_us.data.model.question.response.question.ResponseQuestionDetailDto
import com.example.com_us.data.model.question.response.question.ResponseQuestionDto
import com.example.com_us.data.service.QuestionService
import com.example.com_us.data.source.QuestionDataSource
import javax.inject.Inject

class DefaultQuestionDataSource @Inject constructor(private val questionService : QuestionService) :
    QuestionDataSource {
    override suspend fun getQuestionListByCate(category: String): BaseResponse<List<ResponseQuestionDto>> {
        return questionService.getQuestionListByCate(category)
    }
    override suspend fun getQuestionDetail(questionId: Long): BaseResponse<ResponseQuestionDetailDto> {
        return questionService.getQuestionDetail(questionId)
    }
    override suspend fun getAnswerDetail(answer: String): BaseResponse<List<ResponseAnswerDetailDto>> {
        return questionService.getAnswerDetail(answer)
    }
    override suspend fun postAnswer(body: RequestAnswerRequest): BaseResponse<ResponseAnswerDetailWithDateDto> {
        return questionService.postAnswer(body)
    }
    override suspend fun getPreviousAnswer(questionId: Long): BaseResponse<ResponsePreviousAnswerDto> {
        return questionService.getPreviousAnswer(questionId)
    }
}