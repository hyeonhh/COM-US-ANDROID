package com.example.com_us.ui.base


// 성공 / 실패에 따른 UI 처리
sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data : T) : UiState<T>()
    data class Error(val message : String) : UiState<Nothing>()
}
