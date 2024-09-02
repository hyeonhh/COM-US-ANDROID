package com.example.com_us.ui.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.com_us.data.ApiClient
import com.example.com_us.data.datasource.ProfileRemoteDataSource
import com.example.com_us.data.repository.ProfileRepository
import retrofit2.create
class ProfileViewModelFactory(private val context: Context): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                val repository = ProfileRepository(ProfileRemoteDataSource(ApiClient.getApiClient(context).create()))
                ProfileViewModel(repository) as T
            }
            else -> {
                throw IllegalArgumentException("Failed to create ViewModel : ${modelClass.name}")
            }
        }
    }
}