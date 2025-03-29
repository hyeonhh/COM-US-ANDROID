package com.example.com_us.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.GetPasswordOption
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.lifecycleScope
import com.example.com_us.MainActivity
import com.example.com_us.R
import com.example.com_us.base.activity.BaseActivity
import com.example.com_us.data.model.auth.LoginRequest
import com.example.com_us.databinding.ActivityLoginBinding
import com.example.com_us.databinding.ActivityQuestionCheckAnswerBinding
import com.google.android.gms.common.api.Api.Client
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.AuthCodeClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApi
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.jvm.Throws

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding,LoginViewModel>(
    ActivityLoginBinding::inflate
) {
    override val viewModel : LoginViewModel by viewModels()

    override fun onBindLayout() {
        super.onBindLayout()
        binding.btnKakaoLogin.setOnClickListener { onKakaoLogin() }

      lifecycleScope.launch {
          viewModel.homeEvent.observe(this@LoginActivity, {
              val intent = Intent(this@LoginActivity, MainActivity::class.java)
              startActivity(intent)
          })
      }

    }

    val callback : (OAuthToken?, Throwable?) -> Unit = {token,error ->
        if (error!= null ){
            Timber.e("failed to login :$error")
        }
        else if (token != null){
            Timber.i("success to login :${token.accessToken}")
        }
        else {
            Timber.i("error to login :${token}")
        }
    }


    private fun onKakaoLogin(){
        try {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this@LoginActivity)) {
                Timber.d("카카오톡 로그인 가능? ? yes")
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        Timber.e("failed to login with kakaotalk :$error")
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }  else {
                            UserApiClient.instance.loginWithKakaoAccount(
                                this,
                                callback = callback
                            ) // 카카오 이메일 로그인
                        }
                        UserApiClient.instance.loginWithKakaoAccount(
                            this@LoginActivity,
                            callback = callback
                        )
                    } else if (token != null) {
                        UserApiClient.instance.me { user, e ->
                            if (e != null) {
                                Timber.e("사용자 정보 요청 실패 :${e.message}")
                            } else if (user != null) {
                                val request = LoginRequest(
                                    name = user.kakaoAccount?.profile?.nickname ?: "",
                                    imageUrl = user.kakaoAccount?.profile?.thumbnailImageUrl ?: "",
                                    socialId = user.id.toString(),
                                )
                                    viewModel.onKakaoLogin(request)
                            }
                            Timber.i("success to login with kakaotalk :${token.accessToken}")

                        }

                    }
                    else {
                        Timber.i("여기 에러요! ")
                    }
                }
                // 카카오톡 앱으로 로그인 못하는 경우
            } else {
                Timber.d("카카오톡으로 로그인 가능하니? ? no")

                try {
                    val callback : (OAuthToken?, Throwable?) -> Unit = {token,error ->
                        if (error!= null ){
                            Timber.e("failed to login :$error")
                        }
                        else if (token != null){
                            UserApiClient.instance.me { user, e ->
                                if (e != null) {
                                    Timber.e("사용자 정보 요청 실패 :${e.message}")
                                }
                                else if (user != null) {
                                    val request = LoginRequest(
                                        name = user.kakaoAccount?.profile?.nickname ?: "",
                                        imageUrl = user.kakaoAccount?.profile?.thumbnailImageUrl ?: "",
                                        socialId = user.id.toString(),
                                    )
                                        viewModel.onKakaoLogin(request)
                                }
                            }

                        }
                        else {
                            Timber.i("error to login :${token}")
                        }
                    }
                    UserApiClient.instance.loginWithKakaoAccount(
                        this@LoginActivity,
                        callback = callback
                    )
                }catch (e:Exception){
                    Timber.e("로그인 에러 :${e.message}")
                    e.printStackTrace()
                }
            }
        } catch (e:Exception) {
            e.printStackTrace()
            Timber.d("잡았단 에러 :${e.message}")
        }
    }
    }
