package com.example.com_us.base.data

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

fun <T> BaseResponse<T>.toResult(): Result<T> {
    return when{
        status == 200 &&  data != null -> Result.success(data)
        status == 200  && data == null -> Result.failure(NetworkError.NullDataError())
        status == 400 or 401 -> Result.failure(NetworkError.ApiError(status, message))
        status == 500 -> Result.failure(NetworkError.ApiError(status, message))
        else ->  Result.failure(NetworkError.ApiError(status, message))
    }
}