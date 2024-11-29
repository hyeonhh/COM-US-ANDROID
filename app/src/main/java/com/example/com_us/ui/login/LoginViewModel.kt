package com.example.com_us.ui.login

import android.util.Log
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialResponse
import androidx.lifecycle.viewModelScope
import com.example.com_us.base.viewmodel.BaseViewModel
import com.example.com_us.data.repository.AuthRepository
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

// 구글 로그인 처리
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : BaseViewModel() {

    fun handleSignIn(result : GetCredentialResponse) {
        when(val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        //todo : 확인용 출력문
                        println(googleIdTokenCredential.id)

                        // 로그인
                        googleLogin(googleIdTokenCredential.id)

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


    private fun googleLogin(code : String) {
        viewModelScope.launch {
            authRepository.login(code)
                .onSuccess {
                    // todo : 로그인 성공 시 메인 화면으로 이동하도록 하기
                }
                .onFailure {
                    // 로그인 중 예외 발생으로 실패
                    // todo : 로그인 실패 처리하기
                }
        }
    }

}