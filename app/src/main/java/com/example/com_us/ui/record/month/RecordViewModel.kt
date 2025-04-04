package com.example.com_us.ui.record.month

import androidx.compose.runtime.MutableState
import androidx.lifecycle.viewModelScope
import com.example.com_us.base.viewmodel.BaseViewModel
import com.example.com_us.data.model.question.Data
import com.example.com_us.data.repository.BlockRepository
import com.example.com_us.data.repository.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val questionRepository: QuestionRepository,
) : BaseViewModel(){

    private val _record = MutableStateFlow(emptyList<Data>())
    val record = _record.asStateFlow()

    init {
        getBlockInfo()
    }

     private fun getBlockInfo(){
        viewModelScope.launch {
            questionRepository.getAnswer().onSuccess {
                _record.value = it
            }
                .onFailure {
                    Timber.d("월별 실패 :${it.message}")
                }
        }
    }
}