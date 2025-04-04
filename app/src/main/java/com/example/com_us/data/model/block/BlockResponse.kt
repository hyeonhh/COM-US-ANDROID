package com.example.com_us.data.model.block

data class BlockResponse(
    val level : Int,
    val questionId : Long,
    val category : String,
    val questionContent : String,
    val answerId : Int,
    val answerType : String,
    val answerContent : String,
)
