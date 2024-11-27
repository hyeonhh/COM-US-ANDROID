package com.example.com_us.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.com_us.base.data.NetworkError
import com.example.com_us.base.viewmodel.BaseViewModel
import com.example.com_us.data.repository.HomeRepository
import com.example.com_us.data.model.home.ResponseHomeDataDto
import com.example.com_us.ui.base.UiState
import com.example.com_us.util.ServerResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository) : BaseViewModel() {
    private val _homeUiState= MutableStateFlow<UiState<ResponseHomeDataDto>>(UiState.Loading)
    val homeUiState = _homeUiState.asStateFlow()

    var serverResponseHandler: ServerResponseHandler? = null

    init {
        loadHomeData()
    }

    fun loadHomeData(){
        viewModelScope.launch {
            homeRepository.getHomeData()
                .onSuccess {
                    _homeUiState.value = UiState.Success(it)
                }
                .onFailure {
                    val errorMessage = when(it) {
                        is NetworkError.NetworkException -> { it.message }
                        is NetworkError.NullDataError -> { "데이터가 준비중이에요!" }
                        else -> "알 수 없는 에러가 발생했습니다. 다시 시도해주세요!"
                    }
                    if (errorMessage!= null ) _homeUiState.value = UiState.Error(errorMessage)
                }
        }
    }



}