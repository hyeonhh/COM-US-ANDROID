package com.example.com_us.ui.question.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.com_us.R
import com.example.com_us.base.data.NetworkError
import com.example.com_us.base.viewmodel.BaseViewModel
import com.example.com_us.data.repository.impl.LikeRepository
import com.example.com_us.data.model.question.response.question.ResponseQuestionDto
import com.example.com_us.data.repository.QuestionRepository
import com.example.com_us.ui.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject



@HiltViewModel
class ThemeQuestionListViewModel @Inject constructor(
    private val questionRepository: QuestionRepository,
    private val likeRepository: LikeRepository,
) : BaseViewModel() {
    // 질문 리스트
    private val _questionListByCate = MutableLiveData<List<ResponseQuestionDto>>()
    val questionListByCate: LiveData<List<ResponseQuestionDto>> = _questionListByCate


    // ui 상태 변수
    private val _uiState = MutableStateFlow<UiState<List<ResponseQuestionDto>>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    // 선택한 테마의 id
    private val _selectedThemeId = MutableLiveData<Int>().apply {
        value = R.id.include_theme_all
    }
    val selectedThemeId: LiveData<Int> = _selectedThemeId



    // 좋아요 등록
    fun setLike(questionId : String){
        viewModelScope.launch {
            likeRepository.setLike(questionId).apply {
                if(this.status == 201){
                    Timber.d("success to like")
                }
                else {
                    Timber.d("failed to like")
                }
            }
        }
    }

    // 좋아요 취소
    fun setUnLike(questionId : String){
        viewModelScope.launch {
            likeRepository.setUnlike(questionId).apply {
                if(this.status == 201){
                    Timber.d("success to unlike")
                } else Timber.d("failed to like")

            }
        }
    }



    // 클릭한 카테고리의 질문 리스트를 가져오는 함수
    fun loadQuestionListByCate(category: String){
        viewModelScope.launch {
            questionRepository.getQuestionListByCate(category,"")
                .onSuccess {
                    _uiState.value = UiState.Success(it)
                }
                .onFailure {
                    val errorMessage = when(it){
                        is NetworkError.IOException -> {it.message}
                        is NetworkError.NullDataError -> {"아직 데이터를 준비중이에요!"}
                        else -> "알 수 없는 에러가 발생했습니다. 다시 시도해주세요!"
                    }
                    if (errorMessage != null) {
                        _uiState.value = UiState.Error(errorMessage)
                    }
                }
        }
    }
}