package com.example.com_us.ui.question.select

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
import com.example.com_us.util.ColorMatch
import com.example.com_us.ui.compose.AnswerOptionList
import com.example.com_us.ui.compose.AnswerTypeTag
import com.example.com_us.ui.question.result.ResultBeforeSignActivity
import com.example.com_us.ui.question.previous.PreviousAnswerActivity
import com.example.com_us.util.QuestionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// 질문에 대한 답변을 선택하는 화면
@AndroidEntryPoint
class SelectAnswerActivity : BaseActivity<ActivityQuestionDetailBinding,SelectAnswerViewModel>(
    ActivityQuestionDetailBinding::inflate
) {

    private lateinit var answerList: List<String>
    private lateinit var question: String
    private lateinit var category: String

    private var questionId: Long = -1
    private var answerOptionId: Int = -1
    override val viewModel: SelectAnswerViewModel by viewModels()

    override fun onBindLayout() {
        super.onBindLayout()

        questionId = intent.getLongExtra("questionId", 0L)

        if(questionId > 0) {
            QuestionManager.questionId = questionId
            viewModel.loadQuestionDetail(questionId)
            setPreviousAnswerButton()
        }

        setQuestionDetail()

        viewModel.selectedAnswerOptionId.observe(this) {
            if (it > -1) {
                setCompleteButton()
                answerOptionId = it
            }
        }

    }

    private fun setQuestionDetail() {
        lifecycleScope.launch {
            viewModel.uiState.collect {
                when(it) {
                    is UiState.Loading ->{}
                    is UiState.Success -> {
                        binding.constraintQuestionDetail.visibility = View.VISIBLE
                        binding.textviewDetailQuestion.text = it.data.question.questionContent
                        question = it.data.question.questionContent
                        category = it.data.question.category
                        setQuestionTypeCompose(it.data.question.answerType)
                        setQuestionAnswerOptionCompose(it.data.answerList)
                    }

                    is UiState.Error -> {
                        Toast.makeText(
                            this@SelectAnswerActivity,
                            it.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                }

                    }

            }

    }

    private fun setQuestionTypeCompose(answerType: String) {
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

    private fun setQuestionAnswerOptionCompose(answerList: List<String>) {
        this.answerList = answerList
        binding.composeDetailAnsweroption.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AnswerOptionList(answerList, viewModel)
            }
        }
    }

    private fun setCompleteButton(){
        binding.buttonDetailComplete.isClickable = true
        binding.buttonDetailComplete.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.white
            )
        )
        binding.buttonDetailComplete.setBackgroundResource(R.drawable.shape_fill_rect10_orange700)

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