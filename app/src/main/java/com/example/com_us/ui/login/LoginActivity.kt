package com.example.com_us.ui.login

import android.content.Intent
import android.widget.Toast
import androidx.activity.viewModels
import com.example.com_us.MainActivity
import com.example.com_us.base.activity.BaseActivity
import com.example.com_us.data.model.auth.LoginRequest
import com.example.com_us.databinding.ActivityLoginBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthError
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding,LoginViewModel>(
    ActivityLoginBinding::inflate
) {
    override val viewModel : LoginViewModel by viewModels()

    override fun onBindLayout() {
        super.onBindLayout()
        binding.btnKakaoLogin.setOnClickListener { onKakaoLogin() }

          viewModel.homeEvent.observe(this@LoginActivity, {
              val intent = Intent(this@LoginActivity, MainActivity::class.java)
              startActivity(intent)
          })

    }

    val callback : (OAuthToken?, Throwable?) -> Unit = {token,error ->
        if (error!= null ){
            Toast.makeText(this@LoginActivity,"$error", Toast.LENGTH_LONG).show()
            if (error is AuthError && error.statusCode == 302) {
                onKakaoAccountLogin()
            }
            if (error is ClientError && error.reason==ClientErrorCause.Cancelled) {
                Timber.e("사용자 로그인 취소")
            }
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

    private fun onKakaoAccountLogin() {
            try {
                UserApiClient.instance.loginWithKakaoAccount(
                    this@LoginActivity,
                    callback = callback
                )

            }catch (e:Exception){
                e.printStackTrace()
            }
    }

    private fun onKakaoLogin(){
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this@LoginActivity)) {
                Timber.d("카카오톡 설치여부 ? yes")
                try {
                UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)

                 }
                catch (e:Exception){
                     e.printStackTrace()
                }
            }
        else {
            onKakaoAccountLogin()
            }
        }
    }

