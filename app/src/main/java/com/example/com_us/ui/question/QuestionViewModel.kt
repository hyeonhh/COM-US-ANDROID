package com.example.com_us.ui.question

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.com_us.R

class QuestionViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    private val _selectedThemeId = MutableLiveData<Int>().apply {
        value = R.id.include_theme_all
    }
    val selectedThemeId: LiveData<Int> = _selectedThemeId

    fun updateSelectedThemeId(newId: Int) {
        _selectedThemeId.value = newId
    }
}