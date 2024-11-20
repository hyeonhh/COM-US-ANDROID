package com.example.com_us.data.default_repository

import com.example.com_us.data.model.question.request.RequestAnswerRequest
import com.example.com_us.data.model.question.response.question.ResponseAnswerDetailDto
import com.example.com_us.data.model.question.response.question.ResponseAnswerDetailWithDateDto
import com.example.com_us.data.model.question.response.question.ResponsePreviousAnswerDto
import com.example.com_us.data.model.question.response.question.ResponseQuestionDetailDto
import com.example.com_us.data.model.question.response.question.ResponseQuestionDto
import com.example.com_us.data.repository.QuestionRepository
import com.example.com_us.data.default_source.DefaultQuestionDataSource
import javax.inject.Inject

class DefaultQuestionRepository @Inject constructor(
    private val defaultQuestionDataSource: DefaultQuestionDataSource
)  : QuestionRepository {
   override suspend fun getQuestionListByCate(category: String): Result<List<ResponseQuestionDto>> {
        return try{
            defaultQuestionDataSource.getQuestionListByCate(category).toResult()
        } catch (e: Exception) {
            Result.failure(NetworkError.NetworkException(e))
        }
    }
   override suspend fun getQuestionDetail(questionId: Long): Result<ResponseQuestionDetailDto> {
       return try{
           defaultQuestionDataSource.getQuestionDetail(questionId).toResult()
       } catch (e: Exception) {
           Result.failure(NetworkError.NetworkException(e))
       }
    }
    override suspend fun getAnswerDetail(answer: String): Result<List<ResponseAnswerDetailDto>> {
        return try{
            defaultQuestionDataSource.getAnswerDetail(answer).toResult()
        } catch (e: Exception) {
            Result.failure(NetworkError.NetworkException(e))
        }
    }
   override suspend fun postAnswer(body: RequestAnswerRequest): Result<ResponseAnswerDetailWithDateDto> {
       return try{
           defaultQuestionDataSource.postAnswer(body).toResult()
       } catch (e: Exception) {
           Result.failure(NetworkError.NetworkException(e))
       }
    }
   override suspend fun getPreviousAnswer(questionId: Long ): Result<ResponsePreviousAnswerDto> {
       return try{
           defaultQuestionDataSource.getPreviousAnswer(questionId).toResult()
       } catch (e: Exception) {
           Result.failure(NetworkError.NetworkException(e))
       }
    }
}