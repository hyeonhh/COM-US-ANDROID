package com.example.com_us.ui.question.sign

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.com_us.data.repository.QuestionRepository
import com.example.com_us.data.model.question.request.RequestAnswerRequest
import com.example.com_us.data.model.question.response.question.ResponseAnswerDetailWithDateDto
import com.example.com_us.data.model.question.response.question.ResponseQuestionDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


// Ui 상태 정의
sealed class QuestionUiState {
    data object Initial : QuestionUiState()
    data class Success(val data : List<ResponseQuestionDto>) : QuestionUiState()
    data class Error(val message : String) : QuestionUiState()

}

@HiltViewModel
class SignAnswerViewModel @Inject constructor(
    private val questionRepository: QuestionRepository,
) : ViewModel() {



    private val _resultData = MutableLiveData<ResponseAnswerDetailWithDateDto>()
    val resultData: LiveData<ResponseAnswerDetailWithDateDto> = _resultData



    fun postAnswer(questionId: Long, answerContent: String){
        var body = RequestAnswerRequest(questionId, answerContent)
        viewModelScope.launch {
            questionRepository.postAnswer(body)
                .onSuccess {
                    _resultData.value = it
                }
                .onFailure {
                    Log.d("POST: [ANSWER]", it.toString())
                }
        }
    }


}