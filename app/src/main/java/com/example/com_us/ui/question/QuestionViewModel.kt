package com.example.com_us.ui.question

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.com_us.R
import com.example.com_us.data.repository.QuestionRepository
import com.example.com_us.data.request.question.RequestAnswerDto
import com.example.com_us.data.response.question.ResponseAnswerDetailDto
import com.example.com_us.data.response.question.ResponsePreviousAnswerDto
import com.example.com_us.data.response.question.ResponseQuestionDetailDto
import com.example.com_us.data.response.question.ResponseQuestionDto
import kotlinx.coroutines.launch

class QuestionViewModel(private val questionRepository: QuestionRepository) : ViewModel() {

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
                    _questionListByCate.value = it
                }
                .onFailure {
                    Log.d("GET: [QUESTION LIST BY CATE] DATA FAILURE", it.toString())
                }
        }
    }

    fun loadQuestionDetail(questionId: Long){
        viewModelScope.launch {
            questionRepository.getQuestionDetail(questionId)
                .onSuccess {
                    _questionDetail.value = it
                }
                .onFailure {
                    Log.d("GET: [QUESTION DETAIL] DATA FAILURE", it.toString())
                }
        }
    }

    fun loadAnswerDetail(answer: String){
        viewModelScope.launch {
            questionRepository.getAnswerDetail(answer)
                .onSuccess {
                    _answerDetail.value = it
                }
                .onFailure {
                    Log.d("GET: [ANSWER DETAIL] DATA FAILURE", it.toString())
                }
        }
    }

    fun postAnswer(questionId: Long, answerContent: String){
        var body = RequestAnswerDto(questionId, answerContent)
        viewModelScope.launch {
            questionRepository.postAnswer(body)
                .onSuccess {
                }
                .onFailure {
                    Log.d("POST: [ANSWER] DATA FAILURE", it.toString())
                }
        }
    }

    fun loadPreviousAnswer(questionId: Long) {
        viewModelScope.launch {
            questionRepository.getPreviousAnswer(questionId)
                .onSuccess {
                    _answerPrevious.value = it
                }
                .onFailure {
                    Log.d("GET: [ANSWER DETAIL] DATA FAILURE", it.toString())
                }
        }
    }
}