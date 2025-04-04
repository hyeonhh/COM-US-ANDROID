package com.example.com_us.ui.check

import androidx.fragment.app.viewModels
import com.example.com_us.base.fragment.BaseFragment
import com.example.com_us.databinding.FragmentCheckBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckFragment : BaseFragment<FragmentCheckBinding, CheckViewModel>(
    FragmentCheckBinding::inflate
){
    override val viewModel : CheckViewModel by viewModels()

}