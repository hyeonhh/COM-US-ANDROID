package com.example.com_us.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.com_us.data.repository.ProfileRepository
import com.example.com_us.data.model.question.response.question.ResponseProfileDto
import com.example.com_us.util.ServerResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val profileRepository: ProfileRepository) : ViewModel() {

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