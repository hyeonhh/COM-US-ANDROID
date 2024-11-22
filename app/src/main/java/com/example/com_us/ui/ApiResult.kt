package com.example.com_us.ui


// 성공 / 실패에 따른 UI 처리
sealed class ApiResult<out T> {
    data object Initial : ApiResult<Nothing>()
    data class Success<T>(val data : T) : ApiResult<T>()
    data class Error(val message : String) : ApiResult<Nothing>()
}
