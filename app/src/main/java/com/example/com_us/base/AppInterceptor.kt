package com.example.com_us.base

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

    class AppInterceptor(private val accessToken: String) : Interceptor {
        private val contentType = "application/json"
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain) : Response = with(chain) {

            val request = chain .request()
            // 인증이 필요없는 요청의 경우 : No AUTH 헤더를 확인하여 token을 넣지 않는다.
            if ("true" in request.headers.values("NO-AUTH")) {
                return chain.proceed(request)
            }
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