package com.example.com_us.ui.question

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.example.com_us.R
import com.example.com_us.data.response.question.Answer
import com.example.com_us.databinding.ActivityQuestionPreviousAnswerBinding
import com.example.com_us.ui.compose.AnswerHistoryItem
import com.example.com_us.ui.compose.AnswerTypeTag
import com.example.com_us.util.ColorMatch

class QuestionPreviousAnswerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionPreviousAnswerBinding

    private val questionViewModel: QuestionViewModel by viewModels { QuestionViewModelFactory(applicationContext) }
    private var questionId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQuestionPreviousAnswerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActionBar()

        questionId = intent.getLongExtra("questionId", -1)

        println(questionId)
        if(questionId > -1) questionViewModel.loadPreviousAnswer(questionId)

        questionViewModel.answerPrevious.observe(this) {
            setQuestion(it.question.questionCount, it.question.questionContent)
            setQuestionTypeCompose(it.question.category, it.question.answerType)
            setComposeList(it.answerList)
        }
    }

    private fun setActionBar() {
        setSupportActionBar(binding.includePreviousToolbar.toolbar)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_left)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setQuestion(convCount: Int, question: String) {
        binding.textviewPreviousConvCount.text = String.format(resources.getString(R.string.question_previous_conv_count), convCount)
        binding.textviewPreviousQuestion.text = question
    }

    private fun setQuestionTypeCompose(category: String, answerType: String) {
        binding.composePreviousQuestiontype.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                Row {
                    ColorMatch.fromKor(category)?.let { AnswerTypeTag(it.colorType, category) }
                    ColorMatch.fromKor(answerType)?.let { AnswerTypeTag(it.colorType, answerType) }
                }
            }
        }
    }

    private fun setComposeList(answerList: List<Answer>) {
        binding.textviewPreviousAnswerCount.text = String.format(resources.getString(R.string.question_previous_answer_count), answerList.size)
        binding.composePreviousAnswerlist.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                var columCount = 2
                LazyVerticalGrid(
                    columns = GridCells.Fixed(columCount)
                ) {
                    items(answerList.size) { i ->
                        var idx = answerList.size - (i+1)
                        AnswerHistoryItem(
                            date = answerList[idx].createdAt,
                            answer = answerList[idx].answerContent,
                        )
                    }
                }
            }
        }
    }

}