package com.example.com_us.data.di

import javax.inject.Qualifier

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class BaseUrl

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ModifyBaseUrl


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ModifyRetrofit


    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class AuthInterceptor

