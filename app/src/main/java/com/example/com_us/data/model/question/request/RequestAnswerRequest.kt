package com.example.com_us.data.model.question.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

@Serializable
data class RequestAnswerRequest(
    @SerialName("questionId")
    val questionId: Int,
    @SerialName("answerContent")
    val answerContent: String,
    @SerialName("answerType")
    val answerType : String,

)