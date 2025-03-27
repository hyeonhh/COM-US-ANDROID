package com.example.com_us.ui.block.modify

import android.view.View
import androidx.fragment.app.viewModels
import com.example.com_us.R
import com.example.com_us.base.fragment.BaseFragment
import com.example.com_us.databinding.FragmentBlockModifyBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class BlockModifyFragment : BaseFragment<FragmentBlockModifyBinding, BlockModifyViewModel>(
    FragmentBlockModifyBinding::inflate
) {
    override val viewModel: BlockModifyViewModel by viewModels()
    private var currentType: String? = null

    private var rotateDegree = intArrayOf(-90, -180, -270,0)
    private var clickCount = 0
    private var selectedDegree = 0

    private var block = mutableListOf(
        mutableListOf(0, 0, 0, 0),
        mutableListOf(0, 0, 0, 0),
        mutableListOf(0, 0, 0, 0),
        mutableListOf(0, 0, 0, 0)
    )

    private var blockView : List<List<View>> = emptyList()
    private fun onClickBlockBox(type: String, resId: Int) {
        with(binding) {
            currentType = type
            rotateToLeft.visibility = View.VISIBLE
            rotateToRight.visibility = View.VISIBLE
            selectedDegree=0
            ivBlockRotate.rotation = selectedDegree.toFloat()
            ivBlockRotate.visibility = View.VISIBLE
            ivBlockRotate.setImageDrawable(resources.getDrawable(resId))

            rotateToLeft.setOnClickListener {
                clickCount += 1
                if (clickCount >= 5) clickCount -= 4
                ivBlockRotate.rotation = rotateDegree[clickCount-1].toFloat()

                selectedDegree = rotateDegree[clickCount-1]
                Timber.d("선택된 대화 주머니 :$currentType, degree:$selectedDegree")

            }
            rotateToRight.setOnClickListener {
                clickCount += 1
                if (clickCount >= 5) clickCount -= 4
                selectedDegree = rotateDegree[clickCount - 1]
                ivBlockRotate.rotation = rotateDegree[clickCount - 1].toFloat()
            }
        }
    }

    private fun setDailyBlock(row :Int, col : Int){
        if (row >=4 || row <0 || col>=4 || col<0) {
            onRetry()
            return
        }
        binding.warnText.visibility = View.INVISIBLE
        blockView[row][col].setBackgroundColor(resources.getColor(R.color.orange_700))
        currentType = null
    }

    private fun setInterestBlock(row :Int, col : Int, selectedDegree: Int) {
        if (selectedDegree==0 || selectedDegree==-180){
            if (row >=4 || row <0 || col>=4 || col<0 || row +1 >=4) {
                onRetry()
                return
            }
                if ((block[row][col] ==1) ||(block[row+1][col]==1) ) {
                onRetry()
                return
            }
            binding.warnText.visibility = View.INVISIBLE
            block[row][col]=1
            block[row+1][col]=1
            blockView[row][col].setBackgroundColor(resources.getColor(R.color.interest_block))
            blockView[row+1][col].setBackgroundColor(resources.getColor(R.color.interest_block))
            currentType = null
        }
        if (selectedDegree==-90 || selectedDegree==-270){
            if (row >=4 || row <0 || col>=4 || col<0 || col +1 >=4) {
                onRetry()
            }
                if ((block[row][col] ==1) ||(block[row][col+1]==1) ) {
                onRetry()
                return
            }
            binding.warnText.visibility = View.INVISIBLE
            block[row][col]=1
            block[row][col+1]=1
            blockView[row][col].setBackgroundColor(resources.getColor(R.color.interest_block))
            blockView[row][col+1].setBackgroundColor(resources.getColor(R.color.interest_block))
            currentType = null
        }
    }
    private fun setFamilyBlock(row :Int, col : Int, selectedDegree: Int){
        if(selectedDegree==0) {
            if (row >=4 || row <0 || col>=4 || col<0 || row +1 >=4 || col+1 >=4 || col+2>=4) {
                onRetry()
                return
            }
            if ((block[row][col] ==1) ||(block[row][col+2]==1) || (block[row][col+1]==1) || (block[row+1][col+1]==1) ) {
                onRetry()
                return
            }
            binding.warnText.visibility = View.INVISIBLE
            block[row][col] = 1
            block[row][col+1] = 1
            block[row][col+2] = 1
            block[row+1][col+1] = 1

            blockView[row][col].setBackgroundColor(resources.getColor(R.color.purple_700))
            blockView[row][col+1].setBackgroundColor(resources.getColor(R.color.purple_700))
            blockView[row][col+2].setBackgroundColor(resources.getColor(R.color.purple_700))
            blockView[row+1][col+1].setBackgroundColor(resources.getColor(R.color.purple_700))

            currentType = null
        }
        if (selectedDegree == -90) {
            if (row >=4 || row <0 || col>=4 || col<0  || row+1>=4 || row+2>=4 || col+1>=4) {
                onRetry()
                return
            }
            if ((block[row][col] ==1) ||( block[row+1][col]==1) || (block[row+2][col]==1)||block[row+1][col+1]==1 ) {
                onRetry()
                return
            }
            binding.warnText.visibility = View.INVISIBLE
            block[row][col] = 1
            block[row+1][col] = 1
            block[row+2][col] = 1
            block[row+1][col+1] = 1

            blockView[row][col].setBackgroundColor(resources.getColor(R.color.purple_700))
            blockView[row+1][col].setBackgroundColor(resources.getColor(R.color.purple_700))
            blockView[row+2][col].setBackgroundColor(resources.getColor(R.color.purple_700))
            blockView[row+1][col+1].setBackgroundColor(resources.getColor(R.color.purple_700))

            currentType = null
        }
        if(selectedDegree==-180) {
            if (row >=4 || row <0 || col>=4 || col<0 || row +1 >=4 || row-1<0 || col+1 >=4  || col+2>=4 ) {
                onRetry()
                return
            }
            if ((block[row][col] ==1) ||(block[row-1][col+1]==1) || (block[row+1][col+1] ==1) ||  block[row+1][col+2]==1) {
                onRetry()
                return
            }
            binding.warnText.visibility = View.INVISIBLE
            block[row][col] = 1
            block[row-1][col+1] = 1
            block[row+1][col+1] = 1
            block[row+1][col+2] = 1

            blockView[row][col].setBackgroundColor(resources.getColor(R.color.purple_700))
            blockView[row-1][col+1].setBackgroundColor(resources.getColor(R.color.purple_700))
            blockView[row+1][col+1].setBackgroundColor(resources.getColor(R.color.purple_700))
            blockView[row+1][col+2].setBackgroundColor(resources.getColor(R.color.purple_700))

            currentType = null
        }
        if (selectedDegree==-270){
            if (row >=4 || row <0 || col>=4 || col<0 || row +1 >=4 || col+1 >=4 || row-1<0 ) {
                onRetry()
                return
            }
            if ((block[row][col] ==1) ||( block[row-1][col+1] ==1) || (block[row][col+1] ==1) ||  block[row+1][col+1]==1) {
                onRetry()
                return
            }
            binding.warnText.visibility = View.INVISIBLE
            block[row][col] = 1
            block[row-1][col+1] = 1
            block[row][col+1] = 1
            block[row+1][col+1] = 1

            blockView[row][col].setBackgroundColor(resources.getColor(R.color.purple_700))
            blockView[row-1][col+1].setBackgroundColor(resources.getColor(R.color.purple_700))
            blockView[row][col+1].setBackgroundColor(resources.getColor(R.color.purple_700))
            blockView[row+1][col+1].setBackgroundColor(resources.getColor(R.color.purple_700))

            currentType = null
        }

    }

    private fun onRetry(){
        selectedDegree =0
        binding.warnText.visibility = View.VISIBLE
        binding.ivBlockRotate.visibility= View.VISIBLE
        binding.ivBlockRotate.rotation = selectedDegree.toFloat()
        binding.rotateToLeft.visibility = View.VISIBLE
        binding.rotateToRight.visibility = View.VISIBLE
    }

    private fun setFriendBlock(row :Int, col : Int, degree: Int){
        if (degree ==0 || degree==-180) {
            if (row >=4 || row <0 || col>=4 || col<0 || row +1 >=4 || col+1 >=4 || row+2>=4) {
                onRetry()
                return
            }
            if ((block[row][col] ==1) ||( block[row+1][col]==1) || (block[row+1][col+1]==1)|| block[row+2][col+1]==1)  {
                onRetry()
                return
            }
            binding.warnText.visibility = View.INVISIBLE
            block[row][col]=1
            block[row+1][col]=1
            block[row+1][col+1]=1
            block[row+2][col+1]=1

            blockView[row][col].setBackgroundColor(resources.getColor(R.color.green_700))
            blockView[row+1][col].setBackgroundColor(resources.getColor(R.color.green_700))
            blockView[row+1][col+1].setBackgroundColor(resources.getColor(R.color.green_700))
            blockView[row+2][col+1].setBackgroundColor(resources.getColor(R.color.green_700))
            currentType=null
        }

        if(degree == -90 || degree==-270) {
            if (row >=4 || row <0 || col>=4 || col<0  || col+1 >=4 || col+2>=4 || row-1<0) {
                onRetry()
                return
            }
            if ((block[row][col] ==1) ||( block[row][col+1]==1) || (block[row-1][col+1]==1)|| block[row-1][col+2]==1)  {
                onRetry()
                return
            }
            binding.warnText.visibility = View.INVISIBLE
            block[row][col]=1
            block[row][col+1]=1
            block[row-1][col+1]=1
            block[row-1][col+2]=1

            blockView[row][col].setBackgroundColor(resources.getColor(R.color.green_700))
            blockView[row][col+1].setBackgroundColor(resources.getColor(R.color.green_700))
            blockView[row-1][col+1].setBackgroundColor(resources.getColor(R.color.green_700))
            blockView[row-1][col+2].setBackgroundColor(resources.getColor(R.color.green_700))
            currentType=null

        }

    }
    private fun setSchoolBlock(row :Int, col : Int, degree: Int){
        if(degree==0) {
            if (row >=4 || row <0 || col>=4 || col<0 || row+1 >=4 || col+1 >=4) {
              onRetry()
                return
            }
            if ((block[row][col] ==1) ||(block[row+1][col]==1) || (block[row+1][col+1]==1)) {
                onRetry()
                return
            }
            block[row][col] = 1
            block[row+1][col] = 1
            block[row+1][col+1] = 1
            blockView[row][col].setBackgroundColor(resources.getColor(R.color.blue_700))
            blockView[row+1][col].setBackgroundColor(resources.getColor(R.color.blue_700))
            blockView[row+1][col+1].setBackgroundColor(resources.getColor(R.color.blue_700))
            currentType = null
            selectedDegree=0
        }
        if (degree == -90) {
            if (row >=4 || row <0 || col>=4 || col<0 || col+1 >=4 || row-1<0) {
                onRetry()
                return
            }
            if ((block[row][col] ==1) ||(block[row-1][col+1]==1) || (block[row][col+1]==1)) {
                onRetry()
                return
            }
            binding.warnText.visibility = View.INVISIBLE
            block[row][col] = 1
            block[row-1][col+1] = 1
            block[row][col+1] = 1

            blockView[row][col].setBackgroundColor(resources.getColor(R.color.blue_700))
            blockView[row-1][col+1].setBackgroundColor(resources.getColor(R.color.blue_700))
            blockView[row][col+1].setBackgroundColor(resources.getColor(R.color.blue_700))
            currentType = null
            selectedDegree=0
        }
        if(degree==-180) {
            if (row >=4 || row <0 || col>=4 || col<0 || row+1 >=4 || col+1 >=4) {
                onRetry()
                return
            }
            if ((block[row][col] ==1) ||(block[row][col+1]==1) || (block[row+1][col+1]==1)) {
                onRetry()
                return
            }
            binding.warnText.visibility = View.INVISIBLE
            block[row][col] = 1
            block[row][col+1] = 1
            block[row+1][col+1] = 1

            blockView[row][col].setBackgroundColor(resources.getColor(R.color.blue_700))
            blockView[row][col+1].setBackgroundColor(resources.getColor(R.color.blue_700))
            blockView[row+1][col+1].setBackgroundColor(resources.getColor(R.color.blue_700))
            currentType = null
            selectedDegree=0
        }
        if (degree==-270){
            if (row >=4 || row <0 || col>=4 || col<0 || row+1 >=4 || col+1 >=4) {
                onRetry()
                return
            }
            if ((block[row][col] ==1) ||(block[row][col+1]==1) || (block[row+1][col]==1)) {
                onRetry()
                return
            }
            binding.warnText.visibility = View.INVISIBLE
            block[row][col] = 1
            block[row][col+1] = 1
            block[row+1][col] = 1

            blockView[row][col].setBackgroundColor(resources.getColor(R.color.blue_700))
            blockView[row][col+1].setBackgroundColor(resources.getColor(R.color.blue_700))
            blockView[row+1][col].setBackgroundColor(resources.getColor(R.color.blue_700))

            currentType = null
            selectedDegree=0
        }

    }

    private fun onSetBlock(blockType : String, degree :Int, row : Int, col : Int){
        binding.rotateToLeft.visibility = View.INVISIBLE
        binding.rotateToRight.visibility = View.INVISIBLE
        binding.ivBlockRotate.visibility = View.INVISIBLE
        when(blockType){
            "school" -> setSchoolBlock(row,col,degree)
            "daily" ->  setDailyBlock(row,col)
            "family" -> setFamilyBlock(row,col,degree)
            "friend" -> setFriendBlock(row,col,degree)
            "interest" -> setInterestBlock(row, col, degree)
        }
    }

    override fun onBindLayout() {
        super.onBindLayout()
        blockView = listOf(
            listOf(binding.includeBlock.block1, binding.includeBlock.block2, binding.includeBlock.block3,binding.includeBlock.block4),
            listOf(binding.includeBlock.block5, binding.includeBlock.block6, binding.includeBlock.block7,binding.includeBlock.block8),
            listOf(binding.includeBlock.block9, binding.includeBlock.block10, binding.includeBlock.block11,binding.includeBlock.block12),
            listOf(binding.includeBlock.block13, binding.includeBlock.block14, binding.includeBlock.block15,binding.includeBlock.block16),
        )

        with(binding) {

            includeBlock.block1.setOnClickListener {
                onSetBlock(currentType!!, selectedDegree, 0, 0)
            }
            includeBlock.block2.setOnClickListener {
                currentType?.let {
                    onSetBlock(it,selectedDegree,0,1)
                }
            }
            includeBlock.block3.setOnClickListener {
                    currentType?.let {
                        onSetBlock(it,selectedDegree,0,2)
                    }
                }
            includeBlock.block4.setOnClickListener {
                currentType?.let {
                    onSetBlock(it,selectedDegree,0,3)
                }
            }

            includeBlock.block5.setOnClickListener {
                currentType?.let {
                    onSetBlock(it,selectedDegree,1,0)
                }
            }

            includeBlock.block6.setOnClickListener {
                currentType?.let {
                    onSetBlock(it,selectedDegree,1,1)
                }
            }

            includeBlock.block7.setOnClickListener {
                currentType?.let {
                    onSetBlock(it,selectedDegree,1,2)
                }
            }

            includeBlock.block8.setOnClickListener {
                currentType?.let {
                    onSetBlock(it,selectedDegree,1,3)
                }
            }

            includeBlock.block9.setOnClickListener {
                currentType?.let {
                    onSetBlock(it,selectedDegree,2,0)
                }
            }


            includeBlock.block10.setOnClickListener {
                currentType?.let {
                    onSetBlock(it,selectedDegree,2,1)
                }
            }
            includeBlock.block11.setOnClickListener {
                currentType?.let {
                    onSetBlock(it,selectedDegree,2,2)
                }
            }

            includeBlock.block12.setOnClickListener {
                currentType?.let {
                    onSetBlock(it,selectedDegree,2,3)
                }
            }
            includeBlock.block13.setOnClickListener {
                currentType?.let {
                    onSetBlock(it,selectedDegree,3,0)
                }
            }

            includeBlock.block14.setOnClickListener {
                currentType?.let {
                    onSetBlock(it,selectedDegree,3,1)
                }
            }

            includeBlock.block15.setOnClickListener {
                currentType?.let {
                    onSetBlock(it,selectedDegree,3,2)
                }
            }
            includeBlock.block16.setOnClickListener {
                currentType?.let {
                    onSetBlock(it,selectedDegree,3,3)
                }
            }

            // 대화 주머니 클릭
            includeInfo.firstBlock.setOnClickListener {
                    it.isSelected = !it.isSelected

                    if (it.isSelected) {
                        includeInfo.secondBlock.isSelected = false
                        includeInfo.thirdBlock.isSelected = false
                        includeInfo.fourthBlock.isSelected = false
                        includeInfo.fifthBlock.isSelected = false

                        onClickBlockBox("daily", R.drawable.block_daily_v2)
                    }
                }
                includeInfo.secondBlock.setOnClickListener {
                    it.isSelected = !it.isSelected
                    if (it.isSelected) {
                        includeInfo.firstBlock.isSelected = false
                        includeInfo.thirdBlock.isSelected = false
                        includeInfo.fourthBlock.isSelected = false
                        includeInfo.fifthBlock.isSelected = false
                        onClickBlockBox("school", R.drawable.block_school_v2)
                    }
                }
                includeInfo.thirdBlock.setOnClickListener {
                    it.isSelected = !it.isSelected
                    if (it.isSelected) {
                        includeInfo.firstBlock.isSelected = false
                        includeInfo.secondBlock.isSelected = false
                        includeInfo.fourthBlock.isSelected = false
                        includeInfo.fifthBlock.isSelected = false

                        onClickBlockBox("interest", R.drawable.block_interest_v2)
                    }
                }
                includeInfo.fourthBlock.setOnClickListener {
                    it.isSelected = !it.isSelected
                    if (it.isSelected) {
                        includeInfo.firstBlock.isSelected = false
                        includeInfo.secondBlock.isSelected = false
                        includeInfo.thirdBlock.isSelected = false
                        includeInfo.fifthBlock.isSelected = false

                        onClickBlockBox("family", R.drawable.block_family_v2)
                    }
                }
                includeInfo.fifthBlock.setOnClickListener {
                    it.isSelected = !it.isSelected
                    if (it.isSelected) {
                        includeInfo.secondBlock.isSelected = false
                        includeInfo.thirdBlock.isSelected = false
                        includeInfo.fourthBlock.isSelected = false
                        includeInfo.firstBlock.isSelected = false

                        onClickBlockBox("friend", R.drawable.block_friend_v2)
                    }
                }
            }
        }
    }