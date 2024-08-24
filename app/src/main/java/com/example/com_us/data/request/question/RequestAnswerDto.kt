package com.example.com_us.data.request.question

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestAnswerDto(
    @SerialName("questionId")
    val questionId: Long,
    @SerialName("answerContent")
    val answerContent: String,
)