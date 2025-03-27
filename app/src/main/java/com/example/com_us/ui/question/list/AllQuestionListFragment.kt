package com.example.com_us.ui.question.list

import android.content.Intent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.com_us.R
import com.example.com_us.base.fragment.BaseFragment
import com.example.com_us.databinding.FragmentQuestionBinding
import com.example.com_us.ui.base.UiState
import com.example.com_us.ui.compose.QuestionListItem
import com.example.com_us.ui.question.select.SelectAnswerActivity
import com.example.com_us.util.ThemeType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// 바텀 네비게이션바에서 햄버거 버튼 클릭 시 이동하는 질문 리스트 화면
@AndroidEntryPoint
class AllQuestionListFragment : BaseFragment<FragmentQuestionBinding,AllQuestionListViewModel>(
    FragmentQuestionBinding::inflate
),View.OnClickListener {

    override val viewModel: AllQuestionListViewModel by viewModels()
    private var lastSelectedView: TextView? = null
    private lateinit var selectedView:TextView
    private lateinit var themeKor:String


    override fun onBindLayout() {
        super.onBindLayout()

        val themeAllView = binding.includeThemeAll
        viewModel.updateSelectedThemeId(themeAllView.textviewTheme.id)
        setThemeClickListener()
        setThemeList()
        setComposeList()
    }


    // 선택한 카테고리에 맞는 질문 리스트 얻기
    private fun setThemeList() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.selectedThemeId.collect {
                    selectedView = binding.root.findViewById(it)
                    themeKor = selectedView.text.toString()
                    var category = ThemeType.fromKor(themeKor).toString()
                    if(category == ThemeType.ALL.toString()) category = ""
                    if (category == "null") return@collect
                    viewModel.loadQuestionListByCate(category)
                }
            }
        }

    }

    // 각 질문을 담을 아이템
    private fun setComposeList() {
        // 데이터 리스트
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.apiResult.collectLatest {
                    when(it) {
                        is UiState.Loading -> {
                            binding.progress.visibility = View.VISIBLE
                        }
                        is UiState.Success -> {
                            if (it.data.isNotEmpty()) {
                                binding.progress.visibility = View.GONE
                                setThemeSelected()
                                binding.constraintQuestion.visibility = View.VISIBLE
                                binding.textviewQuestionCount.text = String.format(
                                    resources.getString(R.string.question_title_question_count),
                                    it.data.size
                                )
                                binding.composeviewQuestion.apply {
                                    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                                    setContent {
                                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                                            items(it.data.size) { idx ->
                                                QuestionListItem(
                                                    viewModel ,
                                                    data = it.data[idx],
                                                    onClick = {
                                                        moveToSelectAnswer(it.data[idx].id, it.data[idx].category)
                                                    })
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        is UiState.Error -> {
                            binding.constraintQuestion.visibility = View.GONE
                            Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                        }

                    }
                }
            }
        }

    }

    private fun setThemeClickListener(){
        binding.includeThemeAll.textviewTheme.setOnClickListener(this)
        binding.includeThemeDaily.textviewTheme.setOnClickListener(this)
        binding.includeThemeFamily.textviewTheme.setOnClickListener(this)
        binding.includeThemeFriend.textviewTheme.setOnClickListener(this)
        binding.includeThemeRandom.textviewTheme.setOnClickListener(this)
        binding.includeThemeInterest.textviewTheme.setOnClickListener(this)
        binding.includeThemeSchool.textviewTheme.setOnClickListener(this)
    }

    private fun setThemeSelected() {
        binding.textviewQuestionTitle.text = String.format(resources.getString(R.string.question_title), themeKor)

        lastSelectedView?.let { view ->
            view.setBackgroundResource(R.drawable.shape_stroke_rect5_gray200)  // 기본 배경 리소스로 변경
            view.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_500))  // 기본 텍스트 색상으로 변경
        }

        selectedView.let { view ->
            view.setBackgroundResource(R.drawable.shape_fill_rect5_gray700)
            view.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }

        lastSelectedView = selectedView
    }

    private fun moveToSelectAnswer(questionId: Long,type : String) {
        val intent = Intent(activity, SelectAnswerActivity::class.java)
        intent.putExtra("type",type)
        intent.putExtra("questionId", questionId)
        startActivity(intent)
    }

    override fun onClick(view: View) {
        viewModel.updateSelectedThemeId(view.id)
    }

}