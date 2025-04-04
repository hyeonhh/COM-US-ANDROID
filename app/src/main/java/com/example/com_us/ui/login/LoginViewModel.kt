package com.example.com_us.ui.login

import android.util.Log
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialResponse
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.com_us.base.viewmodel.BaseViewModel
import com.example.com_us.data.model.auth.LoginRequest
import com.example.com_us.data.model.auth.LoginResponse
import com.example.com_us.data.repository.AuthRepository
import com.example.com_us.data.repository.UserTokenRepository
import com.example.com_us.data.repository.impl.UserTokenRepositoryImpl
import com.example.com_us.ui.event.SingleLiveEvent
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

// 구글 로그인 처리
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userTokenRepository: UserTokenRepository
) : BaseViewModel() {

    private var accessToken : String =""
    private var refreshToken : String = ""

    init {
        viewModelScope.launch {
            getToken().await()
            if (accessToken!="" && refreshToken!="") {
                startHomeActivity()
            }
        }
    }

    // 홈화면 필요 여부
    private val _homeEvent  = SingleLiveEvent<Any>()
    val homeEvent : LiveData<Any>
        get() = _homeEvent

    private fun startHomeActivity(){
        _homeEvent.call()
    }

    private  fun getToken()=
        viewModelScope.async {
            // 만약 빈 경우 로그인 실행
            accessToken =  userTokenRepository.getAccessToken().first()
            refreshToken = userTokenRepository.getRefreshToken().first()
        }

     fun onKakaoLogin(request : LoginRequest){
         Timber.d("로그인")
        viewModelScope.launch {
            authRepository.login(request)
                .onSuccess {
                    //todo : 토큰 처리
                    // access token , refresh token 저장하기
                    Timber.d("success to login :${it}")
                    userTokenRepository.saveAccessToken(it.accessToken)
                    userTokenRepository.saveRefreshToken(it.refreshToken)
                    startHomeActivity()
                }
                .onFailure {
                    Timber.d("failed to login :${it}")
                }
        }
    }
}