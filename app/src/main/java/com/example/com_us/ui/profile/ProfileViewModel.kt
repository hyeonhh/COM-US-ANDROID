package com.example.com_us.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.com_us.data.repository.ProfileRepository
import com.example.com_us.data.response.question.ResponseProfileDto
import com.example.com_us.util.ServerResponseHandler
import kotlinx.coroutines.launch

class ProfileViewModel(private val profileRepository: ProfileRepository) : ViewModel() {

    var serverResponseHandler: ServerResponseHandler? = null

    private val _profileData = MutableLiveData<ResponseProfileDto>()
    val profileData: LiveData<ResponseProfileDto> = _profileData

    fun loadProfileData(){
        viewModelScope.launch {
            profileRepository.getProfileData()
                .onSuccess {
                    _profileData.value = it
                    serverResponseHandler?.onServerSuccess()
                }
                .onFailure {
                    Log.d("GET: [PROFILE DATA]", it.toString())
                    serverResponseHandler?.onServerFailure()
                }
        }
    }
}