package com.example.com_us.data.model.auth

import kotlinx.serialization.Serializable


@Serializable
data class LoginResponse (
    val accessToken : String,
    val refreshToken : String
)