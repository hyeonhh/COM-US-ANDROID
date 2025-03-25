package com.example.com_us.base

import android.util.Log
import com.example.com_us.data.repository.UserTokenRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import javax.inject.Provider

class AppInterceptor @Inject constructor (
    private val dataStore: Provider<UserTokenRepository>,
    ) : Interceptor {
        private val contentType = "application/json"
        private var accessToken : String? = ""
        private var refreshToken : String? = ""


        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain) : Response = with(chain) {
            CoroutineScope(Dispatchers.IO).launch {
                //todo : access token 불러오기
                accessToken =
                    dataStore
                        .get()
                        .getAccessToken()
                        .first()
                        .let { it.ifEmpty { null } }

                Timber.d("accesstoken $accessToken")

                refreshToken =
                    dataStore.get()
                        .getRefreshToken()
                        .first()
                    .let { it.ifEmpty { null } }
                }

                val request = chain.request()
                // 인증이 필요없는 요청의 경우 : No AUTH 헤더를 확인하여 token을 넣지 않는다.
                if ("true" in request.headers.values("NO-AUTH")) {
                    return chain.proceed(request)
                }
                val newRequest = accessToken?.let {
                    request().newBuilder()
                        .addHeader("Content-Type", contentType)
                        .addHeader("Authorization", "Bearer $it")
                        .build()
                }


                val response = newRequest?.let { proceed(it) }
                when (response?.code) {
                    401, 404 -> {
                        Log.d("API FAILURE", response.toString())
                    }

                    500 -> {
                        Log.d("API FAILURE 500", response.toString())
                    }
                }
            response!!
            }
}