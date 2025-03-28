package com.example.com_us.ui.question.theme

import android.content.Intent
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.lifecycleScope
import com.example.com_us.R
import com.example.com_us.base.activity.BaseActivity
import com.example.com_us.databinding.ActivityThemeQuestionListBinding
import com.example.com_us.ui.base.UiState
import com.example.com_us.ui.question.select.selection.SelectAnswerActivity
import com.example.com_us.ui.compose.QuestionListItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ThemeQuestionListActivity : BaseActivity<ActivityThemeQuestionListBinding,ThemeQuestionListViewModel>(
    ActivityThemeQuestionListBinding::inflate
) {


    override val viewModel: ThemeQuestionListViewModel by viewModels()

    override fun onBindLayout() {
        super.onBindLayout()
        val theme = intent.getStringExtra("theme")
        theme?.let{
            viewModel.loadQuestionListByCate(theme)
        }

        val themeKor = intent.getStringExtra("themeKor")
        binding.textviewTitle.text = String.format(resources.getString(R.string.theme_question_list_title), themeKor)
        setActionBar()
        setComposeList()

    }

    private fun setComposeList() {
        lifecycleScope.launch {
            viewModel.uiState.collect {
                when (it) {
                    is UiState.Success -> {
                        binding.composeviewTheme.apply {
                            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                            setContent {
                                LazyColumn {
                                    println(it.data)
                                    items(it.data.size) { idx ->
                                        QuestionListItem(
                                            viewModel2 = viewModel,
                                            data = it.data[idx],
                                            onClick = { moveToQuestionDetail(it.data[idx].id) })
                                    }
                                }
                            }
                        }
                    }
                   is UiState.Error -> {
                       Toast.makeText(this@ThemeQuestionListActivity,it.toString(),Toast.LENGTH_SHORT).show()
                   }
                    is UiState.Loading -> {}
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
        val intent = Intent(this, SelectAnswerActivity::class.java)
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