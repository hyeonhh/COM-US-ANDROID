package com.example.com_us.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.com_us.data.ApiClient
import com.example.com_us.data.datasource.HomeRemoteDataSource
import com.example.com_us.data.datasource.QuestionRemoteDataSource
import com.example.com_us.data.repository.HomeRepository
import com.example.com_us.data.repository.QuestionRepository
import retrofit2.create
class HomeViewModelFactory(private val context: Context): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                val repository = HomeRepository(HomeRemoteDataSource(ApiClient.getApiClient(context).create()))
                HomeViewModel(repository) as T
            }
            else -> {
                throw IllegalArgumentException("Failed to create ViewModel : ${modelClass.name}")
            }
        }
    }
}