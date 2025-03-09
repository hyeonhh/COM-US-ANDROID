package com.example.com_us.ui.login

import android.util.Log
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialResponse
import androidx.lifecycle.viewModelScope
import com.example.com_us.base.viewmodel.BaseViewModel
import com.example.com_us.data.model.auth.LoginRequest
import com.example.com_us.data.repository.AuthRepository
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

// 구글 로그인 처리
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : BaseViewModel() {

    fun onKakaoLogin(request : LoginRequest){
        viewModelScope.launch {
            authRepository.login(request)
                .onSuccess {
                    Timber.e("success to login :${it}")

                }
                .onFailure {
                    Timber.e("failed to login :${it.message}")
                }
        }
    }
    fun handleSignIn(result : GetCredentialResponse) {
        when(val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        //todo : 확인용 출력문
                        println(googleIdTokenCredential.id)

                        // 로그인
                        // googleLogin(googleIdTokenCredential.id)

                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e(TAG, "Received an invalid google id token response", e)
                    }
                } else {
                    Log.e(TAG, "Unexpected type of credential")
                }
            }
            else ->  {
                Log.e(TAG, "Unexpected type of credential")
            }
        }
    }


}