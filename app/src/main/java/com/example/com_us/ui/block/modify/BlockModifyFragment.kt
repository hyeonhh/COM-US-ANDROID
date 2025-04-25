package com.example.com_us.ui.block.modify


import android.content.ClipData
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.view.DragEvent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.util.component1
import androidx.core.util.component2
import androidx.core.view.DragStartHelper
import androidx.core.view.children
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.com_us.R
import com.example.com_us.base.fragment.BaseFragment
import com.example.com_us.data.model.block.BlockPlace
import com.example.com_us.data.model.block.SaveBlockRequest
import com.example.com_us.data.model.home.Block
import com.example.com_us.databinding.FragmentBlockModifyBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.ceil
import kotlin.math.sqrt

data class CellInfo(val row: Int, val column: Int)

@AndroidEntryPoint
class BlockModifyFragment : BaseFragment<FragmentBlockModifyBinding, BlockModifyViewModel>(
    FragmentBlockModifyBinding::inflate
) {
    private val args by navArgs<BlockModifyFragmentArgs>()
    private lateinit var blockList: List<List<View>>
    override val viewModel: BlockModifyViewModel by viewModels()
    private var currentType: String? = null

    private var dailyBlockCount = 0
    private var schoolBlockCount = 0
    private var hobbyBlockCount = 0
    private var familyBlockCount  = 0
    private var friendBlockCount = 0
    private var row : Int = 0
    private var col : Int = 0

    private var level = 0
    private var rotateDegree = intArrayOf(-90, -180, -270, 0)
    private var clickCount = 0
    private var selectedDegree = 0

    private var block = mutableListOf(
        mutableListOf(0, 0, 0, 0),
        mutableListOf(0, 0, 0, 0),
        mutableListOf(0, 0, 0, 0),
        mutableListOf(0, 0, 0, 0)
    )
    private var selectedBlock = arrayListOf<Pair<Int, Int>>()

    private var blockView: List<List<View>> = emptyList()

    private fun  showCompleteToast() {
        Toast.makeText(requireContext(), "블럭을 모두 배치했어요 !", Toast.LENGTH_SHORT).show()
    }
    private fun setGridDragListener() {
        binding.includeBlock.gridLayout.setOnDragListener { view, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    // 드래그 시작 - MIME 타입 확인
                    event.clipDescription.hasMimeType(MIMETYPE_TEXT_PLAIN)
                }

                DragEvent.ACTION_DRAG_ENTERED -> {
                    // 드래그가 그리드 영역에 들어옴
                    Timber.d("ACTION_DRAG_ENTERED")
                    true
                }

                DragEvent.ACTION_DRAG_LOCATION -> {
                    binding.ivBlockRotate.visibility = View.INVISIBLE

                    val gridLayout = binding.includeBlock.gridLayout
                    // 드래그 중 - 현재 위치에 따라 적절한 셀 하이라이트
                    val x = event.x
                    val y = event.y

                    // 현재 위치에 해당하는 셀 찾기
                    row = (y / (view.height / (gridLayout.rowCount))).toInt()
                    col = (x / (view.width / (gridLayout.columnCount))).toInt()

                    true
                }

                DragEvent.ACTION_DROP -> {
                    binding.ivBlockRotate.visibility = View.INVISIBLE
                    Timber.d("ACTION_DROP")
                    val gridLayout = binding.includeBlock.gridLayout
                    for (i in 0 until gridLayout.childCount) {
                        val child = gridLayout.getChildAt(i)
                        val first = child.tag.toString()[0].toString()
                        val second = child.tag.toString()[1].toString()
                        if (first == row.toString() && second == col.toString()) {
                            Timber.d(("currentTYpe :$currentType"))
                             when(currentType){
                                "DAILY" -> {
                                    dailyBlockCount+=1
                                    setDailyBlock(row,col)
                                }
                                "SCHOOL" -> {

                                    schoolBlockCount+=1
                                    setSchoolBlock(row,col,  selectedDegree)
                                }
                                "HOBBY" -> {
                                    hobbyBlockCount+=1
                                    setInterestBlock(row,col,  selectedDegree)
                                }
                                "FAMILY" -> {
                                    familyBlockCount+=1
                                    setFamilyBlock(row,col,  selectedDegree)
                                }
                                "FRIEND" -> {
                                    friendBlockCount+=1
                                    setFriendBlock(row,col, selectedDegree)
                                }
                                else -> {}
                            }

                            break
                        } else continue
                    }


                    true
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    Timber.d("ACTION_DRAG_EXITED")
                    true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    Timber.d("ACTION_DRAG_ENDED")
                    true
                }

                else -> false
            }
        }
    }


    private fun onDragBlock() {
        binding.ivBlockRotate.rotation = selectedDegree.toFloat()

        // 현재 회전 각도도 조절하기
        DragStartHelper(binding.ivBlockRotate) { v, _ ->
            val item = ClipData.Item(v.tag as? CharSequence)
            val dragData = ClipData(
                v.tag as? CharSequence,
                arrayOf(MIMETYPE_TEXT_PLAIN),
                item
            )


            val shadowBuilder = object :View.DragShadowBuilder(v){
                override fun onProvideShadowMetrics(
                    outShadowSize: Point?,
                    outShadowTouchPoint: Point?
                ) {
                    val maxSize = ceil(sqrt((v.width * v.width + v.height * v.height).toDouble())).toInt()
                    outShadowSize?.set(maxSize, maxSize)
                    outShadowTouchPoint?.set(maxSize / 2, maxSize / 2)
                }
                override fun onDrawShadow(canvas: Canvas) {
                    canvas.save()
                    // 중앙으로 이동하고 회전 후 원래 위치로 이동
                    val centerX = canvas.width / 2f
                    val centerY = canvas.height / 2f
                    canvas.translate(centerX - v.width / 2f, centerY - v.height / 2f)
                    canvas.rotate(selectedDegree.toFloat(), v.width / 2f, v.height / 2f)

                    super.onDrawShadow(canvas)
                    canvas.restore()
                }
            }
            v.startDragAndDrop(
                dragData,
                shadowBuilder,
                null,
                0
            )
            true
        }.attach()
    }
    private fun onClickBlockBox(type: String, resId: Int) {
        with(binding) {
            currentType = type
            selectedDegree= 0
            ivBlockRotate.rotation = selectedDegree.toFloat()
            ivBlockRotate.visibility = View.VISIBLE
            ivBlockRotate.setImageDrawable(resources.getDrawable(resId))

            btnRotate.setOnClickListener {
                clickCount += 1
                if (clickCount >= 5) clickCount -= 4
                selectedDegree = rotateDegree[clickCount - 1]
                ivBlockRotate.rotation = selectedDegree.toFloat()
            }
        }
    }

    private fun setDailyBlock(row :Int, col : Int) {
        selectedBlock = arrayListOf<Pair<Int, Int>>()

        if (row >= 4 || row < 0 || col >= 4 || col < 0) {
            onRetry()
            return
        }
        binding.warnText.visibility = View.INVISIBLE
        block[row][col] = 1
        viewModel.setBlock(row, col)
        selectedBlock.add(row to col)

        blockView[row][col].setBackgroundColor(resources.getColor(R.color.orange_700_50))

        Timber.d("selectedBlock :$selectedBlock")
        val gridLayout = binding.includeBlock.gridLayout
        for (i in 0 until gridLayout.childCount) {
            val child = gridLayout.getChildAt(i)
            val first = child.tag.toString()[0].toString()
            val second = child.tag.toString()[1].toString()
            if (first == row.toString() && second == col.toString()) {
                child.setBackgroundColor(resources.getColor(R.color.orange_700_50))

                onComplete()
                break
            }
        }
    }


    private fun setBlock(blockData: Block) {

        for (block in blockData.blocks) {
            val color = when(block.category) {
                "DAILY" -> resources.getColor(R.color.orange_700)
                "SCHOOL" -> resources.getColor(R.color.blue_700)
                "HOBBY" -> resources.getColor(R.color.salmon_700)
                "FAMILY" -> resources.getColor(R.color.purple_700)
                "FRIEND" -> resources.getColor(R.color.green_700)
                else -> resources.getColor(R.color.white)
            }
            block.blockPlace.forEach {
                this.block[it.row][it.col] = 1
                blockList[it.row][it.col].setBackgroundColor(color)
            }


        }
    }

    private fun setInterestBlock(row :Int, col : Int, selectedDegree: Int) {
        selectedBlock = arrayListOf<Pair<Int , Int>>()
        if (selectedDegree==0 || selectedDegree==-180){
            if (row >=4 || row <0 || col>=4 || col<0 || row +1 >=4) {
                onRetry()
                return
            }
                if ((block[row][col] ==1) ||(block[row+1][col]==1) ) {
                onRetry()
                return
            }
            currentType = "HOBBY"
            binding.warnText.visibility = View.INVISIBLE
            block[row][col]=1
            block[row+1][col]=1

            viewModel.setBlock(row,col)
            viewModel.setBlock(row+1,col)

            selectedBlock.add(row to col)
            selectedBlock.add(row+1 to col)


            blockView[row][col].setBackgroundColor(resources.getColor(R.color.salmon_700_50))
            blockView[row+1][col].setBackgroundColor(resources.getColor(R.color.salmon_700_50))
            onComplete()
        }
        if (selectedDegree==-90 || selectedDegree==-270){
            if (row >=4 || row <0 || col>=4 || col<0 || col +1 >=4) {
                onRetry()
                return
            }
                if ((block[row][col] ==1) ||(block[row][col+1]==1) ) {
                onRetry()
                return
            }
            currentType = "HOBBY"
            binding.warnText.visibility = View.INVISIBLE
            block[row][col]=1
            block[row][col+1]=1

            viewModel.setBlock(row,col)
            viewModel.setBlock(row,col+1)

            selectedBlock.add(row to col)
            selectedBlock.add(row to col+1)

            blockView[row][col].setBackgroundColor(resources.getColor(R.color.salmon_700_50))
            blockView[row][col+1].setBackgroundColor(resources.getColor(R.color.salmon_700_50))
            onComplete()
        }
    }
    private fun setFamilyBlock(row :Int, col : Int, selectedDegree: Int){
        selectedBlock = arrayListOf<Pair<Int , Int>>()
        if(selectedDegree==0) {
            if (row >=4 || row <0 || col>=4 || col<0 || row +1 >=4 || col+1 >=4 || col+2>=4) {
                onRetry()
                return
            }
            if ((block[row][col] ==1) ||(block[row][col+2]==1) || (block[row][col+1]==1) || (block[row+1][col+1]==1) ) {
                onRetry()
                return
            }
            currentType = "FAMILY"
            binding.warnText.visibility = View.INVISIBLE
            block[row][col] = 1
            block[row][col+1] = 1
            block[row][col+2] = 1
            block[row+1][col+1] = 1

            viewModel.setBlock(row,col)
            viewModel.setBlock(row,col+1)
            viewModel.setBlock(row,col+2)
            viewModel.setBlock(row+1,col+1)

            selectedBlock.add(row to col)
            selectedBlock.add(row to col+1)
            selectedBlock.add(row to col+2)
            selectedBlock.add(row+1 to col+1)

            blockView[row][col].setBackgroundColor(resources.getColor(R.color.purple_700_50))
            blockView[row][col+1].setBackgroundColor(resources.getColor(R.color.purple_700_50))
            blockView[row][col+2].setBackgroundColor(resources.getColor(R.color.purple_700_50))
            blockView[row+1][col+1].setBackgroundColor(resources.getColor(R.color.purple_700_50))
            onComplete()
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
            currentType = "FAMILY"

            binding.warnText.visibility = View.INVISIBLE
            block[row][col] = 1
            block[row+1][col] = 1
            block[row+2][col] = 1
            block[row+1][col+1] = 1

            selectedBlock.add(row to col)
            selectedBlock.add(row+1 to col)
            selectedBlock.add(row+2 to col)
            selectedBlock.add(row+1 to col+1)

            viewModel.setBlock(row,col)
            viewModel.setBlock(row+1,col)
            viewModel.setBlock(row+2,col)
            viewModel.setBlock(row+1,col+1)



            blockView[row][col].setBackgroundColor(resources.getColor(R.color.purple_700_50))
            blockView[row+1][col].setBackgroundColor(resources.getColor(R.color.purple_700_50))
            blockView[row+2][col].setBackgroundColor(resources.getColor(R.color.purple_700_50))
            blockView[row+1][col+1].setBackgroundColor(resources.getColor(R.color.purple_700_50))
            onComplete()
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
            currentType = "FAMILY"

            binding.warnText.visibility = View.INVISIBLE
            block[row][col] = 1
            block[row-1][col+1] = 1
            block[row+1][col+1] = 1
            block[row+1][col+2] = 1

            selectedBlock.add(row to col)
            selectedBlock.add(row-1 to col+1)
            selectedBlock.add(row+1 to col+1)
            selectedBlock.add(row+1 to col+2)

            viewModel.setBlock(row,col)
            viewModel.setBlock(row-1,col+1)
            viewModel.setBlock(row+1,col+1)
            viewModel.setBlock(row+1,col+2)


            blockView[row][col].setBackgroundColor(resources.getColor(R.color.purple_700_50))
            blockView[row-1][col+1].setBackgroundColor(resources.getColor(R.color.purple_700_50))
            blockView[row+1][col+1].setBackgroundColor(resources.getColor(R.color.purple_700_50))
            blockView[row+1][col+2].setBackgroundColor(resources.getColor(R.color.purple_700_50))
            onComplete()
        }
        if (selectedDegree==-270){
            selectedBlock = arrayListOf<Pair<Int , Int>>()
            if (row >=4 || row <0 || col>=4 || col<0 || row +1 >=4 || col+1 >=4 || row-1<0 ) {
                onRetry()
                return
            }
            if ((block[row][col] ==1) ||( block[row-1][col+1] ==1) || (block[row][col+1] ==1) ||  block[row+1][col+1]==1) {
                onRetry()
                return
            }
            currentType = "FAMILY"

            binding.warnText.visibility = View.INVISIBLE
            block[row][col] = 1
            block[row-1][col+1] = 1
            block[row][col+1] = 1
            block[row+1][col+1] = 1

            selectedBlock.add(row to col)
            selectedBlock.add(row-1 to col+1)
            selectedBlock.add(row to col+1)
            selectedBlock.add(row+1 to col+1)

            viewModel.setBlock(row,col)
            viewModel.setBlock(row-1,col+1)
            viewModel.setBlock(row,col+1)
            viewModel.setBlock(row+1,col+1)


            blockView[row][col].setBackgroundColor(resources.getColor(R.color.purple_700_50))
            blockView[row-1][col+1].setBackgroundColor(resources.getColor(R.color.purple_700_50))
            blockView[row][col+1].setBackgroundColor(resources.getColor(R.color.purple_700_50))
            blockView[row+1][col+1].setBackgroundColor(resources.getColor(R.color.purple_700_50))
            onComplete()
        }

    }

    private fun onRetry(){
        selectedDegree =0
        binding.warnText.visibility = View.VISIBLE
        binding.ivBlockRotate.visibility= View.VISIBLE
        binding.ivBlockRotate.rotation = selectedDegree.toFloat()
    }

    private fun onComplete(){
        binding.btnComplete.setBackgroundDrawable(resources.getDrawable(R.drawable.button_block_complete))
        binding.btnComplete.setTextColor(resources.getColor(R.color.white))
    }

    private fun setFriendBlock(row :Int, col : Int, degree: Int){
        selectedBlock = arrayListOf<Pair<Int , Int>>()

        if (degree ==0 || degree==-180) {
            if (row >=4 || row <0 || col>=4 || col<0 || row +1 >=4 || col+1 >=4 || row+2>=4) {
                onRetry()
                return
            }
            if ((block[row][col] ==1) ||( block[row+1][col]==1) || (block[row+1][col+1]==1)|| block[row+2][col+1]==1)  {
                onRetry()
                return
            }
            currentType = "FRIEND"

            binding.warnText.visibility = View.INVISIBLE
            block[row][col]=1
            block[row+1][col]=1
            block[row+1][col+1]=1
            block[row+2][col+1]=1

            viewModel.setBlock(row,col)
            viewModel.setBlock(row+1,col)
            viewModel.setBlock(row+1,col+1)
            viewModel.setBlock(row+2,col+1)



            selectedBlock.add(row to col)
            selectedBlock.add(row+1 to col)
            selectedBlock.add(row+1 to col+1)
            selectedBlock.add(row+2 to col+1)


            val gridLayout = binding.includeBlock.gridLayout
            for (i in 0 until gridLayout.childCount) {

                val child = gridLayout.getChildAt(i)
                val first = child.tag.toString()[0].toString()
                val second = child.tag.toString()[1].toString()
                if (first == row.toString() && second == col.toString()) {
                    child.setBackgroundColor(resources.getColor(R.color.orange_700_50))
                   // blockView[row][col].setBackgroundColor(resources.getColor(R.color.orange_700_50))
                    onComplete()
                    break
                }
            }


            blockView[row][col].setBackgroundColor(resources.getColor(R.color.green_700_50))
            blockView[row+1][col].setBackgroundColor(resources.getColor(R.color.green_700_50))
            blockView[row+1][col+1].setBackgroundColor(resources.getColor(R.color.green_700_50))
            blockView[row+2][col+1].setBackgroundColor(resources.getColor(R.color.green_700_50))
            onComplete()

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
            currentType = "FRIEND"
            binding.warnText.visibility = View.INVISIBLE

            block[row][col]=1
            block[row][col+1]=1
            block[row-1][col+1]=1
            block[row-1][col+2]=1

            selectedBlock.add(row to col)
            selectedBlock.add(row to col+1)
            selectedBlock.add(row-1 to col+1)
            selectedBlock.add(row-1 to col+2)

            viewModel.setBlock(row,col)
            viewModel.setBlock(row,col+1)
            viewModel.setBlock(row-1,col+1)
            viewModel.setBlock(row-1,col+2)



            blockView[row][col].setBackgroundColor(resources.getColor(R.color.green_700_50))
            blockView[row][col+1].setBackgroundColor(resources.getColor(R.color.green_700_50))
            blockView[row-1][col+1].setBackgroundColor(resources.getColor(R.color.green_700_50))
            blockView[row-1][col+2].setBackgroundColor(resources.getColor(R.color.green_700_50))
            onComplete()

        }

    }
    private fun setSchoolBlock(row :Int, col : Int, degree: Int){
        selectedBlock = arrayListOf()
        if(degree==0) {
            if (row >=4 || row <0 || col>=4 || col<0 || row+1 >=4 || col+1 >=4) {
              onRetry()
                return
            }
            if ((block[row][col] ==1) ||(block[row+1][col]==1) || (block[row+1][col+1]==1)) {
                onRetry()
                return
            }
            currentType = "SCHOOL"

            block[row][col] = 1
            block[row+1][col] = 1
            block[row+1][col+1] = 1
            blockView[row][col].setBackgroundColor(resources.getColor(R.color.blue_700_50))
            blockView[row+1][col].setBackgroundColor(resources.getColor(R.color.blue_700_50))
            blockView[row+1][col+1].setBackgroundColor(resources.getColor(R.color.blue_700_50))

            selectedBlock.add(row to col)
            selectedBlock.add(row+1 to col)
            selectedBlock.add(row+1 to col+1)

            viewModel.setBlock(row,col)
            viewModel.setBlock(row+1,col)
            viewModel.setBlock(row+1,col+1)


            onComplete()

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
            currentType = "SCHOOL"

            binding.warnText.visibility = View.INVISIBLE
            block[row][col] = 1
            block[row-1][col+1] = 1
            block[row][col+1] = 1

            selectedBlock.add(row to col)
            selectedBlock.add(row-1 to col+1)
            selectedBlock.add(row to col+1)

            viewModel.setBlock(row,col)
            viewModel.setBlock(row-1,col+1)
            viewModel.setBlock(row,col+1)


            blockView[row][col].setBackgroundColor(resources.getColor(R.color.blue_700_50))
            blockView[row-1][col+1].setBackgroundColor(resources.getColor(R.color.blue_700_50))
            blockView[row][col+1].setBackgroundColor(resources.getColor(R.color.blue_700_50))
            onComplete()

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
            currentType = "SCHOOL"

            binding.warnText.visibility = View.INVISIBLE
            block[row][col] = 1
            block[row][col+1] = 1
            block[row+1][col+1] = 1

            selectedBlock.add(row to col)
            selectedBlock.add(row to col+1)
            selectedBlock.add(row+1 to col+1)


            viewModel.setBlock(row,col)
            viewModel.setBlock(row,col+1)
            viewModel.setBlock(row+1,col+1)


            blockView[row][col].setBackgroundColor(resources.getColor(R.color.blue_700_50))
            blockView[row][col+1].setBackgroundColor(resources.getColor(R.color.blue_700_50))
            blockView[row+1][col+1].setBackgroundColor(resources.getColor(R.color.blue_700_50))
            onComplete()

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
            currentType = "SCHOOL"

            binding.warnText.visibility = View.INVISIBLE
            block[row][col] = 1
            block[row][col+1] = 1
            block[row+1][col] = 1

            viewModel.setBlock(row,col)
            viewModel.setBlock(row,col+1)
            viewModel.setBlock(row+1,col)



            selectedBlock.add(row to col)
            selectedBlock.add(row to col+1)
            selectedBlock.add(row+1 to col)

            blockView[row][col].setBackgroundColor(resources.getColor(R.color.blue_700_50))
            blockView[row][col+1].setBackgroundColor(resources.getColor(R.color.blue_700_50))
            blockView[row+1][col].setBackgroundColor(resources.getColor(R.color.blue_700_50))

            onComplete()
        }

    }

    private fun onSetBlock(blockType : String, degree :Int, row : Int, col : Int){
        binding.ivBlockRotate.visibility = View.INVISIBLE
        when(blockType){
            "SCHOOL" -> setSchoolBlock(row,col,degree)
            "DAILY" ->  setDailyBlock(row,col)
            "FAMILY" -> setFamilyBlock(row,col,degree)
            "FRIEND" -> setFriendBlock(row,col,degree)
            "HOBBY" -> setInterestBlock(row, col, degree)
        }
    }

    private fun initBlock() {
        for (views in blockList) {
            views.forEach {
                it.setBackgroundColor(resources.getColor(R.color.white))
            }
        }
    }


    override fun onBindLayout() {
        super.onBindLayout()

        onDragBlock()
        setGridDragListener()
        //handleBlockDrop()

        binding.includeInfo.txtDeleteBlock.setOnClickListener { viewModel.deleteBlock()}


        viewModel.allBlockCompleteEvent.observe(this, {
            // 블럭이 모두 채워진 경우?
            initBlock()
           // binding.includeInfo.txtLevel.text =" ${level+1}단계"
            findNavController().navigate(R.id.blockCompleteFragment)
        })
        blockList =
            listOf(
                listOf(
                    binding.includeBlock.block1,
                    binding.includeBlock.block2,
                    binding.includeBlock.block3,
                    binding.includeBlock.block4,
                ),
                listOf(
                    binding.includeBlock.block5,
                    binding.includeBlock.block6,
                    binding.includeBlock.block7 ,
                    binding.includeBlock.block8,
                ),
                listOf(
                    binding.includeBlock.block9,
                    binding.includeBlock.block10,
                    binding.includeBlock.block11,
                    binding.includeBlock.block12,

                    ),
                listOf(
                    binding.includeBlock.block13,
                    binding.includeBlock.block14,
                    binding.includeBlock.block15,
                    binding.includeBlock.block16,
                )
            )


        // 블럭 조회 후  채우기 
        lifecycleScope.launch {
            viewModel.block.collect {
                setBlock(it)
            }
        }


        binding.includeInfo.modifyViewModel = viewModel
        binding.includeInfo.isModifying = true
        binding.includeInfo.lifecycleOwner = viewLifecycleOwner

        blockView = listOf(
            listOf(binding.includeBlock.block1, binding.includeBlock.block2, binding.includeBlock.block3,binding.includeBlock.block4),
            listOf(binding.includeBlock.block5, binding.includeBlock.block6, binding.includeBlock.block7,binding.includeBlock.block8),
            listOf(binding.includeBlock.block9, binding.includeBlock.block10, binding.includeBlock.block11,binding.includeBlock.block12),
            listOf(binding.includeBlock.block13, binding.includeBlock.block14, binding.includeBlock.block15,binding.includeBlock.block16),
        )

        with(binding) {

            // 블럭 배치 성공
            viewModel.blockEvent.observe(this@BlockModifyFragment, {
                findNavController().navigate(R.id.blockFragment)
            })

            btnComplete.setOnClickListener {
                val arrayList : ArrayList<BlockPlace>  = arrayListOf()
                for (block in selectedBlock) {
                    arrayList.add(BlockPlace(block.first, block.second))
                }
                Timber.d("arrayList :$arrayList")

                if (currentType!= null ) {
                    Timber.d("currentTYpe :$currentType")
                    viewModel.saveBlock(
                        SaveBlockRequest(
                            category = currentType!!,
                            blockPlace = arrayList))

                }


            }

            btnCancel.setOnClickListener {
                findNavController().popBackStack()
            }

            // 대화 주머니 클릭
            includeInfo.firstBlock.setOnClickListener {
                if (viewModel.dailyBlockCount.value == dailyBlockCount) {
                    showCompleteToast()
                    return@setOnClickListener
                }
                it.isSelected =true
                        currentType = "DAILY"
                        includeInfo.secondBlock.isSelected = false
                        includeInfo.thirdBlock.isSelected = false
                        includeInfo.fourthBlock.isSelected = false
                        includeInfo.fifthBlock.isSelected = false
                        onClickBlockBox("DAILY", R.drawable.block_daily_v2)
                    }


            includeInfo.imageView6.setOnClickListener {
                if (viewModel.dailyBlockCount.value == dailyBlockCount) {
                    showCompleteToast()
                    return@setOnClickListener
                }
                includeInfo.firstBlock.isSelected = true
                    includeInfo.secondBlock.isSelected = false
                    includeInfo.thirdBlock.isSelected = false
                    includeInfo.fourthBlock.isSelected = false
                    includeInfo.fifthBlock.isSelected = false
                    onClickBlockBox("DAILY", R.drawable.block_daily_v2)
                }
                includeInfo.secondBlock.setOnClickListener {
                    if (viewModel.schoolBlockCount.value == schoolBlockCount) {
                        showCompleteToast()
                        return@setOnClickListener
                    }
                    it.isSelected = true
                        includeInfo.firstBlock.isSelected = false
                        includeInfo.thirdBlock.isSelected = false
                        includeInfo.fourthBlock.isSelected = false
                        includeInfo.fifthBlock.isSelected = false
                        onClickBlockBox("SCHOOL", R.drawable.block_school_v2)
                }
                includeInfo.thirdBlock.setOnClickListener {
                    if (viewModel.userHobbyBlockCount.value == hobbyBlockCount) {
                        showCompleteToast()
                        return@setOnClickListener
                    }
                    it.isSelected = true
                        includeInfo.firstBlock.isSelected = false
                        includeInfo.secondBlock.isSelected = false
                        includeInfo.fourthBlock.isSelected = false
                        includeInfo.fifthBlock.isSelected = false
                        onClickBlockBox("HOBBY", R.drawable.block_interest_v2)
                    }

                includeInfo.fourthBlock.setOnClickListener {
                    if (viewModel.userFamilyBlockCount.value == familyBlockCount) {
                        showCompleteToast()
                        return@setOnClickListener
                    }
                    it.isSelected = true
                        includeInfo.firstBlock.isSelected = false
                        includeInfo.secondBlock.isSelected = false
                        includeInfo.thirdBlock.isSelected = false
                        includeInfo.fifthBlock.isSelected = false
                        onClickBlockBox("FAMILY", R.drawable.block_family_v2)
                    }

                includeInfo.fifthBlock.setOnClickListener {
                    if (viewModel.friendBlockCount.value == friendBlockCount) {
                        showCompleteToast()
                        return@setOnClickListener
                    }
                    it.isSelected  = true

                        includeInfo.secondBlock.isSelected = false
                        includeInfo.thirdBlock.isSelected = false
                        includeInfo.fourthBlock.isSelected = false
                        includeInfo.firstBlock.isSelected = false
                        onClickBlockBox("FRIEND", R.drawable.block_friend_v2)
                }
            }
        }
    }