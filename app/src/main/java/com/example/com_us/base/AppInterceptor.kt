package com.example.com_us.base

import android.content.Intent
import android.util.Log
import com.example.com_us.data.repository.UserTokenRepository
import com.example.com_us.data.service.AuthService
import com.example.com_us.data.service.TokenService
import com.example.com_us.ui.login.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import javax.inject.Provider
import javax.net.ssl.HttpsURLConnection

class AppInterceptor @Inject constructor (
    private val dataStore: Provider<UserTokenRepository>,
    ) : Interceptor {
        private val contentType = "application/json"
        private var accessToken : String? = ""

    private fun errorResponse(request: Request): Response = Response.Builder()
        .request(request)
        .protocol(Protocol.HTTP_2)
        .message("")
        .body(ResponseBody.create(null, ""))
        .build()

    // todo : 얘는 언제 호출됨?
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain) : Response = runBlocking{
        val request = chain.request()

        // 인증이 필요없는 요청의 경우 : No AUTH 헤더를 확인하여 token을 넣지 않는다.
        if ("true" in request.headers.values("NO-AUTH")) {
            return@runBlocking chain.proceed(request)
        }

        //todo : access token 불러오기
                val token =
                    dataStore
                        .get()
                        .getAccessToken()
                        .firstOrNull()
                        ?.let { it.ifEmpty { null } }

        val response = chain.proceedWithToken(request,token)
        if (response.code != HttpsURLConnection.HTTP_UNAUTHORIZED || accessToken==null) {
            return@runBlocking  response
        }

                Timber.d("accesstoken $accessToken")


                // 인증이 필요없는 요청의 경우 : No AUTH 헤더를 확인하여 token을 넣지 않는다.
                if ("true" in request.headers.values("NO-AUTH")) {
                    return@runBlocking chain.proceed(request)
                }

               // val response = newRequest.let { proceed(it) }
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

    private fun Interceptor.Chain.proceedWithToken(
        request: Request,
        token : String?,
    ) : Response =
        request.newBuilder()
            .apply {
                if (token.isNullOrBlank().not()) {
                    addHeader("Content-Type", contentType)
                    addHeader(AUTHORIZATION, "Bearer $token")
                }
            }.build()
            .let(::proceed)

    companion object {
        private const val AUTHORIZATION = "Authorization"
    }

}