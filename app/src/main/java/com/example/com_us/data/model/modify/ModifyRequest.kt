package com.example.com_us.data.model.modify

import kotlinx.serialization.Serializable

@Serializable
data class ModifyRequest(
    val sentence: String,
)
