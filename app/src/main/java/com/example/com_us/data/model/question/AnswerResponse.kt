package com.example.com_us.data.model.question

import kotlinx.serialization.Serializable


@Serializable
data class Data(
    val answerDate:String,
    val answers : List<Answer>
)

@Serializable
data class Answer(
    val answerTime : String,
    val category : String,
    val answerType: String,
    val questionContent:String,
    val answerContent : String,
)