package com.example.com_us.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.com_us.data.repository.ProfileRepository
import com.example.com_us.data.response.question.ResponseProfileDto
import kotlinx.coroutines.launch

class ProfileViewModel(private val profileRepository: ProfileRepository) : ViewModel() {

    private val _profileData = MutableLiveData<ResponseProfileDto>()
    val profileData: LiveData<ResponseProfileDto> = _profileData

    fun loadProfileData(){
        viewModelScope.launch {
            profileRepository.getProfileData()
                .onSuccess {
                    _profileData.value = it
                }
                .onFailure {
                    Log.d("GET: [PROFILE DATA] DATA FAILURE", it.toString())
                }
        }
    }
}