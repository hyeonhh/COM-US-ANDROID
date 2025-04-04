package com.example.com_us.ui.loading

import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.com_us.R
import com.example.com_us.base.fragment.BaseFragment
import com.example.com_us.databinding.FragmentLoadingBinding
import com.example.com_us.ui.qa.AnswerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoadingFragment : BaseFragment<FragmentLoadingBinding, AnswerViewModel>(
    FragmentLoadingBinding::inflate
) {
    override val viewModel : AnswerViewModel by activityViewModels()
    private val navController by lazy { findNavController() }
    private val args by navArgs<LoadingFragmentArgs>()
    override fun onBindLayout() {
        super.onBindLayout()

        viewModel.setUserAnswer(args.answer)
        viewModel.setAnswerDate(args.answerDate)
        viewModel.setQuestion(args.question)
        viewModel.setQuestionCategory(args.category)
        viewModel.setUserAnswerType(args.answerType)


        viewModel.getModifyAnswer()

        viewModel.failEvent.observe(this, {
            Toast.makeText(requireActivity(), "다시 시도해주세요",Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        })

        viewModel.answerEvent.observe(this, {
            if (args.isRecord) {
                val action = LoadingFragmentDirections.actionLoadingFragmentToAnswerFragment(isRecord = true)
                navController.navigate(action)
            }
            else {
                navController.navigate(R.id.answerFragment)

            }
        })
    }
}