package com.example.com_us.data.model.block

import kotlinx.serialization.Serializable

@Serializable
data class SaveBlockRequest(
    val category : String,
    val blockPlace: List<BlockPlace>
)


@Serializable
data class BlockPlace(
    val row : Int,
    val column : Int,
)