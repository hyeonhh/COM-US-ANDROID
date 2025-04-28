package com.example.com_us.ui.question.theme

import android.view.MenuItem
import android.widget.Toast
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.com_us.R
import com.example.com_us.base.fragment.BaseFragment
import com.example.com_us.databinding.ActivityThemeQuestionListBinding
import com.example.com_us.ui.base.UiState
import com.example.com_us.ui.compose.QuestionListItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ThemeQuestionListFragment : BaseFragment<ActivityThemeQuestionListBinding,ThemeQuestionListViewModel>(
    ActivityThemeQuestionListBinding::inflate
) {

    private val navController by lazy { findNavController()}
    override val viewModel: ThemeQuestionListViewModel by viewModels()
    private val args by navArgs<ThemeQuestionListFragmentArgs>()

    override fun onBindLayout() {
        super.onBindLayout()
        val theme = args.theme
        viewModel.loadQuestionListByCate(theme)

        val themeKor = when(theme){
            "DAILY" -> "일상"
            "SCHOOL" ->"학교"
            "HOBBY","INTEREST" -> "취미"
            "FAMILY" -> "학교"
            "FRIEND" -> "친구"
            else -> ""
        }

        binding.textviewTitle.text = String.format(resources.getString(R.string.theme_question_list_title), themeKor)
        setComposeList()

    }

    private fun setComposeList() {
        lifecycleScope.launch {
            viewModel.uiState.collect {
                when (it) {
                    is UiState.Success -> {
                        binding.composeviewTheme.apply {
                            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                            setContent {
                                LazyColumn {
                                    items(it.data.size) { idx ->
                                        QuestionListItem(
                                            viewModel2 = viewModel,
                                            data = it.data[idx],
                                            onClick = {
                                                if (it.data[idx].answerType == "MULTIPLE_CHOICE") {
                                                    val action = ThemeQuestionListFragmentDirections.actionThemeQuestionListFragmentToSelectAnswerFragment(
                                                        questionId =  it.data[idx].id.toInt(),
                                                        answerType = "MULTIPLE_CHOICE",
                                                        type = it.data[idx].category
                                                    )
                                                    navController.navigate(action)
                                                }
                                                if (it.data[idx].answerType == "SENTENCE"){
                                                    val action = ThemeQuestionListFragmentDirections.actionThemeQuestionListFragmentToConversationQuestionFragment(
                                                        questionId =  it.data[idx].id.toInt(),
                                                        answerType = "SENTENCE",
                                                        type = it.data[idx].category
                                                    )
                                                    navController.navigate(action)
                                                }
                                                else {
                                                    val action = ThemeQuestionListFragmentDirections.actionThemeQuestionListFragmentToSelectAnswerFragment(
                                                        questionId =  it.data[idx].id.toInt(),
                                                        answerType = "BOTH",
                                                        type = it.data[idx].category
                                                    )
                                                    navController.navigate(action)
                                                }
                                            })
                                    }
                                }
                            }
                        }
                    }
                   is UiState.Error -> {
                       Toast.makeText(requireContext(),it.toString(),Toast.LENGTH_SHORT).show()
                   }
                    is UiState.Loading -> {}
                }
            }
        }
    }


}