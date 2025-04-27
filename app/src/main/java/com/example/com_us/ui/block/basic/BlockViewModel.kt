package com.example.com_us.ui.block.basic

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.com_us.base.viewmodel.BaseViewModel
import com.example.com_us.data.model.block.BlockCountResponse
import com.example.com_us.data.model.home.Block
import com.example.com_us.data.repository.BlockRepository
import com.example.com_us.ui.event.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BlockViewModel @Inject constructor(
    private val blockRepository: BlockRepository,
):BaseViewModel() {

    private val _block = MutableStateFlow(Block())
    val block = _block.asStateFlow()

    init {
        getBlock()
        getBlockCount()
    }

    //현재 단계
    private val _currentLevel = MutableStateFlow(0)
    val currentLevel = _currentLevel.asStateFlow()

    private val _familyBlockCount = MutableStateFlow(0)
    private val _friendBlockCount = MutableStateFlow(0)
    private val _dailyBlockCount = MutableStateFlow(0)
    private val _schoolBlockCount = MutableStateFlow(0)
    private val _interestBlockCount = MutableStateFlow(0)

     val familyBlockCount = _familyBlockCount.asStateFlow()
     val friendBlockCount = _friendBlockCount.asStateFlow()
     val dailyBlockCount = _dailyBlockCount.asStateFlow()
     val schoolBlockCount = _schoolBlockCount.asStateFlow()

     val interestBlockCount = _interestBlockCount.asStateFlow()

     private val _blockList =
         MutableStateFlow(
        mutableListOf(
            mutableListOf(0, 0, 0, 0),
            mutableListOf(0, 0, 0, 0),
            mutableListOf(0, 0, 0, 0),
            mutableListOf(0, 0, 0, 0)
        )
         )

    val blockList = _blockList.asStateFlow()

    fun setBlock(rol : Int, col : Int){
        _blockList.value[rol][col] = 1
        Timber.d("block list :$_blockList")
    }

    private val _allBlockCompleteEvent = SingleLiveEvent<Any>()
    val allBlockCompleteEvent  : LiveData<Any> get() = _allBlockCompleteEvent

    private fun showCompleteScreen(){
        _allBlockCompleteEvent.call()
        _currentLevel.value += 1
    }


    private fun checkAllBlockIsOne() : Boolean {
        return _blockList.value.all{row ->
            row.isNotEmpty() && row.all { it==1 }
        }
    }

    private fun getBlock(){
        viewModelScope.launch {
            blockRepository.getBlock()
                .onFailure {
                    Timber.e("failed to get block :${it.message}")
                }
                .onSuccess {
                    _block.value = it
                    if (it.blocks!= null) {
                        it.blocks.forEach {
                            it.blockPlace.forEach {
                                setBlock(it.row, it.col)
                            }
                        }
                    }
                    if(checkAllBlockIsOne()) {
                        showCompleteScreen()
                    }
                }

        }
    }
    private fun getBlockCount() {
        viewModelScope.launch {
          val result =   blockRepository.getAvailableCount()
            when(result.status) {
                200 -> {
                    _familyBlockCount.value = result.data?.familyBlockCount ?: 0
                    _friendBlockCount.value = result.data?.friendBlockCount ?: 0
                    _dailyBlockCount.value = result.data?.dailyBlockCount ?: 0
                    _schoolBlockCount.value = result.data?.schoolBlockCount ?: 0
                    _interestBlockCount.value = result.data?.hobbyBlockCount ?: 0
                }
            }

        }
    }

}