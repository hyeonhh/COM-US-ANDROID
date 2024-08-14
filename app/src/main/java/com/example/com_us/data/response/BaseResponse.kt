package com.example.com_us.data.response

import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    val status: Int,
    val message: String,
    val data: T? = null,
)

@Serializable
data class BaseResponseNoData(
    val status: Int,
    val message: String,
)
