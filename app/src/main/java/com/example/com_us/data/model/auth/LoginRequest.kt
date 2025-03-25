package com.example.com_us.data.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val name : String,
    val imageUrl : String,
    val socialType : String = "KAKAO",
    val socialId : String
)
