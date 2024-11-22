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
import com.example.com_us.databinding.ActivityQuestionDetailBinding
import com.example.com_us.ui.UiState
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
class SelectAnswerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionDetailBinding
    private lateinit var answerList: List<String>
    private lateinit var question: String
    private lateinit var category: String

    private var questionId: Long = -1
    private var answerOptionId: Int = -1
    private val viewModel: SelectAnswerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


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
                       if (it is UiState.Success) {
                            binding.constraintQuestionDetail.visibility = View.VISIBLE
                            binding.textviewDetailQuestion.text = it.data.question.questionContent
                            question = it.data.question.questionContent
                            category = it.data.question.category
                            setQuestionTypeCompose(it.data.question.answerType)
                            setQuestionAnswerOptionCompose(it.data.answerList)
                        }

                        if  (it is UiState.Error) {
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
        binding.buttonDetailComplete.setOnClickListener{
            if(answerOptionId > -1) {
                moveToQuestionAnswer(answerOptionId)
            }
        }
    }

    private fun setPreviousAnswerButton() {
        binding.buttonDetailAnswerbefore.setOnClickListener {
            val intent = Intent(this, PreviousAnswerActivity::class.java)
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

//    override fun onServerSuccess() {
//        binding.constraintQuestionDetail.visibility = View.VISIBLE
//    }
//
//    override fun onServerFailure() {
//        Toast.makeText(this, getString(R.string.question_detail_msg_when_server_failure), Toast.LENGTH_SHORT).show()
//        finish()
//    }
}