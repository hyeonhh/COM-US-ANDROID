package com.example.com_us.ui.qa

import android.content.Intent
import android.net.Uri
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.com_us.R
import com.example.com_us.base.fragment.BaseFragment
import com.example.com_us.databinding.ActivityAnswerBinding
import com.example.com_us.ui.base.UiState
import com.example.com_us.ui.compose.AnswerTypeTag
import com.example.com_us.ui.compose.QuestionTypeTag
import com.example.com_us.util.ColorMatch
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.regex.Pattern


@AndroidEntryPoint
class AnswerFragment : BaseFragment<ActivityAnswerBinding, AnswerViewModel>(
    ActivityAnswerBinding::inflate
){
    override val viewModel : AnswerViewModel by activityViewModels()
    private val navController by lazy { findNavController() }
    private val args by navArgs<AnswerFragmentArgs>()


    private fun setQuestionTypeCompose() {
        binding.category.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                Row {
                    //todo :데이터 연결
                    val colorType = ColorMatch.fromKor(viewModel.category)
                    if (colorType != null) {
                        QuestionTypeTag(colorType.colorType, viewModel.category)
                    }
                }
            }
        }
    }

    private fun setAnswerTypeCompose() {
        binding.answerType.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                Row {
                    //todo :데이터 연결
                   val answerType =  if (viewModel.answerType=="MULTIPLE_CHOICE")"선택형" else "대화형"
                    val colorType = ColorMatch.fromKor(answerType)
                    if (colorType != null) {
                        AnswerTypeTag(colorType.colorType, answerType)
                    }
                }
            }
        }
    }

    override fun onBindLayout() {
        super.onBindLayout()

        // 안내 문구 표시
        val word = "파란색"
        val start = binding.txtInfo.text.toString().indexOf("파란색")
        val end = start + word.length

        val content = binding.txtInfo.text.toString()
        val spannableString = SpannableString(content)

        spannableString.setSpan(ForegroundColorSpan(resources.getColor(R.color.blue_700)),start,end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        spannableString.setSpan(UnderlineSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.txtInfo.setText(spannableString)


        binding.btnComplete.setOnClickListener {
            val action = AnswerFragmentDirections.actionSelectAnswerFragmentToPreviousDestination()
            navController.navigate(action)
        }

        if (args.isRecord) {
            binding.btnComplete.visibility = View.VISIBLE
            binding.btnCompleteAndGetBlock.visibility = View.GONE
        }
        else {
            binding.btnComplete.visibility = View.GONE
            binding.btnCompleteAndGetBlock.visibility = View.VISIBLE
        }

        binding.btnCompleteAndGetBlock.setOnClickListener {
           val action = AnswerFragmentDirections.actionAnswerFragmentToCollectBlockFragment(category = viewModel.category)
            navController.navigate(action)
        }

        binding.btnBack.setOnClickListener {
            // 월별 기록으로 가기
            val action = AnswerFragmentDirections.actionSelectAnswerFragmentToPreviousDestination()
            navController.navigate(action)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiState.collectLatest {
                when(it){
                   is  UiState.Success -> {

                       val modifiedText = it.data.convertedSentence.joinToString(" ")
                       binding.txtModifiedAnswer.text = modifiedText

                       val answerList = it.data.convertedSentence
                       val spannableString = SpannableString(modifiedText)

                       // 중괄호 내용 찾아서 회색으로 표시
                       val pattern = Pattern.compile("\\{([^{}]*)\\}")
                       val matcher = pattern.matcher(modifiedText)

                       while (matcher.find()) {
                           // 중괄호와 내용 모두 회색으로 표시
                           spannableString.setSpan(
                               ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.gray_500)),
                               matcher.start(),
                               matcher.end(),
                               Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                           )
                       }


                       it.data.kslVideos.forEach {
                           for (answer in answerList) {
                               if (it.key == answer) {
                                   val startIndex = modifiedText.indexOf(answer)
                                   if (startIndex != -1) {
                                       Timber.d("발견 :$answer")
                                       //todo : 특정 단어만 파란색 밑줄 치기
                                       val endIndex = startIndex + answer.length
                                       spannableString.setSpan(UnderlineSpan(), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

                                       // todo : 비수지 표현


                                       // 클릭 이벤트 추가
                                       val clickableSpan = object : ClickableSpan() {
                                           override fun onClick(widget: View) {
                                               Timber.d("Clicked on: $answer")  // 클릭 시 로그 출력
                                               val urlIntent = Intent(
                                                   Intent.ACTION_VIEW,
                                                   Uri.parse(it.value.videoUrl)
                                               )
                                               startActivity(urlIntent)
                                           }

                                           override fun updateDrawState(ds: TextPaint) {
                                               super.updateDrawState(ds)
                                               ds.color = resources.getColor(R.color.blue_700)  // 클릭 가능한 링크 색상 변경
                                               ds.isUnderlineText = true  // 밑줄 유지
                                           }
                                       }
                                       spannableString.setSpan(clickableSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

                                       binding.txtModifiedAnswer.text = spannableString

                                       binding.txtModifiedAnswer.movementMethod = LinkMovementMethod.getInstance()  // 클릭 가능하게 설정


                                   }

                                   }


                               }
                       }
                       setQuestionTypeCompose()
                       setAnswerTypeCompose()
                    }
                    else -> {
                        Toast.makeText(requireContext(), "잠시 후에 다시 시도해주세요",Toast.LENGTH_SHORT).show()
                    }
                }
                }
            }

        }

        binding.txtDate.text = viewModel.answeredDate +"의 대화"
        binding.txtQuestion.text = viewModel.selectedQuestion
        binding.txtAnswer.text = viewModel.answer


    }
}