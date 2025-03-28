package com.example.com_us.data.model.question.request

import kotlinx.serialization.Serializable

@Serializable
data class DetailQuestionRequest(
    val isRandom : Boolean ,
    val questionId : Int,
)
