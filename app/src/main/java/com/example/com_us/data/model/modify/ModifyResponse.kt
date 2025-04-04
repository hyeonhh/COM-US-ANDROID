package com.example.com_us.data.model.modify

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


// data 필드 구조
@Serializable
data class ModifyResponse(
    //todo : SerialName이랑 SerializedName 차이가 뭐지 ?
    // SerializedName 로 하니까 제대로 됨 ..
    @SerializedName("converted_sentence")
    val convertedSentence: List<String> = emptyList(),
    @SerializedName("ksl_videos")
    val kslVideos: Map<String, VideoInfo>  = emptyMap()// 동적 키를 Map으로 처리
)

// 비디오 정보 구조
@Serializable
data class VideoInfo(
    @SerializedName("video_url")
    val videoUrl: String
)