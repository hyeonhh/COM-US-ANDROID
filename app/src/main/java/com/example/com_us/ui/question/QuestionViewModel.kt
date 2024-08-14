package com.example.com_us.ui.question

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.com_us.R
import com.example.com_us.data.repository.QuestionRepository
import com.example.com_us.data.response.question.ResponseQuestionDto
import kotlinx.coroutines.launch

class QuestionViewModel(private val questionRepository: QuestionRepository) : ViewModel() {

    private val _selectedThemeId = MutableLiveData<Int>().apply {
        value = R.id.include_theme_all
    }
    val selectedThemeId: LiveData<Int> = _selectedThemeId

    private val _questionListByCate = MutableLiveData<List<ResponseQuestionDto>>()
    val questionListByCate: LiveData<List<ResponseQuestionDto>> = _questionListByCate

    fun updateSelectedThemeId(newId: Int) {
        _selectedThemeId.value = newId
    }
    fun loadQuestionListByCate(category: String){
        viewModelScope.launch {
            questionRepository.getQuestionListByCate(category)
                .onSuccess {
                    _questionListByCate.value = it
                    println(_questionListByCate)
                }
                .onFailure {
                    Log.d("GET: [QUESTION LIST BY CATE] DATA FAILURE", it.toString())
                }
        }
    }
}