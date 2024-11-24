package com.example.com_us.base

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

    class AppInterceptor(private val accessToken: String) : Interceptor {
        private val contentType = "application/json"
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain) : Response = with(chain) {
            val newRequest = request().newBuilder()
                .addHeader("Content-Type", contentType)
                .addHeader("Authorization", accessToken)
                .build()

            val response = proceed(newRequest)
            when (response.code) {
                401, 404 -> {
                    Log.d("API FAILURE", response.toString())
                }
                500 -> {
                    Log.d("API FAILURE 500", response.toString())
                }
            }
            response
        }
}