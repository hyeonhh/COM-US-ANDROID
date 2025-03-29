package com.example.com_us.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.com_us.MainActivity
import com.example.com_us.base.activity.BaseActivity
import com.example.com_us.databinding.ActivitySplashBinding
import com.example.com_us.ui.login.LoginActivity
import com.kakao.sdk.auth.AuthApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>(
    ActivitySplashBinding::inflate
) {
    override val viewModel  : SplashViewModel by viewModels()

    override fun onBindLayout() {
        super.onBindLayout()
        Timber.d("SplashScreen")

         viewModel.homeEvent.observe(this@SplashActivity, {
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                lifecycleScope.launch {
                    delay(2000)
                    startActivity(intent)
                }
            })
            viewModel.loginEvent.observe(this@SplashActivity, {
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                lifecycleScope.launch {
                    delay(2000)
                    startActivity(intent)

                }
            })
    }
}