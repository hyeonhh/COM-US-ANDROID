package com.example.com_us.ui.question

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.com_us.data.ApiClient
import com.example.com_us.data.datasource.QuestionRemoteDataSource
import com.example.com_us.data.repository.QuestionRepository
import retrofit2.create
class QuestionViewModelFactory(private val context: Context): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(QuestionViewModel::class.java) -> {
                val repository = QuestionRepository(QuestionRemoteDataSource(ApiClient.getApiClient(context).create()))
                QuestionViewModel(repository) as T
            }
            else -> {
                throw IllegalArgumentException("Failed to create ViewModel : ${modelClass.name}")
            }
        }
    }
}