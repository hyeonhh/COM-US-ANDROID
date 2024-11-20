package com.example.com_us.ui.question.result

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.com_us.data.model.question.response.question.ResponseAnswerDetailDto
import com.example.com_us.data.repository.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultViewModel  @Inject constructor(
    private val questionRepository : QuestionRepository
) : ViewModel() {

    private val _answerDetail = MutableLiveData<List<ResponseAnswerDetailDto>>()
    val answerDetail: LiveData<List<ResponseAnswerDetailDto>> = _answerDetail


    fun loadAnswerDetail(answer: String){
        viewModelScope.launch {
            questionRepository.getAnswerDetail(answer)
                .onSuccess {
                    _answerDetail.value = it
                }
                .onFailure {
                    Log.d("GET: [ANSWER DETAIL]", it.toString())
                }
        }
    }


}