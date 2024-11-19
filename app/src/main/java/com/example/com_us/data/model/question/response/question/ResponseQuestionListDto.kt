package com.example.com_us.data.model.question.response.question

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class ResponseQuestionDto(
    @SerialName("id")
    val id: Long,
    @SerialName("category")
    val category: String,
    @SerialName("answerType")
    val answerType: String,
    @SerialName("questionContent")
    val questionContent: String,
    @SerialName("questionCount")
    val questionCount: String,
)
