package com.example.com_us.ui.question.select

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.com_us.data.default_repository.NetworkError
import com.example.com_us.data.model.question.response.question.ResponseQuestionDetailDto
import com.example.com_us.data.repository.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectAnswerViewModel @Inject constructor(
    private val questionRepository: QuestionRepository
): ViewModel() {

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
                    _questionDetail.value = it
                }
                .onFailure {
                    when {
                        it is NetworkError.NetworkException -> {
                            Log.d("GET: [QUESTION DETAIL]", it.toString())

                        }
                        it is NetworkError.NullDataError -> {
                            Log.d("GET: [QUESTION DETAIL]", it.toString())
                        }
                    }
                }
        }
    }

}