package com.example.com_us.ui.question

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.Observer
import com.example.com_us.databinding.ActivityQuestionDetailBinding
import com.example.com_us.util.ColorMatch
import com.example.com_us.ui.compose.AnswerOptionItem
import com.example.com_us.ui.compose.AnswerTypeTag

class QuestionDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionDetailBinding
    private val questionViewModel: QuestionViewModel by viewModels { QuestionViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val questionId = intent.getLongExtra("questionId", 0L)

        if(questionId > 0) questionViewModel.loadQuestionDetail(questionId)

        setQuestionDetail()

    }

    private fun setQuestionDetail() {
        questionViewModel.questionDetail.observe(this, Observer {
            binding.textviewDetailQuestion.text = it.question.questionContent
            setQuestionTypeCompose(it.question.category, it.question.answerType)
            setQuestionAnswerOptionCompose(it.answerList)
        })
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
        var columCount = 2
        binding.composeDetailAnsweroption.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(columCount)
                ) {
                    items(answerList.size) { item ->
                        AnswerOptionItem(answerList[item])
                    }
                }
            }
        }
    }
}