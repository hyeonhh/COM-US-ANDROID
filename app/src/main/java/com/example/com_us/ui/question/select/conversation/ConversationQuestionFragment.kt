package com.example.com_us.ui.question.select.conversation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.com_us.R
import com.example.com_us.base.fragment.BaseFragment
import com.example.com_us.data.model.question.request.DetailQuestionRequest
import com.example.com_us.data.model.question.request.RequestAnswerRequest
import com.example.com_us.databinding.ActivityConversationQuestionBinding
import com.example.com_us.ui.base.UiState
import com.example.com_us.ui.compose.AnswerTypeTag
import com.example.com_us.ui.compose.QuestionTypeTag
import com.example.com_us.ui.qa.AnswerFragment
import com.example.com_us.util.ColorMatch
import com.example.com_us.util.QuestionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ConversationQuestionFragment : BaseFragment<ActivityConversationQuestionBinding, ConversationQuestionViewModel>(
    ActivityConversationQuestionBinding::inflate
){
    private var type : String? = null
    private var answerType : String? = null
    private var questionId: Int = -1
    override val viewModel : ConversationQuestionViewModel by viewModels()
    private val navController by lazy {findNavController()}
    private val args : ConversationQuestionFragmentArgs by navArgs()

    private var answerDate : String = ""
    private var question : String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        questionId = args.questionId
        type = args.type
        answerType = args.answerType
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setQuestionTypeCompose(category : String) {
        binding.questionCategory.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                Row {
                    //todo :데이터 연결
                    val colorType = ColorMatch.fromKor(category)
                    if (colorType != null) {
                        QuestionTypeTag(colorType.colorType, category)
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
                    val colorType = ColorMatch.fromKor("대화형")
                    if (colorType != null) {
                        AnswerTypeTag(colorType.colorType, "대화형")
                    }
                }
            }
        }
    }

    private fun setPreviousAnswerButton() {
        binding.buttonDetailAnswerbefore.setOnClickListener {
            val action = ConversationQuestionFragmentDirections.actionConversationQuestionFragmentToPreviousAnswerFragment(args.questionId.toString())
            navController.navigate(action)
            QuestionManager.questionId = questionId
        }
    }


    private fun setQuestionDetail() {
        lifecycleScope.launch {
            viewModel.uiState.collectLatest {
                when(it) {
                    is UiState.Loading ->{}
                    is UiState.Success -> {
                        binding.tvDate.text = "${it.data.question.answerDate}일의 기록"
                        binding.textviewDetailQuestion.text = it.data.question.questionContent

                        answerDate = it.data.question.answerDate
                        question = it.data.question.questionContent
                        setAnswerTypeCompose()
                        if (it.data.multipleChoice[0]=="") return@collectLatest
                    }

                    is UiState.Error -> {
                        Timber.d("에러 $it.message")
                    }
                }
            }

        }

    }

    override fun onBindLayout() {
        super.onBindLayout()

        if(args.answerType=="BOTH") {
            binding.goToSelect.visibility = View.VISIBLE
        }

        binding.btnBack.setOnClickListener {
            navController.popBackStack()
        }
        if(questionId > 0) {
            QuestionManager.questionId = questionId
            viewModel.loadQuestionDetail(
                DetailQuestionRequest(
                    isRandom = args.isRandom, questionId)
            )
            setQuestionDetail()
            setPreviousAnswerButton()
        }

        // 답변 완료 클릭
        binding.buttonDetailComplete.setOnClickListener {

            viewModel.postAnswer(
                RequestAnswerRequest(
                questionId = args.questionId,
                answerType = "SENTENCE",
                answerContent = binding.editText.text.toString()
            )
            )

            if (type!= null ) {
                val action =
                    ConversationQuestionFragmentDirections.actionConversationQuestionFragmentToLoadingFragment(
                        question = question,
                        answerDate = answerDate,
                        answer = binding.editText.text.toString(),
                        answerType = "SENTENCE",
                        category = type!!
                    )
                navController.navigate(action)

            }
        }

        // 이전 답변 보기 기능
        binding.buttonDetailAnswerbefore.setOnClickListener {
            setPreviousAnswerButton()
        }

        if (answerType=="BOTH") {
            binding.goToSelect.visibility = View.VISIBLE
        }
        binding.editText.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!= null)
                if (s.isNotEmpty()){
                        val resource =
                            when (type) {
                                "DAILY" -> resources.getDrawable(R.drawable.answer_button_daily)
                                "INTEREST" -> resources.getDrawable(R.drawable.answer_button_interest)
                                "FAMILY" -> resources.getDrawable(R.drawable.answer_button_family)
                                "FRIEND" -> resources.getDrawable(R.drawable.answer_button_friend)
                                "SCHOOL" -> resources.getDrawable(R.drawable.answer_button_school)
                                else -> resources.getDrawable(R.drawable.answer_button_daily)
                            }
                        binding.buttonDetailComplete.setBackgroundDrawable(resource)
                        binding.buttonDetailComplete.setTextColor(resources.getColor(R.color.white))
                }
                else {
                    binding.buttonDetailComplete.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_fill_rect10_gray200))
                    binding.buttonDetailComplete.setTextColor(resources.getColor(R.color.gray_500))
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        type?.let { setQuestionTypeCompose( it) }
        setAnswerTypeCompose()

        binding.goToSelect.setOnClickListener {
            if (type!= null && answerType!= null ) {
                val action =
                    ConversationQuestionFragmentDirections.actionConversationQuestionFragmentToSelectAnswerFragment(
                        questionId = questionId, type = type!!, answerType = answerType!!
                    )
                navController.navigate(action)
            }

        }
    }
}