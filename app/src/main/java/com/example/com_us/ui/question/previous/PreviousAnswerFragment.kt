package com.example.com_us.ui.question.previous

import android.view.View
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.com_us.R
import com.example.com_us.base.fragment.BaseFragment
import com.example.com_us.data.model.question.response.question.PreviousAnswer
import com.example.com_us.data.model.question.response.question.ResponsePreviousAnswerDto
import com.example.com_us.databinding.ActivityQuestionPreviousAnswerBinding
import com.example.com_us.ui.base.UiState
import com.example.com_us.ui.compose.AnswerHistoryItem
import com.example.com_us.ui.compose.AnswerHistoryList
import com.example.com_us.ui.compose.AnswerTypeTag
import com.example.com_us.ui.compose.QuestionTypeTag
import com.example.com_us.ui.compose.TimeLineItem
import com.example.com_us.util.ColorMatch
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

// 이전 답변을 보여주는 화면
@AndroidEntryPoint
class PreviousAnswerFragment : BaseFragment<ActivityQuestionPreviousAnswerBinding,PreviousAnswerViewModel>(
    ActivityQuestionPreviousAnswerBinding::inflate,
) {

   override  val viewModel: PreviousAnswerViewModel by viewModels()
    private var questionId: Int = -1
    private val args by navArgs<PreviousAnswerFragmentArgs>()
    private val navController by lazy {findNavController()}

    override fun onBindLayout() {
        super.onBindLayout()
        binding.btnBack.setOnClickListener { navController.popBackStack() }

        questionId = args.questionId.toInt()

        println(questionId)
        if(questionId > -1) viewModel.loadPreviousAnswer(questionId)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState.collect{
                    when(it){
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            binding.textviewPreviousQuestion.text = it.data.questionContent

                            setAnswerTypeCompose(it.data.questionAnswerType)
                            setCategoryCompose(it.data.category)
                            setComposeList(it.data)
                        }
                        is UiState.Error ->
                            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }

    }

    private fun setCategoryCompose(category : String) {
        binding.composePreviousQuestiontype.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                Row {
                    val colorType = ColorMatch.fromKor(category)
                    if (colorType != null) {
                        QuestionTypeTag(colorType.colorType, category)
                    }
                }
            }
        }
    }


    private fun setComposeList(data : ResponsePreviousAnswerDto) {
        binding.textviewPreviousAnswerCount.text = String.format(resources.getString(R.string.question_previous_answer_count), data.answerCount)
        // 이전 답변 리스트
        binding.composePreviousAnswerlist.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AnswerHistoryList(data.answers,PreviousAnswerFragmentDirections, navController, category = data.category, question = data.questionContent )
            }
        }
    }

    // 대화형인지 선택형인지
    private fun setAnswerTypeCompose(answerType : String) {
        when (answerType) {
            "BOTH" -> {
                binding.answerType.apply{
                    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                    setContent {
                        Row {
                            //todo :데이터 연결
                            val colorType = ColorMatch.fromKor("선택형")
                            if (colorType != null) {
                                Timber.d("$colorType")
                                AnswerTypeTag(colorType.colorType, "선택형")
                            }

                        }
                    }
                }
                binding.answerType2.apply{
                    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                    setContent {
                        Row {
                            //todo :데이터 연결
                            val colorType = ColorMatch.fromKor("대화형")
                            if (colorType != null) {
                                Timber.d("$colorType")
                                AnswerTypeTag(colorType.colorType, "대화형")
                            }
                        }
                    }
                }
            }
            "MULTIPLE_CHOICE" -> {
                Timber.d("answerType :$answerType")
                binding.answerType2.visibility = View.GONE

                binding.answerType.apply{
                    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                    setContent {
                        Row {
                            //todo :데이터 연결
                            val colorType = ColorMatch.fromKor("선택형")
                            if (colorType != null) {
                                Timber.d("$colorType")
                                AnswerTypeTag(colorType.colorType, "선택형")
                            }
                        }
                    }
                }
            }
            "SENTENCE" ->  {
                Timber.d("answerType :$answerType")
                binding.answerType2.visibility = View.GONE
                binding.answerType.apply {
                    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                    setContent {
                        Row {
                            //todo :데이터 연결
                            val colorType = ColorMatch.fromKor("대화형")
                            if (colorType != null) {
                                Timber.d("$colorType")
                                AnswerTypeTag(colorType.colorType, "대화형")
                            }

                        }
                    }
                }
            }
        }
    }


}