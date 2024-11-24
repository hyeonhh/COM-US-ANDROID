package com.example.com_us.ui.question.select

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.com_us.base.data.NetworkError
import com.example.com_us.data.model.question.response.question.ResponseQuestionDetailDto
import com.example.com_us.data.repository.QuestionRepository
import com.example.com_us.ui.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectAnswerViewModel @Inject constructor(
    private val questionRepository: QuestionRepository
): ViewModel() {

    // ui 상태 변수
    private val _uiState =  MutableStateFlow<UiState<ResponseQuestionDetailDto>>(UiState.Initial)
    val uiState: StateFlow<UiState<ResponseQuestionDetailDto>> = _uiState

    private val _questionDetail = MutableLiveData<ResponseQuestionDetailDto>()
    val questionDetail: LiveData<ResponseQuestionDetailDto> = _questionDetail

    private val _selectedAnswerOptionId = MutableLiveData(-1)
    val selectedAnswerOptionId: LiveData<Int> = _selectedAnswerOptionId

    fun updateSelectedAnswerOptionId(newOptionId: Int) {
        _selectedAnswerOptionId.value = newOptionId
    }

    // 질문 클릭 시 상세 내용 가져오는 함수
    fun loadQuestionDetail(questionId: Long){
        viewModelScope.launch {
            questionRepository.getQuestionDetail(questionId)
                .onSuccess {
                    _uiState.value = UiState.Success(it)
                }
                .onFailure {
                    println(it)
                    val errorMessage = when(it) {
                        is NetworkError.ApiError -> { it.message }
                        is NetworkError.NullDataError -> { "데이터가 없어요" }
                        is NetworkError.NetworkException -> { "잠시 후에 다시 시도해주세요" }
                        else -> "알 수 없는 에러가 발생했어요. 다시 시도해주세요!"
                    }
                    _uiState.value = UiState.Error(errorMessage)
                }
        }
    }

}