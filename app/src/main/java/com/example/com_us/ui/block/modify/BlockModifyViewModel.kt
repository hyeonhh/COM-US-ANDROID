package com.example.com_us.ui.block.modify

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.com_us.base.viewmodel.BaseViewModel
import com.example.com_us.data.model.block.SaveBlockRequest
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
class BlockModifyViewModel @Inject constructor(
    private val blockRepository: BlockRepository,
) : BaseViewModel() {

    // 블럭 배치하기
    fun putBlock(category: String) {
        when(category) {
            "DAILY" -> {
            }
        }
    }

    //현재 단계
    private val _currentLevel = MutableStateFlow(0)
    val currentLevel = _currentLevel.asStateFlow()
    // 블럭 배치 성공
    private val _blockEvent = SingleLiveEvent<Any>()
    val blockEvent: LiveData<Any>
        get() = _blockEvent

    private val _allBlockCompleteEvent = SingleLiveEvent<Any>()
    val allBlockCompleteEvent  : LiveData<Any> get() = _allBlockCompleteEvent

    private fun showCompleteScreen(){
        _allBlockCompleteEvent.call()
    }

    private val _blockList =
        mutableListOf(
        mutableListOf(0, 0, 0, 0),
        mutableListOf(0, 0, 0, 0),
        mutableListOf(0, 0, 0, 0),
        mutableListOf(0, 0, 0, 0)
    )

     fun deleteBlock() {
        viewModelScope.launch {
            blockRepository.deleteBlock().apply {
                if (this.status==200){
                    //todo : 블럭 지우기
                    getBlock()
                    getBlockCount()
                }
            }
        }
    }

    private fun checkAllBlockIsOne() : Boolean {
        return _blockList.all {row ->
            row.isNotEmpty() && row.all { it==1 }
        }
    }
    fun setBlock(rol : Int, col : Int){
        _blockList[rol][col] = 1
    }

    private fun saveBlock() {
        _blockEvent.call()
    }


    private val _familyBlockCount = MutableStateFlow(0)
    private val _friendBlockCount = MutableStateFlow(0)
    private val _dailyBlockCount = MutableStateFlow(0)
    private val _schoolBlockCount = MutableStateFlow(0)
    private val _interestBlockCount = MutableStateFlow(0)

    val familyBlockCount = _familyBlockCount.asStateFlow()

    val userFamilyBlockCount =  _familyBlockCount.asStateFlow()
    val userDailyBlockCount = _dailyBlockCount.asStateFlow()
    val userSchoolBlockCount = _schoolBlockCount.asStateFlow()
    val userHobbyBlockCount = _interestBlockCount.asStateFlow()



    val friendBlockCount = _friendBlockCount.asStateFlow()
    val dailyBlockCount = _dailyBlockCount.asStateFlow()
    val schoolBlockCount = _schoolBlockCount.asStateFlow()

    val interestBlockCount = _interestBlockCount.asStateFlow()


    private val _block = MutableStateFlow(Block())
    val block = _block.asStateFlow()

    init {
        getBlock()
        getBlockCount()
    }


    private fun getBlock() {
        viewModelScope.launch {
            blockRepository.getBlock()
                .onFailure {
                    Timber.e("failed to get block :${it.message}")
                }
                .onSuccess {
                    _block.value = it
                  //  _currentLevel.value = it.first().level
                    // 이미 채워진 블럭 위치도 1로 만들어주기

                    if (it.blocks!= null) {
                        it.blocks.forEach {
                            it.blockPlace.forEach {
                                setBlock(it.row, it.col)
                            }
                        }
                    }
                }

        }
    }

    private fun getBlockCount() {
        viewModelScope.launch {
            val result = blockRepository.getAvailableCount()
            when (result.status) {
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


    fun saveBlock(body: SaveBlockRequest) {
        viewModelScope.launch {
            blockRepository.saveBlock(body).apply {
                if (this.status == 200) {
                    saveBlock()
                    if(checkAllBlockIsOne()) {
                        showCompleteScreen()
                    }
                } else {
                    Timber.e("failed to ave block")
                }
            }
        }
    }
}