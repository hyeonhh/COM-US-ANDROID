package com.example.com_us.data.response.question

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class ResponseAnswerDetailWithDateDto(
    @SerialName("signLanguageInfo")
    val signLanguageInfo: List<ResponseAnswerDetailDto>,
    @SerialName("answerDate")
    val answerDate: String,
)
