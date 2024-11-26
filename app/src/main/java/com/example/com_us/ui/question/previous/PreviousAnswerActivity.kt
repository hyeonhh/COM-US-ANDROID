package com.example.com_us.ui.question.previous

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.com_us.R
import com.example.com_us.data.model.question.response.question.Answer
import com.example.com_us.databinding.ActivityQuestionPreviousAnswerBinding
import com.example.com_us.ui.base.UiState
import com.example.com_us.ui.compose.AnswerHistoryItem
import com.example.com_us.ui.compose.AnswerTypeTag
import com.example.com_us.util.ColorMatch
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// 이전 답변을 보여주는 화면
@AndroidEntryPoint
class PreviousAnswerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionPreviousAnswerBinding

    private val viewModel: PreviousAnswerViewModel by viewModels()
    private var questionId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQuestionPreviousAnswerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActionBar()

        questionId = intent.getLongExtra("questionId", -1)

        println(questionId)
        if(questionId > -1) viewModel.loadPreviousAnswer(questionId)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState.collect{
                    when(it){
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            setQuestion(it.data.question.questionCount, it.data.question.questionContent)
                            setQuestionTypeCompose(it.data.question.category, it.data.question.answerType)
                            setComposeList(it.data.answerList)
                        }
                       is UiState.Error ->
                            Toast.makeText(this@PreviousAnswerActivity, it.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }

                }
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