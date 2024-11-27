package com.example.com_us.ui.login

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.com_us.base.activity.BaseActivity
import com.example.com_us.databinding.ActivityLoginBinding
import com.example.com_us.databinding.ActivityQuestionCheckAnswerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding,LoginViewModel>(
    ActivityLoginBinding::inflate
) {
    override val viewModel : LoginViewModel by viewModels()
}