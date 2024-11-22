package com.example.com_us.ui.question.sign

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.com_us.data.default_repository.NetworkError
import com.example.com_us.data.repository.QuestionRepository
import com.example.com_us.data.model.question.request.RequestAnswerRequest
import com.example.com_us.data.model.question.response.question.ResponseAnswerDetailWithDateDto
import com.example.com_us.ui.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignAnswerViewModel @Inject constructor(
    private val questionRepository: QuestionRepository,
) : ViewModel() {


    private val _signIndex = MutableStateFlow<Int>(0)
    val signIndex  = _signIndex.asStateFlow()

    private val _apiResult = MutableStateFlow<ApiResult<ResponseAnswerDetailWithDateDto>>(ApiResult.Initial)
    val uiState=   _apiResult.asStateFlow()
    private val _resultData = MutableLiveData<ResponseAnswerDetailWithDateDto>()
    val resultData: LiveData<ResponseAnswerDetailWithDateDto> = _resultData

    fun setSignIndex(index : Int ) {
        _signIndex.value = index
    }
// todo : 이 함수의 역할은 뭐야?
    fun postAnswer(questionId: Long, answerContent: String){
        val body = RequestAnswerRequest(questionId, answerContent)
        viewModelScope.launch {
            questionRepository.postAnswer(body)
                .onSuccess {
                    _apiResult.value = ApiResult.Success(it)
                }
                .onFailure {
                   val errorMessage =  when(it) {
                       is NetworkError.NetworkException -> { "네트워크 에러가 발생했어요! 잠시 후에 다시 시도해주세에요"}
                       is NetworkError.ApiError ->{it.message}
                       is NetworkError.NullDataError -> {"데이터를 준비하고 있어요!"}
                       else -> { "잠시 후에 다시 시도해주세요"}
                   }
                    _apiResult.value = ApiResult.Error(errorMessage)
                }
        }
    }


}