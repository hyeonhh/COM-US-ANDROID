package com.example.com_us.data.response.question

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponsePreviousAnswerDto(
    @SerialName("question")
    val question: QuestionPrevious,
    @SerialName("answerList")
    val answerList: List<Answer>
)

@Serializable
data class QuestionPrevious(
    @SerialName("id")
    val id: Long,
    @SerialName("category")
    val category: String,
    @SerialName("answerType")
    val answerType: String,
    @SerialName("questionContent")
    val questionContent: String,
    @SerialName("questionCount")
    val questionCount: Int
)

@Serializable
data class Answer(
    @SerialName("id")
    val id: Long,
    @SerialName("answerContent")
    val answerContent: String,
    @SerialName("questionId")
    val questionId: Long,
    @SerialName("createdAt")
    val  createdAt: String
)