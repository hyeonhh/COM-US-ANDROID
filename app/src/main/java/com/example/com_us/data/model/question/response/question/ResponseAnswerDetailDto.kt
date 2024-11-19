package com.example.com_us.data.model.question.response.question

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class ResponseAnswerDetailDto(
    @SerialName("id")
    val id: Long,
    @SerialName("signLanguageName")
    val signLanguageName: String,
    @SerialName("signLanguageVideoUrl")
    val signLanguageVideoUrl: String,
    @SerialName("signLanguageDescription")
    val signLanguageDescription: String,
)
