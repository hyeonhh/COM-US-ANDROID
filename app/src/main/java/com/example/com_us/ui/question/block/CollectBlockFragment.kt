package com.example.com_us.ui.question.block

import android.content.Intent
import android.os.Handler
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.com_us.R
import com.example.com_us.base.fragment.BaseFragment
import com.example.com_us.databinding.ActivityQuestionCollectBlockBinding
import com.example.com_us.ui.question.result.ResultAfterSignActivity
import com.example.com_us.util.ThemeType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// 대화 블럭 획득 화면
@AndroidEntryPoint
class CollectBlockFragment : BaseFragment<ActivityQuestionCollectBlockBinding,CollectBlockViewModel>(
    ActivityQuestionCollectBlockBinding::inflate
) {
    override val viewModel: CollectBlockViewModel by viewModels()

    private val args by navArgs<CollectBlockFragmentArgs>()

    private fun setTheme() {
        binding.textviewCollectTheme.text = args.category
        val blockRes = ThemeType.findResFromKor(args.category)
        if(blockRes != null){
            binding.imageviewCollectBlockicon.setImageResource(blockRes)
        }
    }

    override fun onBindLayout() {
        super.onBindLayout()
        setTheme()
        lifecycleScope.launch {
            delay(3000)
            findNavController().navigate(R.id.navigation_questions)
        }


    }

}