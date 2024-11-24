package com.example.com_us.base.data

sealed class NetworkError : Exception(){
    data class ApiError(
        val statusCode : Int,
        override val message : String
    ) : NetworkError()

    data class  NetworkException(
        override val cause : Throwable,
    ) : NetworkError()

    data class NullDataError(
        override val message : String = "Data is null"
    ) : NetworkError()

}
