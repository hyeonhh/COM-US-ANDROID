package com.example.com_us.data.model.question.response.question

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class ResponseQuestionDetailDto(
    @SerialName("question")
    val question: Question,
    @SerialName("answerList")
    val answerList: List<String>
)

@Serializable
data class Question(
    @SerialName("id")
    val id: Long,
    @SerialName("category")
    val category: String,
    @SerialName("answerType")
    val answerType: String,
    @SerialName("questionContent")
    val questionContent: String
)



