package com.example.com_us.base

import android.content.Context
import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.security.KeyStore
import java.security.cert.Certificate
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

object ApiClient {
    private const val contentType = "application/json"
    class AppInterceptor(private val context: Context, private val accessToken: String) : Interceptor {
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
}