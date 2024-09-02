package com.example.com_us.data.response.question

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class ResponseProfileDto(
    @SerialName("userInfo")
    val userInfo: UserInfo,
    @SerialName("answerStatistic")
    val answerStatistic: AnswerStatistic,
)

@Serializable
data class UserInfo(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("imageUrl")
    val imageUrl: String,
    @SerialName("todayChatTime")
    val todayChatTime: String,
    @SerialName("totalChatTime")
    val totalChatTime: String,
    @SerialName("totalChatCount")
    val totalChatCount: Int,
)

@Serializable
data class AnswerStatistic(
    @SerialName("sentenceRatio")
    val sentenceRatio: Double,
    @SerialName("multipleChoiceRatio")
    val multipleChoiceRatio: Double,
    @SerialName("dailyQuestionRatio")
    val dailyQuestionRatio: Double,
    @SerialName("schoolQuestionRatio")
    val schoolQuestionRatio: Double,
    @SerialName("hobbyQuestionRatio")
    val hobbyQuestionRatio: Double,
    @SerialName("familyQuestionRatio")
    val familyQuestionRatio: Double,
    @SerialName("friendQuestionRatio")
    val friendQuestionRatio: Double,
    @SerialName("randomQuestionRatio")
    val randomQuestionRatio: Double,
)
