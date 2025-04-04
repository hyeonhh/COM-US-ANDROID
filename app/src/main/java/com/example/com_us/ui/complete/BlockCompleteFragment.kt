package com.example.com_us.ui.complete

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.com_us.R
import com.example.com_us.base.fragment.BaseFragment
import com.example.com_us.databinding.FragmentBlockCompleteBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BlockCompleteFragment : BaseFragment<FragmentBlockCompleteBinding, BlockCompleteViewModel>(
    FragmentBlockCompleteBinding::inflate
){
    override val viewModel: BlockCompleteViewModel
       by viewModels()
    private val navController by lazy { findNavController() }

    override fun onBindLayout() {
        super.onBindLayout()

        lifecycleScope.launch {
            delay(3000)
            val action = BlockCompleteFragmentDirections.actionBlockCompleteFragmentToBlockFragment(isFull = true)
            navController.navigate(action)
        }
    }
}