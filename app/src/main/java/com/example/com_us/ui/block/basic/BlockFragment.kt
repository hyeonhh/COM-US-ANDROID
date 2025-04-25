package com.example.com_us.ui.block.basic

import android.view.View
import androidx.compose.runtime.currentCompositionErrors
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.com_us.R
import com.example.com_us.base.fragment.BaseFragment
import com.example.com_us.data.model.home.Block
import com.example.com_us.databinding.FragmentBlockBasicBinding
import com.example.com_us.util.ColorMatch
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class BlockFragment : BaseFragment<FragmentBlockBasicBinding, BlockViewModel>(
    FragmentBlockBasicBinding::inflate
) {
    private lateinit var blockList: List<List<View>>
    override val viewModel : BlockViewModel by viewModels()
    private val navController by lazy{ findNavController()}
    private var level : Int = 0
    private val args by navArgs<BlockFragmentArgs>()

    private fun initBlock() {
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
        for (views in blockList) {
            views.forEach {
                it.setBackgroundColor(resources.getColor(R.color.white))
                binding.textviewHomeNoblock.visibility = View.VISIBLE
                binding.imageviewHomeNoblock.visibility = View.VISIBLE
            }
        }
    }
        override fun onBindLayout() {
        super.onBindLayout()

//            lifecycleScope.launch {
//                viewModel.currentLevel.collectLatest {
//                    level= it
//                    binding.includeBlockInfo.txtLevel.text = "${level}단계"
//                }
//            }
//

            viewModel.allBlockCompleteEvent.observe(this, {
                initBlock()
            })
//        //todo :
//        if (args.isFull) {
//           // 새로운 판 보이기
//            initBlock()
//        }

        // 블럭 조회 후  채우기
        lifecycleScope.launch {
            viewModel.block.collect {
                setBlock(it)
            }
        }

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


        binding.includeBlockInfo.viewModel = viewModel
        binding.includeBlockInfo.isModifying = false
        binding.includeBlockInfo.lifecycleOwner = viewLifecycleOwner

        binding.btnGoToModify.setOnClickListener {
            if (args.isFull){
                val action = BlockFragmentDirections.actionBlockFragmentToBlockModifyFragment(true)
                navController.navigate(action)
            }
            navController.navigate(R.id.blockModifyFragment)
        }
    }

    private fun setBlock(blockData: Block) {
        Timber.d("setBlock")
        if (blockData.blocks.isNotEmpty()) setNoBlockBackground(false)
        else setNoBlockBackground(true)

        level = blockData.level

        for (data in blockData.blocks) {
            val color = when(data.category) {
                "DAILY" -> resources.getColor(R.color.orange_700)
                "SCHOOL" -> resources.getColor(R.color.blue_700)
                "HOBBY" -> resources.getColor(R.color.salmon_700)
                "FAMILY" -> resources.getColor(R.color.purple_700)
                "FRIEND" -> resources.getColor(R.color.green_700)
                else -> resources.getColor(R.color.orange_700)
            }
            data.blockPlace.forEach {
                Timber.d("블럭 :$data")
                // todo : 색 변경 방식 변경 필요
                blockList[it.row][it.col].setBackgroundColor(color)
            }
            }

    }

    private fun setNoBlockBackground(setBg: Boolean) {
        val visibility = if (setBg) View.VISIBLE else View.INVISIBLE
        binding.imageviewHomeNoblock.visibility = visibility
        binding.textviewHomeNoblock.visibility = visibility
    }

}