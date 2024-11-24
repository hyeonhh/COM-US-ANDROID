package com.example.com_us.data.model.home

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class ResponseHomeDataDto(
    @SerialName("user")
    val user: User,
    @SerialName("category")
    val category: Category,
    @SerialName("block")
    val block: List<Block>,
)

@Serializable
data class User(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("imageUrl")
    val imageUrl: String,
    @SerialName("todayChatTime")
    val todayChatTime: String,
)

@Serializable
data class Category(
    @SerialName("DailyCount")
    val DailyCount: Int,
    @SerialName("DailyTotalCount")
    val DailyTotalCount: Int,
    @SerialName("DailyPercent")
    val DailyPercent: Int,

    @SerialName("SchoolCount")
    val SchoolCount: Int,
    @SerialName("SchoolTotalCount")
    val SchoolTotalCount: Int,
    @SerialName("SchoolPercent")
    val SchoolPercent: Int,

    @SerialName("FriendCount")
    val FriendCount: Int,
    @SerialName("FriendTotalCount")
    val FriendTotalCount: Int,
    @SerialName("FriendPercent")
    val FriendPercent: Int,

    @SerialName("FamilyCount")
    val FamilyCount: Int,
    @SerialName("FamilyTotalCount")
    val FamilyTotalCount: Int,
    @SerialName("FamilyPercent")
    val FamilyPercent: Int,

    @SerialName("HobbyCount")
    val HobbyCount: Int,
    @SerialName("HobbyTotalCount")
    val HobbyTotalCount: Int,
    @SerialName("HobbyPercent")
    val HobbyPercent: Int,

    @SerialName("RandomCount")
    val RandomCount: Int,
    @SerialName("RandomTotalCount")
    val RandomTotalCount: Int,
    @SerialName("RandomPercent")
    val RandomPercent: Int,
)

@Serializable
data class Block(
    @SerialName("id")
    val id: Long,
    @SerialName("level")
    val level: Int,
    @SerialName("category")
    val category: String,
    @SerialName("blockRow")
    val blockRow: Int,
    @SerialName("blockColumn")
    val blockColumn: Int,
)