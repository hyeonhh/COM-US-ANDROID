package com.example.com_us.ui.question.previous

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.com_us.base.data.NetworkError
import com.example.com_us.data.model.question.response.question.ResponsePreviousAnswerDto
import com.example.com_us.data.repository.QuestionRepository
import com.example.com_us.ui.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class PreviousAnswerViewModel @Inject constructor(
    private val questionRepository: QuestionRepository
) : ViewModel(){

    // ui 상태 변수
    private val _uiState = MutableStateFlow<UiState<ResponsePreviousAnswerDto>>(UiState.Initial)
    val uiState=  _uiState.asStateFlow()

    private val _answerPrevious = MutableLiveData<ResponsePreviousAnswerDto>()
    val answerPrevious: LiveData<ResponsePreviousAnswerDto> = _answerPrevious

    fun loadPreviousAnswer(questionId: Long) {
        viewModelScope.launch {
            questionRepository.getPreviousAnswer(questionId)
                .onSuccess {
                    _uiState.value = UiState.Success(it)
                }
                .onFailure {
                    val errorMessage = when(it){
                        is NetworkError.NetworkException -> {it.message}
                        is NetworkError.NullDataError -> "데이터가 없어요"
                        else -> "알 수 없는 에러가 발생했어요. 다시 시도해주세요!"
                    }
                    if (errorMessage != null) {
                        _uiState.value = UiState.Error(errorMessage)
                    }
                }
        }
    }
}