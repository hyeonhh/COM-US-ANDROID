package com.example.com_us.data.model.question.response.question

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponsePreviousAnswerDto(
    @SerialName("questionId")
    val questionId: Long,
    @SerialName("category")
    val category: String,
    @SerialName("questionAnswerType")
    val questionAnswerType: String,
    @SerialName("questionContent")
    val questionContent: String,
    @SerialName("answerCount")
    val answerCount: Int,
    @SerialName("answers")
    val answers : List<PreviousAnswer>
)


@Serializable
data class PreviousAnswer(
    @SerialName("answerId")
    val id: Long,
    @SerialName("answerDate")
    val answerDate: String,
    @SerialName("answerType")
    val answerType: String,
    @SerialName("answerContent")
    val  answerContent: String
)