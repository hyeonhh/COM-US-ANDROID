package com.example.com_us.ui.question.previous

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.com_us.data.model.question.response.question.ResponsePreviousAnswerDto
import com.example.com_us.data.repository.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreviousAnswerViewModel @Inject constructor(
    private val questionRepository: QuestionRepository
) : ViewModel(){

    private val _answerPrevious = MutableLiveData<ResponsePreviousAnswerDto>()
    val answerPrevious: LiveData<ResponsePreviousAnswerDto> = _answerPrevious

    fun loadPreviousAnswer(questionId: Long) {
        viewModelScope.launch {
            questionRepository.getPreviousAnswer(questionId)
                .onSuccess {
                    _answerPrevious.value = it
                }
                .onFailure {
                    Log.d("GET: [ANSWER DETAIL]", it.toString())
                }
        }
    }
}