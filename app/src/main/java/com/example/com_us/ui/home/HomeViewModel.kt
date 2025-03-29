package com.example.com_us.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.com_us.base.data.NetworkError
import com.example.com_us.base.viewmodel.BaseViewModel
import com.example.com_us.data.model.auth.LoginResponse
import com.example.com_us.data.repository.HomeRepository
import com.example.com_us.data.model.home.ResponseHomeDataDto
import com.example.com_us.data.repository.UserTokenRepository
import com.example.com_us.ui.base.UiState
import com.example.com_us.ui.event.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val userTokenRepository: UserTokenRepository
) : BaseViewModel() {
    private val _homeUiState= MutableStateFlow<UiState<ResponseHomeDataDto>>(UiState.Loading)
    val homeUiState = _homeUiState.asStateFlow()

    private var accessToken : String =""
    private var refreshToken : String = ""


    init {
        viewModelScope.launch {
            getToken().await()
            if (accessToken=="" || refreshToken=="") {
                startLoginActivity()
            }
            else {
                loadHomeData()
            }
        }


    }
    // 로그인 필요 여부
    private val _loginEvent  = SingleLiveEvent<Any>()
    val loginEvent : LiveData<Any>
        get() = _loginEvent

    private fun startLoginActivity(){
        _loginEvent.call()
    }

    private fun getToken()=
        viewModelScope.async {
            // 만약 빈 경우 로그인 실행
            accessToken = userTokenRepository.getAccessToken().first()
            refreshToken = userTokenRepository.getRefreshToken().first()
        }


    fun loadHomeData(){
        viewModelScope.launch {
            homeRepository.getHomeData()
            .onSuccess {
                    _homeUiState.value = UiState.Success(it)
                }
                .onFailure {
                    val errorMessage = when(it) {
                        is NetworkError.IOException -> { "연결된 네트워크 확인 후 다시 시도해주세요!" }
                        is NetworkError.HttpException -> {
                            Log.e("home api failed",it.message)
                            "에러가 발생했습니다! 잠시 후에 다시 시도해주세요"
                        }
                        else -> "알 수 없는 에러가 발생했습니다. 다시 시도해주세요!"
                    }
                    _homeUiState.value = UiState.Error(errorMessage)
                    checkToken()
                }
        }
    }

    private suspend fun checkToken(){
        Timber.d("checkToken")
        viewModelScope.async {
                userTokenRepository.reissueToken(
                    LoginResponse(
                        accessToken =
                        accessToken,
                        refreshToken = refreshToken
                    )
                ).apply {
                    when (this.status) {
                        200 -> {
                            Timber.d("status:${this.status}")
                            // 토큰 갱신 성공
                            if (this.data != null) {
                                userTokenRepository.saveAccessToken(this.data.accessToken)
                                userTokenRepository.saveRefreshToken(this.data.refreshToken)
                                Timber.d("토큰 갱신 성공")
                            }
                        }

                        500, 401 -> {
                            //토큰 갱신 실패 : 재로그인!
                            startLoginActivity()
                            throw NetworkError.HttpException(message)
                        }
                    }
                }
        }.await()
    }




}