package com.example.com_us.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.com_us.data.repository.HomeRepository
import com.example.com_us.data.response.question.ResponseHomeDataDto
import kotlinx.coroutines.launch

class HomeViewModel(private val homeRepository: HomeRepository) : ViewModel() {
    private val _homeData = MutableLiveData<ResponseHomeDataDto>()
    val homeData: LiveData<ResponseHomeDataDto> = _homeData

    fun loadHomeData(){
        viewModelScope.launch {
            homeRepository.getHomeData()
                .onSuccess {
                    _homeData.value = it
                    println(_homeData)
                }
                .onFailure {
                    Log.d("GET: [HOME DATA] DATA FAILURE", it.toString())
                }
        }
    }
}