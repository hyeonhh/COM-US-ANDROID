package com.example.com_us.ui.question.result

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.com_us.data.model.question.response.question.ResponseAnswerDetailDto
import com.example.com_us.data.repository.QuestionRepository
import com.example.com_us.ui.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultViewModel  @Inject constructor(
    private val questionRepository : QuestionRepository
) : ViewModel() {

    private val _answerDetail = MutableStateFlow<UiState<List<ResponseAnswerDetailDto>>>(UiState.Loading)
    val answerDetail =_answerDetail.asStateFlow()




    fun loadAnswerDetail(answer: String){
        viewModelScope.launch {
            questionRepository.getAnswerDetail(answer)
                .onSuccess {
                    _answerDetail.value = UiState.Success(it)
                }
                .onFailure {
                    Log.d("GET: [ANSWER DETAIL]", it.toString())
                }
        }
    }


}