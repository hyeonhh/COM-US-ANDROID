package com.example.com_us.ui.question.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.com_us.R
import com.example.com_us.data.default_repository.NetworkError
import com.example.com_us.data.model.home.ResponseHomeDataDto
import com.example.com_us.data.model.question.response.question.ResponseAnswerDetailWithDateDto
import com.example.com_us.data.model.question.response.question.ResponseQuestionDto
import com.example.com_us.data.repository.QuestionRepository
import com.example.com_us.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AllQuestionListViewModel @Inject constructor(
    private val questionRepository: QuestionRepository
) : ViewModel() {

    init {
        loadQuestionListByCate("")
    }

    // 질문 리스트
    private val _questionListByCate = MutableLiveData<List<ResponseQuestionDto>>()
    val questionListByCate: LiveData<List<ResponseQuestionDto>> = _questionListByCate


    // ui 상태 변수
    private val _uiState = MutableStateFlow<UiState<List<ResponseQuestionDto>>>(UiState.Initial)
    val uiState  = _uiState.asStateFlow()


    // 선택한 테마의 id
    private val _selectedThemeId = MutableLiveData<Int>().apply {
        value = R.id.include_theme_all
    }
    val selectedThemeId: LiveData<Int> = _selectedThemeId



    fun updateSelectedThemeId(newId: Int) {
        _selectedThemeId.value = newId
    }


    // 클릭한 카테고리의 질문 리스트를 가져오는 함수
    fun loadQuestionListByCate(category: String){
        viewModelScope.launch {
            questionRepository.getQuestionListByCate(category)
                .onSuccess {
                    _uiState.value = UiState.Success(it)
                }
                .onFailure {
                    val errorMessage = when(it){
                        is NetworkError.NetworkException -> {it.message}
                        is NetworkError.NullDataError -> {"아직 데이터를 준비중이에요!"}
                        else -> "알 수 없는 에러가 발생했습니다. 다시 시도해주세요!"
                    }
                    if (errorMessage != null) {
                        _uiState.value = UiState.Error(errorMessage)
                    }                }
        }
    }


}