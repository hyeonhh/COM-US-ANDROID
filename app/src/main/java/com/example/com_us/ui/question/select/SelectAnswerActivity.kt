package com.example.com_us.ui.question.select

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.com_us.R
import com.example.com_us.base.activity.BaseActivity
import com.example.com_us.databinding.ActivityQuestionDetailBinding
import com.example.com_us.ui.base.UiState
import com.example.com_us.ui.compose.AnswerOptionList
import com.example.com_us.ui.compose.AnswerTypeTag
import com.example.com_us.ui.question.previous.PreviousAnswerActivity
import com.example.com_us.ui.question.result.ResultBeforeSignActivity
import com.example.com_us.util.ColorMatch
import com.example.com_us.util.QuestionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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
class SelectAnswerActivity : BaseActivity<ActivityQuestionDetailBinding,SelectAnswerViewModel>(
    ActivityQuestionDetailBinding::inflate
) {

    private lateinit var answerList: List<String>
    private lateinit var question: String
    private lateinit var category: String

    private var answerType : String? = null
    private var questionId: Long = -1
    private var answerOptionId: Int = -1
    override val viewModel: SelectAnswerViewModel by viewModels()

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        questionId = intent.getLongExtra("questionId", 0L)
        answerType = intent.getStringExtra("type")

        return super.onCreateView(name, context, attrs)

    }

    override fun onBindLayout() {
        super.onBindLayout()
        val now = System.currentTimeMillis()
        val date = Date(now)
        val sdf = SimpleDateFormat("yyyy년 MM월 dd일의 대화")
        val getTime = sdf.format(date);

        binding.txtToday.text =getTime



        if(questionId > 0) {
            QuestionManager.questionId = questionId
            viewModel.loadQuestionDetail(questionId)
            setPreviousAnswerButton()
        }

        setQuestionDetail()

        // 답변 클릭 시
        viewModel.selectedAnswerOptionId.observe(this) {
            if (it > -1) {
                binding.buttonDetailComplete.text= "답변완료"
                setCompleteButton()
                answerOptionId = it
            }
        }

    }

    private fun setQuestionDetail() {
        lifecycleScope.launch {
            viewModel.uiState.collectLatest {
                when(it) {
                    is UiState.Loading ->{}
                    is UiState.Success -> {
                        binding.constraintQuestionDetail.visibility = View.VISIBLE
                        binding.textviewDetailQuestion.text = it.data.question.questionContent
                        question = it.data.question.questionContent
                        category = it.data.question.category
                        setQuestionTypeCompose(it.data.question.answerType,category)
                        if (it.data.answerList ==null) return@collectLatest
                        setQuestionAnswerOptionCompose(it.data.answerList,category)
                    }

                    is UiState.Error -> {
                        Toast.makeText(
                            this@SelectAnswerActivity,
                            it.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                    }

            }

    }

    private fun setQuestionTypeCompose(answerType: String,category : String) {
        binding.composeDetailQuestiontype.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                Row {
                    ColorMatch.fromKor(category)?.let { AnswerTypeTag(it.colorType, category) }
                    ColorMatch.fromKor(answerType)?.let { AnswerTypeTag(it.colorType, answerType) }
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
        val type =  when(answerType) {
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
                this,
                R.color.white
            )
        )



        // 완료 버튼 클릭
        binding.buttonDetailComplete.setOnClickListener{
            if(answerOptionId > -1) {
                // 답변 저장
                moveToQuestionAnswer(answerOptionId)
            }
        }
    }

    private fun setPreviousAnswerButton() {
        binding.buttonDetailAnswerbefore.setOnClickListener {
            val intent = Intent(this, PreviousAnswerActivity::class.java)
            QuestionManager.questionId = questionId
            intent.putExtra("questionId", questionId)
            startActivity(intent)
        }
    }

    private fun moveToQuestionAnswer(answerOptionId: Int) {
        val intent = Intent(this, ResultBeforeSignActivity::class.java)
        intent.putExtra("question", question)
        intent.putExtra("category", category)
        intent.putExtra("answer", answerList[answerOptionId])
        startActivity(intent)
        finish()
    }
}