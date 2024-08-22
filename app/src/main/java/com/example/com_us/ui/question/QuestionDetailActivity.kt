package com.example.com_us.ui.question

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.content.ContextCompat
import com.example.com_us.R
import com.example.com_us.databinding.ActivityQuestionDetailBinding
import com.example.com_us.util.ColorMatch
import com.example.com_us.ui.compose.AnswerOptionList
import com.example.com_us.ui.compose.AnswerTypeTag

class QuestionDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionDetailBinding
    private lateinit var answerList: List<String>
    private var answerOptionId: Int = -1
    private lateinit var question: String
    private val questionViewModel: QuestionViewModel by viewModels { QuestionViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val questionId = intent.getLongExtra("questionId", 0L)

        if(questionId > 0) questionViewModel.loadQuestionDetail(questionId)

        setQuestionDetail()

        questionViewModel.selectedAnswerOptionId.observe(this) {
            if (it > -1) {
                setCompleteButton()
                answerOptionId = it
            }
        }
    }

    private fun setQuestionDetail() {
        questionViewModel.questionDetail.observe(this) {
            binding.textviewDetailQuestion.text = it.question.questionContent
            this.question = it.question.questionContent
            setQuestionTypeCompose(it.question.category, it.question.answerType)
            setQuestionAnswerOptionCompose(it.answerList)
        }
    }

    private fun setQuestionTypeCompose(category: String, answerType: String) {
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
                AnswerOptionList(answerList, questionViewModel)
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
            if(answerOptionId > -1) moveToQuestionAnswer(answerOptionId)
        }
    }

    private fun moveToQuestionAnswer(answerOptionId: Int) {
        val intent = Intent(this, QuestionAnswerActivity::class.java)
        intent.putExtra("question", question)
        intent.putExtra("answer", answerList[answerOptionId])
        startActivity(intent)
    }
}