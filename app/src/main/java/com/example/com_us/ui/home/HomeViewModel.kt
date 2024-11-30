package com.example.com_us.ui.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.com_us.base.data.NetworkError
import com.example.com_us.base.viewmodel.BaseViewModel
import com.example.com_us.data.repository.HomeRepository
import com.example.com_us.data.model.home.ResponseHomeDataDto
import com.example.com_us.ui.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository) : BaseViewModel() {
    private val _homeUiState= MutableStateFlow<UiState<ResponseHomeDataDto>>(UiState.Loading)
    val homeUiState = _homeUiState.asStateFlow()

    init {
        loadHomeData()
    }

    fun loadHomeData(){
        viewModelScope.launch(Dispatchers.IO) {
            homeRepository.getHomeData()
            .onSuccess {
                    _homeUiState.value = UiState.Success(it)
                }
                .onFailure {
                    val errorMessage = when(it) {
                        is NetworkError.IOException -> { "연결된 네트워크 확인 후 다시 시도해주세요!" }
                        is NetworkError.HttpException -> {
                            Log.e("home api failed",it.message.toString())
                            "에러가 발생했습니다! 잠시 후에 다시 시도해주세요"
                        }
                        else -> "알 수 없는 에러가 발생했습니다. 다시 시도해주세요!"
                    }
                    _homeUiState.value = UiState.Error(errorMessage)
                }
        }
    }



}