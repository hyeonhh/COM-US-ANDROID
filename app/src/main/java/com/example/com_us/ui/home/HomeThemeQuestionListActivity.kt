package com.example.com_us.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.example.com_us.R
import com.example.com_us.databinding.ActivityThemeQuestionListBinding
import com.example.com_us.ui.question.QuestionDetailActivity
import com.example.com_us.ui.compose.QuestionListItem
import com.example.com_us.ui.question.QuestionViewModel
import com.example.com_us.ui.question.QuestionViewModelFactory

class HomeThemeQuestionListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityThemeQuestionListBinding
    private val questionViewModel: QuestionViewModel by viewModels { QuestionViewModelFactory(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityThemeQuestionListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val theme = intent.getStringExtra("theme").toString()
        val themeKor = intent.getStringExtra("themeKor")
        binding.textviewTitle.text = String.format(resources.getString(R.string.theme_question_list_title), themeKor)

        questionViewModel.loadQuestionListByCate(theme)

        setActionBar()
        setComposeList()
    }

    private fun setComposeList() {
        questionViewModel.questionListByCate.observe(this) {
            binding.composeviewTheme.apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    LazyColumn {
                        items(it.size) { idx ->
                            QuestionListItem(data = it[idx], onClick = { moveToQuestionDetail(it[idx].id) })
                        }
                    }
                }
            }
        }
    }


    private fun setActionBar() {
        setSupportActionBar(binding.includeToolbar.toolbar)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_left)
    }

    private fun moveToQuestionDetail(questionId: Long) {
        val intent = Intent(this, QuestionDetailActivity::class.java)
        intent.putExtra("questionId", questionId)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}