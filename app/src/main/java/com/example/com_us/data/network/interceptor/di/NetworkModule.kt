package com.example.com_us.data.network.interceptor.di

import android.content.Context
import android.util.Log
import com.example.com_us.R
import com.example.com_us.base.AppInterceptor
import com.example.com_us.data.di.BaseUrl
import com.example.com_us.data.repository.UserTokenRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.security.KeyManagementException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.Certificate
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.util.concurrent.TimeUnit
import javax.inject.Provider
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

// 모듈을 통해 객체 제공방법을 hilt에게 알려줄 수 있다.
// 생성자 삽입할 수 없는 결합
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        interceptor: AppInterceptor,

        ): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
        try {
            // Flow에서 최신 토큰을 관찰하고 인터셉터에 설정


            val cf = CertificateFactory.getInstance("X.509")
            val rawFileId = context.resources.getIdentifier("songhayeon", "raw", context.packageName)
            val caInput = context.resources.openRawResource(R.raw.comus)

            var ca: Certificate? = null
            try {
                ca = cf.generateCertificate(caInput)
            } catch (e: CertificateException) {
                e.printStackTrace()
            } finally {
                caInput.close()
            }
            if (ca != null) {
                val keyStoreType = KeyStore.getDefaultType()
                val keyStore = KeyStore.getInstance(keyStoreType)
                keyStore.load(null, null)
                keyStore.setCertificateEntry("ca", ca)

                val tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm()
                val tmf = TrustManagerFactory.getInstance(tmfAlgorithm)
                tmf.init(keyStore)

                val sslContext = SSLContext.getInstance("TLS")
                sslContext.init(null, tmf.trustManagers, null)

                if (tmf.trustManagers.isNotEmpty() && tmf.trustManagers.first() is X509TrustManager) {
                    okHttpClient.sslSocketFactory(sslContext.socketFactory, tmf.trustManagers.first() as X509TrustManager)
                    okHttpClient.hostnameVerifier { _, _ -> true }
                }
            }
        } catch (e: KeyStoreException) {
            Log.e("network error",e.message.toString())
            e.printStackTrace()
        }
        catch (e: CertificateException) {
            e.printStackTrace()
        }
        catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        catch (e: KeyManagementException){
            e.printStackTrace()
        }
        catch (e : IOException){
            e.printStackTrace()
        }


        return okHttpClient
            .addInterceptor(interceptor)
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                },
            ).connectTimeout(200, TimeUnit.SECONDS)
            .readTimeout(200, TimeUnit.SECONDS)
            .writeTimeout(200, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        @BaseUrl baseUrl: String,
        okHttpClient: OkHttpClient,
    ): Retrofit =
        Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}
