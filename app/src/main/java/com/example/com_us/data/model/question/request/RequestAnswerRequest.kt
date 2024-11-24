package com.example.com_us.data.model.question.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestAnswerRequest(
    @SerialName("questionId")
    val questionId: Long,
    @SerialName("answerContent")
    val answerContent: String,
)