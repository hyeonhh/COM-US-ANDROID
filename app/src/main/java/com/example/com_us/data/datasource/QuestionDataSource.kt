package com.example.com_us.data.datasource

import com.example.com_us.data.response.BaseResponse
import com.example.com_us.data.response.question.ResponseQuestionDto

interface QuestionDataSource {
    suspend fun getQuestionListByCate(category: String) : BaseResponse<List<ResponseQuestionDto>>
}