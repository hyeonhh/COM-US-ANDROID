package com.example.com_us.ui.qa

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.com_us.base.data.NetworkError
import com.example.com_us.base.viewmodel.BaseViewModel
import com.example.com_us.data.model.modify.ModifyRequest
import com.example.com_us.data.model.modify.ModifyResponse
import com.example.com_us.data.model.question.request.RequestAnswerRequest
import com.example.com_us.data.model.question.response.question.ResponseQuestionDetailDto
import com.example.com_us.data.repository.ModifyRepository
import com.example.com_us.data.repository.QuestionRepository
import com.example.com_us.ui.base.UiState
import com.example.com_us.ui.event.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AnswerViewModel @Inject constructor(
    private val modifyRepository: ModifyRepository
):BaseViewModel(){

    private val _uiState =  MutableStateFlow<UiState<ModifyResponse>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()


     var answeredDate : String = ""
     var selectedQuestion : String = ""
     var answer : String = ""

    var answerType :String = ""
    var category : String = ""

     fun setAnswerDate(date : String) = run { answeredDate = date }
    fun setQuestion(question:String) = run{selectedQuestion = question}
    fun setUserAnswer(answer:String) = run { this.answer = answer }

    fun setUserAnswerType(answerType:String) = run { this.answerType = answerType
    Timber.d("answerTYpe :$answerType")}
    fun setQuestionCategory(category:String) = run { this.category = category }



    // 홈화면 필요 여부
    private val _answerEvent  = SingleLiveEvent<Any>()
    val answerEvent : LiveData<Any>
        get() = _answerEvent

    private fun startScreen(){
        _answerEvent.call()
    }

    // 홈화면 필요 여부
    private val _failEvent  = SingleLiveEvent<Any>()
    val failEvent : LiveData<Any>
        get() = _failEvent

    private fun failScreen(){
        _failEvent.call()
    }
    fun getModifyAnswer() {
        viewModelScope.launch {
            modifyRepository.getModifyAnswer(ModifyRequest(answer))
                .onSuccess {
                       _uiState.value = UiState.Success(it)
                       startScreen()
                   }
                .onFailure {
                    _uiState.value = UiState.Error("잠시만 기다려주세요")
                    failScreen()

                }



        }
    }



}