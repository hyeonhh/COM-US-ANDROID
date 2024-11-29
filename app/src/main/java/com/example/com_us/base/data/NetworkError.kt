package com.example.com_us.base.data

sealed class NetworkError : Exception(){
    //todo : 이후 로그인 토큰 만료 추가하기
    // HTTP 예외 처리 (40x, 50x 예외)
    data class HttpException(
        override val message : String,
       // val errorBody : String? = null
    ) : NetworkError()

    // 네트워크 연결 오류 등
    data class  IOException(
        override val message : String,
        override val cause: Throwable? = null,  // 원인이 되는 예외
    ) : NetworkError()

    data class NullDataError(
        override val message : String
    ) : NetworkError()

}
