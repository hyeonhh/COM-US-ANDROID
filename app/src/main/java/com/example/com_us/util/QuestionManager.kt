package com.example.com_us.util

import com.example.com_us.data.model.question.response.question.ResponseAnswerDetailDto

object QuestionManager {
    var questionId: Long = -1
    var question: String = ""
    var signLanguageInfo: List<ResponseAnswerDetailDto> = listOf()
    var answerDate: String = ""

    fun reset() {
        questionId = -1
        question = ""
        signLanguageInfo = listOf()
        answerDate = ""
    }
}