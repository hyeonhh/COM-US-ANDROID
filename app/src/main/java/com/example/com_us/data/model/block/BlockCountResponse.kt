package com.example.com_us.data.model.block

import kotlinx.serialization.Serializable

@Serializable
data class BlockCountResponse(
    val dailyBlockCount : Int = 0 ,
    val schoolBlockCount : Int = 0 ,
    val hobbyBlockCount : Int = 0 ,
    val familyBlockCount : Int = 0 ,
    val friendBlockCount  : Int = 0 ,
)
