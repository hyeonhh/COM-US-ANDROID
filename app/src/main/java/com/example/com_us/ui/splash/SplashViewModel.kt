package com.example.com_us.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.com_us.base.viewmodel.BaseViewModel
import com.example.com_us.data.model.auth.LoginResponse
import com.example.com_us.data.repository.UserTokenRepository
import com.example.com_us.ui.event.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userTokenRepository: UserTokenRepository,
): BaseViewModel() {

    private var accessToken : String? =null
    private var refreshToken : String? = null

    init {
        viewModelScope.launch {
          getToken().await()
            if (accessToken=="" || refreshToken=="" || accessToken==null) {
                Timber.d("토큰이 비어있어서, 스플래시에서 로그인으로 이동 ")
                startLoginActivity()
            }
            else {
                Timber.d("토큰이 있어서, 스플래시에서 홈으로 이동 ")
                startHomeActivity()
            }
        }

    }

    // 로그인 필요 여부
    private val _loginEvent  = SingleLiveEvent<Any>()
    val loginEvent : LiveData<Any>
        get() = _loginEvent

    // 홈화면 필요 여부
    private val _homeEvent  = SingleLiveEvent<Any>()
    val homeEvent : LiveData<Any>
        get() = _homeEvent

    private fun startLoginActivity(){
        _loginEvent.call()
    }

    private fun startHomeActivity(){
        _homeEvent.call()
    }



    // 토큰 검사 - 액세스 토큰이 유효하다면 -> 홈화면으로 이동하기

    private  fun getToken()= viewModelScope.async {
            // 만약 빈 경우 로그인 실행
            Timber.d("토큰이 있는지 검사하기 ")
            accessToken =  userTokenRepository.getAccessToken().first()
            refreshToken = userTokenRepository.getRefreshToken().first()
        }

    }


    // 액세스 토큰 만료 시 리프레시 토큰으로 재발급하기

    // 액세스 && 리프레시도 만료 시 재로그인 !