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
import com.example.com_us.databinding.ActivityLoginBinding
import com.example.com_us.databinding.ActivityQuestionCheckAnswerBinding
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding,LoginViewModel>(
    ActivityLoginBinding::inflate
) {
    override val viewModel : LoginViewModel by viewModels()
    private lateinit var credentialManager: CredentialManager




    override fun onBindLayout() {
         val googleIdOption : GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(getString(R.string.googleClientApi))
            .setAutoSelectEnabled(true) // 재방문 사용자 자동 로그인
            .build()

         val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        credentialManager = CredentialManager.create(this@LoginActivity)
        super.onBindLayout()
        binding.btnLogin.setOnClickListener {
                lifecycleScope.launch {
                    val result = credentialManager.getCredential(
                        this@LoginActivity,
                        request
                    )
                    viewModel.handleSignIn(result)
                }
        }
    }
}