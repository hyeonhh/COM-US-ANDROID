package com.example.com_us.data.response.question

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
    val dailyCount: Long,
    @SerialName("DailyTotalCount")
    val dailyTotalCount: Long,
    @SerialName("DailyPercent")
    val dailyPercent: Long,

    @SerialName("SchoolCount")
    val schoolCount: Long,
    @SerialName("SchoolTotalCount")
    val schoolTotalCount: Long,
    @SerialName("SchoolPercent")
    val schoolPercent: Long,

    @SerialName("FriendCount")
    val friendCount: Long,
    @SerialName("FriendTotalCount")
    val friendTotalCount: Long,
    @SerialName("FriendPercent")
    val friendPercent: Long,

    @SerialName("FamilyCount")
    val familyCount: Long,
    @SerialName("FamilyTotalCount")
    val familyTotalCount: Long,
    @SerialName("FamilyPercent")
    val familyPercent: Long,

    @SerialName("HobbyCount")
    val hobbyCount: Long,
    @SerialName("HobbyTotalCount")
    val hobbyTotalCount: Long,
    @SerialName("HobbyPercent")
    val hobbyPercent: Long,

    @SerialName("RandomCount")
    val randomCount: Long,
    @SerialName("RandomTotalCount")
    val randomTotalCount: Long,
    @SerialName("RandomPercent")
    val randomPercent: Long,
)

@Serializable
data class Block(
    @SerialName("id")
    val id: Long,
    @SerialName("level")
    val name: Long,
    @SerialName("category")
    val imageUrl: String,
    @SerialName("blockRow")
    val blockRow: Long,
    @SerialName("blockColumn")
    val blockColumn: Long,
)