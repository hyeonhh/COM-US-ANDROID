package com.example.com_us.ui.question.sign

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.com_us.base.data.NetworkError
import com.example.com_us.base.viewmodel.BaseViewModel
import com.example.com_us.data.repository.QuestionRepository
import com.example.com_us.data.model.question.request.RequestAnswerRequest
import com.example.com_us.data.model.question.response.question.ResponseAnswerDetailWithDateDto
import com.example.com_us.ui.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignAnswerViewModel @Inject constructor(
    private val questionRepository: QuestionRepository,
) : BaseViewModel() {


    private val _signIndex = MutableStateFlow<Int>(0)
    val signIndex  = _signIndex.asStateFlow()

    private val _uiState = MutableStateFlow<UiState<ResponseAnswerDetailWithDateDto>>(UiState.Loading)
    val uiState=   _uiState.asStateFlow()
    private val _resultData = MutableLiveData<ResponseAnswerDetailWithDateDto>()
    val resultData: LiveData<ResponseAnswerDetailWithDateDto> = _resultData

    // todo : 이 함수의 역할은 뭐야? : 답변을 저장하는 함수
    fun postAnswer(questionId: Int, answerContent: String,answerType : String ){
        val body = RequestAnswerRequest(questionId, answerContent, answerType)
        viewModelScope.launch {
            questionRepository.postAnswer(body)
                .onSuccess {
                    Log.d("success","success to save answer to server")
                }
                .onFailure {
                    Log.e("failed","failed to save answer to server")
                }
        }
    }


    fun setSignIndex(index : Int ) {
        _signIndex.value = index
    }


}