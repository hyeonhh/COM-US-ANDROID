package com.example.com_us.data.model.home

import androidx.collection.emptyLongList
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class ResponseHomeDataDto(
    @SerialName("userInfo")
    val userInfo: User,
    @SerialName("randomQuestion")
    val randomQuestion : RandomQuestion,
    @SerialName("questionCounts")
    val questionCounts: List<QuestionCounts>,
    @SerialName("blocks")
    val blockBoard: Block,
)

@Serializable
data class RandomQuestion(
    val questionId:Int = 0,
    val questionContent : String = "",
    val category : String = "",
    val answerType : String= "",
)

@Serializable
data class User(
    @SerialName("name")
    val name: String,
    @SerialName("imageUrl")
    val imageUrl: String,
    @SerialName("answerCount")
    val answerCount : Int,
    @SerialName("likeCount")
    val likeCount : Int,
    @SerialName("week")
    val week : String,
    @SerialName("weeklyAnswer")
    val weeklyAnswer: List<Answer>,
)

@Serializable
data class Answer(
    val answerDay : String,
    val category : String,
)

@Serializable
data class QuestionCounts(
    val category : String,
    val questionTotalCount : Int,
    val questionAnsweredCount : Int,
    val count : String,
    val percentage : String,
)

@Serializable
data class Block(
    val level : Int = 0,
    val blocks: List<Question> = emptyList(),

)

@Serializable
data class Question(
    val questionId: Int =0,
    val category : String = "",
    val questionContent : String = "",
    val answerId : Int = 0,
    val answerType : String = "",
    val answerContent : String = "",
    val blockPlace:List<BlockPlace> = emptyList<BlockPlace>(),
)
@Serializable
data class BlockPlace(
    val blockId :Long,
    val row:Int,
    val col : Int,
)