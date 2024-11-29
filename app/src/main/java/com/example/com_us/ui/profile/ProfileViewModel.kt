package com.example.com_us.ui.profile

import androidx.lifecycle.viewModelScope
import com.example.com_us.base.data.NetworkError
import com.example.com_us.base.viewmodel.BaseViewModel
import com.example.com_us.data.repository.ProfileRepository
import com.example.com_us.data.model.question.response.question.ResponseProfileDto
import com.example.com_us.ui.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val profileRepository: ProfileRepository) : BaseViewModel() {

    private val _profileUiState = MutableStateFlow<UiState<ResponseProfileDto>>(UiState.Loading)
    val profileUiState = _profileUiState.asStateFlow()

    init {
        loadProfileData()
    }

    private fun loadProfileData() {
        viewModelScope.launch {
            profileRepository.getProfileData()
                .onSuccess {
                    _profileUiState.value = UiState.Success(it)
                }
                .onFailure {
                    val errorMessage = when (it) {
                        is NetworkError.IOException -> { it.message }
                        is NetworkError.NullDataError -> { "데이터가 준비중이에요!" }
                        else -> "알 수 없는 에러가 발생했습니다. 다시 시도해주세요!"
                    }
                    if (errorMessage != null) {
                        _profileUiState.value = UiState.Error(errorMessage)
                    }
                }
        }
    }
}