package com.example.com_us.ui.question

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.com_us.R
import com.example.com_us.data.repository.QuestionRepository
import com.example.com_us.data.model.question.request.RequestAnswerRequest
import com.example.com_us.data.model.question.response.question.ResponseAnswerDetailDto
import com.example.com_us.data.model.question.response.question.ResponseAnswerDetailWithDateDto
import com.example.com_us.data.model.question.response.question.ResponsePreviousAnswerDto
import com.example.com_us.data.model.question.response.question.ResponseQuestionDetailDto
import com.example.com_us.data.model.question.response.question.ResponseQuestionDto
import com.example.com_us.util.ServerResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(private val questionRepository: QuestionRepository) : ViewModel() {

    var serverResponseHandler: ServerResponseHandler? = null

    private val _selectedThemeId = MutableLiveData<Int>().apply {
        value = R.id.include_theme_all
    }
    val selectedThemeId: LiveData<Int> = _selectedThemeId

    private val _selectedAnswerOptionId = MutableLiveData(-1)
    val selectedAnswerOptionId: LiveData<Int> = _selectedAnswerOptionId

    private val _questionListByCate = MutableLiveData<List<ResponseQuestionDto>>()
    val questionListByCate: LiveData<List<ResponseQuestionDto>> = _questionListByCate

    private val _questionDetail = MutableLiveData<ResponseQuestionDetailDto>()
    val questionDetail: LiveData<ResponseQuestionDetailDto> = _questionDetail

    private val _answerDetail = MutableLiveData<List<ResponseAnswerDetailDto>>()
    val answerDetail: LiveData<List<ResponseAnswerDetailDto>> = _answerDetail

    private val _answerPrevious = MutableLiveData<ResponsePreviousAnswerDto>()
    val answerPrevious: LiveData<ResponsePreviousAnswerDto> = _answerPrevious

    private val _resultData = MutableLiveData<ResponseAnswerDetailWithDateDto>()
    val resultData: LiveData<ResponseAnswerDetailWithDateDto> = _resultData

    fun updateSelectedThemeId(newId: Int) {
        _selectedThemeId.value = newId
    }
    fun updateSelectedAnswerOptionId(newOptionId: Int) {
        _selectedAnswerOptionId.value = newOptionId
    }
    fun loadQuestionListByCate(category: String){
        viewModelScope.launch {
            questionRepository.getQuestionListByCate(category)
                .onSuccess {
                    serverResponseHandler?.onServerSuccess()
                    _questionListByCate.value = it
                }
                .onFailure {
                    serverResponseHandler?.onServerFailure()
                    Log.d("GET: [QUESTION LIST BY CATE]", it.toString())
                }
        }
    }

    fun loadQuestionDetail(questionId: Long){
        viewModelScope.launch {
            questionRepository.getQuestionDetail(questionId)
                .onSuccess {
                    _questionDetail.value = it
                    serverResponseHandler?.onServerSuccess()
                }
                .onFailure {
                    serverResponseHandler?.onServerFailure()
                    Log.d("GET: [QUESTION DETAIL]", it.toString())
                }
        }
    }

    fun loadAnswerDetail(answer: String){
        viewModelScope.launch {
            questionRepository.getAnswerDetail(answer)
                .onSuccess {
                    _answerDetail.value = it
                    serverResponseHandler?.onServerSuccess()
                }
                .onFailure {
                    serverResponseHandler?.onServerFailure()
                    Log.d("GET: [ANSWER DETAIL]", it.toString())
                }
        }
    }

    fun postAnswer(questionId: Long, answerContent: String){
        var body = RequestAnswerRequest(questionId, answerContent)
        viewModelScope.launch {
            questionRepository.postAnswer(body)
                .onSuccess {
                    _resultData.value = it
                    serverResponseHandler?.onServerSuccess()
                }
                .onFailure {
                    serverResponseHandler?.onServerFailure()
                    Log.d("POST: [ANSWER]", it.toString())
                }
        }
    }

    fun loadPreviousAnswer(questionId: Long) {
        viewModelScope.launch {
            questionRepository.getPreviousAnswer(questionId)
                .onSuccess {
                    _answerPrevious.value = it
                    serverResponseHandler?.onServerSuccess()
                }
                .onFailure {
                    serverResponseHandler?.onServerFailure()
                    Log.d("GET: [ANSWER DETAIL]", it.toString())
                }
        }
    }
}