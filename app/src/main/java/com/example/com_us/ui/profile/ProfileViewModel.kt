package com.example.com_us.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.com_us.data.default_repository.NetworkError
import com.example.com_us.data.repository.ProfileRepository
import com.example.com_us.data.model.question.response.question.ResponseProfileDto
import com.example.com_us.ui.ApiResult
import com.example.com_us.util.ServerResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.UIntArraySerializer
import okhttp3.Response
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val profileRepository: ProfileRepository) : ViewModel() {

    private val _profileUiState = MutableStateFlow<ApiResult<ResponseProfileDto>>(ApiResult.Initial)
    val profileUiState = _profileUiState.asStateFlow()




    fun loadProfileData() {
        viewModelScope.launch {
            profileRepository.getProfileData()
                .onSuccess {
                    _profileUiState.value = ApiResult.Success(it)
                }
                .onFailure {
                    val errorMessage = when (it) {
                        is NetworkError.NetworkException -> { it.message }
                        is NetworkError.NullDataError -> { "데이터가 준비중이에요!" }
                        else -> "알 수 없는 에러가 발생했습니다. 다시 시도해주세요!"
                    }
                    if (errorMessage != null) {
                        _profileUiState.value = ApiResult.Error(errorMessage)
                    }
                }
        }
    }
}