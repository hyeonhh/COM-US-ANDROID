package com.example.com_us.data.datasource

import com.example.com_us.data.request.question.RequestAnswerDto
import com.example.com_us.data.response.BaseResponse
import com.example.com_us.data.response.BaseResponseNoData
import com.example.com_us.data.response.question.ResponseAnswerDetailDto
import com.example.com_us.data.response.question.ResponseQuestionDetailDto
import com.example.com_us.data.response.question.ResponseQuestionDto
import com.example.com_us.data.service.QuestionService

class QuestionRemoteDataSource(private val questionService : QuestionService) : QuestionDataSource {
    override suspend fun getQuestionListByCate(category: String): BaseResponse<List<ResponseQuestionDto>> {
        return questionService.getQuestionListByCate(category)
    }
    override suspend fun getQuestionDetail(questionId: Long): BaseResponse<ResponseQuestionDetailDto> {
        return questionService.getQuestionDetail(questionId)
    }
    override suspend fun getAnswerDetail(answer: String): BaseResponse<List<ResponseAnswerDetailDto>> {
        return questionService.getAnswerDetail(answer)
    }
    override suspend fun postAnswer(body: RequestAnswerDto): BaseResponseNoData {
        return questionService.postAnswer(body)
    }
}