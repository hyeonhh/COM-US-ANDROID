package com.example.com_us.data.service

import com.example.com_us.base.data.BaseResponse
import com.example.com_us.data.model.modify.ModifyRequest
import com.example.com_us.data.model.modify.ModifyResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ModifyService {
    @POST("/ksl/translate")
    @Headers("NO-AUTH: true")
    suspend fun getModifyAnswer(
        @Body body : ModifyRequest) : BaseResponse<ModifyResponse>
}