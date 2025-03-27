package com.example.com_us.data.repository.impl

import com.example.com_us.data.source.LikeDataSource
import javax.inject.Inject

class LikeRepository @Inject constructor(
    private val likeDataSource: LikeDataSource
){
    suspend fun setLike(questionId: String) = likeDataSource.setLike(questionId)
    suspend fun setUnlike(questionId : String) = likeDataSource.setUnlike(questionId)
}