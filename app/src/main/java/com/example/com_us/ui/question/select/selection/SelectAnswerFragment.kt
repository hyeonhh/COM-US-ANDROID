package com.example.com_us.ui.question.select.selection

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.com_us.R
import com.example.com_us.base.fragment.BaseFragment
import com.example.com_us.data.model.question.request.DetailQuestionRequest
import com.example.com_us.data.model.question.request.RequestAnswerRequest
import com.example.com_us.databinding.ActivityQuestionDetailBinding
import com.example.com_us.ui.base.UiState
import com.example.com_us.ui.compose.AnswerOptionList
import com.example.com_us.ui.compose.AnswerTypeTag
import com.example.com_us.ui.compose.QuestionTypeTag
import com.example.com_us.ui.qa.AnswerFragment
import com.example.com_us.ui.question.result.ResultBeforeSignActivity
import com.example.com_us.ui.question.select.conversation.ConversationQuestionFragmentDirections
import com.example.com_us.util.ColorMatch
import com.example.com_us.util.QuestionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date

//todo : 질문 종류에 따라서 배경색 다르게 하기
enum class TYPE(private val backgroundResourceId: Int) {
    DAILY(R.drawable.shape_fill_rect10_orange700),
    SCHOOL(R.drawable.shape_fill_rect10_blue700),
    FRIEND(R.drawable.shape_fill_rect10_green700),
    FAMILY(R.drawable.shape_fill_rect10_purple700),
    INTEREST(R.drawable.shape_fill_rect10_pink700);

    fun applyBackground(button : Button) {
        button.setBackgroundResource(backgroundResourceId)
    }
}
// 질문에 대한 답변을 선택하는 화면
@AndroidEntryPoint
class SelectAnswerFragment : BaseFragment<ActivityQuestionDetailBinding, SelectAnswerViewModel>(
    ActivityQuestionDetailBinding::inflate
) {

    private lateinit var answerList: List<String>
    private lateinit var question: String
    private lateinit var category: String

    private var selectedAnswer = ""

    private var type : String? = null
    private var answerType : String? = null
    private var questionId: Int = -1
    private var answerOptionId: Int = -1
    override val viewModel: SelectAnswerViewModel by viewModels()
    private val navController by lazy { findNavController() }
    private val args by navArgs<SelectAnswerFragmentArgs>()

    private var answerDate : String = ""

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

    override fun onBindLayout() {
        super.onBindLayout()

        if(args.answerType=="BOTH") {
            binding.goToChat.visibility = View.VISIBLE
        }
        binding.btnBack.setOnClickListener {
            navController.popBackStack()
        }

        // 답변 클릭 시
        viewModel.selectedAnswer.observe(this) {
            selectedAnswer = it

            if (it != "") {
                binding.buttonDetailComplete.text= "답변완료"
                setCompleteButton()
            }
        }


        // 답변 완료
        binding.buttonDetailComplete.setOnClickListener {
                viewModel.postAnswer(RequestAnswerRequest(
                    questionId = args.questionId,
                    answerType = "MULTIPLE_CHOICE",
                    answerContent = selectedAnswer
                ))

            if (type!= null ) {
                val action =
                    SelectAnswerFragmentDirections.actionSelectAnswerFragmentToLoadingFragment(
                        question = question, answerDate = answerDate, answer = selectedAnswer,
                        answerType = "MULTIPLE_CHOICE", category = type!!
                    )
                navController.navigate(action)
            }
        }



        binding.goToChat.setOnClickListener {
            val action = SelectAnswerFragmentDirections.actionSelectAnswerFragmentToConversationQuestionFragment(args.questionId, args.type, args.answerType)
            navController.navigate(action)

        }
        val now = System.currentTimeMillis()
        val date = Date(now)
        val sdf = SimpleDateFormat("yyyy년 MM월 dd일의 대화")
        val getTime = sdf.format(date);

        binding.txtToday.text =getTime


        if(questionId > 0) {
            QuestionManager.questionId = questionId
            viewModel.loadQuestionDetail(
                DetailQuestionRequest(
                    isRandom = args.isRandom, questionId)
            )
            setPreviousAnswerButton()
        }

        setQuestionDetail()



    }

    private fun setQuestionDetail() {
        lifecycleScope.launch {
            viewModel.uiState.collectLatest {
                when(it) {
                    is UiState.Loading ->{}
                    is UiState.Success -> {
                        binding.constraintQuestionDetail.visibility = View.VISIBLE
                        binding.textviewDetailQuestion.text = it.data.question.questionContent

                        if(it.data.question.answerType =="BOTH") {
                            binding.goToChat.visibility = View.VISIBLE
                        }
                        question = it.data.question.questionContent
                        category = it.data.question.category
                        answerDate = it.data.question.answerDate
                        setAnswerTypeCompose(it.data.question.answerType,category)
                        if (it.data.multipleChoice[0]=="") return@collectLatest
                        setQuestionAnswerOptionCompose(it.data.multipleChoice,category)
                        setQuestionCategory(category)
                    }

                    is UiState.Error -> {
                       Timber.d("에러 $it.message")
                    }
                }
                    }

            }

    }

    private fun setQuestionCategory(questionCategory : String){
        binding.composeCategory.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                Row {
                    val colorType = ColorMatch.fromKor(questionCategory)
                    if (colorType != null) {
                       QuestionTypeTag(colorType.colorType, questionCategory)
                    }
                }
            }
        }
    }

    private fun setAnswerTypeCompose(answerType: String,category : String) {
        binding.composeDetailQuestiontype.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                Row {
                    val colorType = ColorMatch.fromKor(category)
                    if (colorType != null) {
                        AnswerTypeTag(colorType.colorType, "선택형")
                    }
                }
            }
        }
    }

    private fun setQuestionAnswerOptionCompose(answerList: List<String>,category : String) {
        this.answerList = answerList
        binding.composeDetailAnsweroption.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AnswerOptionList(answerList, viewModel, category = category)
            }
        }
    }

    private fun setCompleteButton(){
        val type =  when(type) {
            "일상" -> TYPE.DAILY
            "학교" -> TYPE.SCHOOL
            "가족" -> TYPE.FAMILY
            "친구" -> TYPE.FRIEND
            "관심사" -> TYPE.INTEREST
            else -> TYPE.DAILY
        }

        binding.buttonDetailComplete.apply{
            type.applyBackground(this)
        }
        binding.buttonDetailComplete.isClickable = true
        binding.buttonDetailComplete.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )




    }

    private fun setPreviousAnswerButton() {
        binding.buttonDetailAnswerbefore.setOnClickListener {
            val action = SelectAnswerFragmentDirections.actionSelectAnswerFragmentToPreviousAnswerFragment(args.questionId.toString())
            navController.navigate(action)
            QuestionManager.questionId = questionId
        }
    }

    private fun moveToQuestionAnswer(answerOptionId: Int) {
        navController.navigate(R.id.answerFragment)
    }
}