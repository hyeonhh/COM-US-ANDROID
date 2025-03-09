package com.example.com_us.ui

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class Application : Application() {
    override fun onCreate() {
        setupTimber()
        super.onCreate()

        KakaoSdk.init(this, "e5844817c45bafa20ebc8beed669f78e")
    }
    private fun setupTimber() {
        Timber.plant(Timber.DebugTree())
    }
}