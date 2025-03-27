package com.example.com_us.ui.block.basic

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.com_us.R
import com.example.com_us.base.fragment.BaseFragment
import com.example.com_us.databinding.FragmentBlockBasicBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BlockFragment : BaseFragment<FragmentBlockBasicBinding, BlockViewModel>(
    FragmentBlockBasicBinding::inflate
) {
    override val viewModel : BlockViewModel by viewModels()
    private val navController by lazy{ findNavController()}
    override fun onBindLayout() {
        super.onBindLayout()

        binding.btnGoToModify.setOnClickListener {
            navController.navigate(R.id.blockModifyFragment)
        }
    }
}