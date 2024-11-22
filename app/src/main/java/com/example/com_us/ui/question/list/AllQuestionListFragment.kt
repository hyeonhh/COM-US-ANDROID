package com.example.com_us.ui.question.list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.com_us.R
import com.example.com_us.databinding.FragmentQuestionBinding
import com.example.com_us.ui.ApiResult
import com.example.com_us.ui.compose.QuestionListItem
import com.example.com_us.ui.question.select.SelectAnswerActivity
import com.example.com_us.util.ThemeType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// 바텀 네비게이션바에서 햄버거 버튼 클릭 시 이동하는 질문 리스트 화면
@AndroidEntryPoint
class AllQuestionListFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentQuestionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AllQuestionListViewModel by viewModels()
    private var lastSelectedView: TextView? = null
    private lateinit var selectedView:TextView
    private lateinit var themeKor:String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestionBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val themeAllView = binding.includeThemeAll
        viewModel.updateSelectedThemeId(themeAllView.textviewTheme.id)

        setThemeList()
        setComposeList()
        setThemeClickListener()

    }

    // 선택한 카테고리에 맞는 질문 리스트 얻기
    private fun setThemeList() {
        viewModel.selectedThemeId.observe(viewLifecycleOwner) {
            selectedView = binding.root.findViewById(it)
            themeKor = selectedView.text.toString()
            var category = ThemeType.fromKor(themeKor).toString()
            if(category == ThemeType.ALL.toString()) category = ""
            viewModel.loadQuestionListByCate(category)
        }
    }

    // 각 질문을 담을 아이템
    private fun setComposeList() {
        // 데이터 리스트
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.apiResult.collect {
                    when(it) {
                        is ApiResult.Success -> {
                            if (it.data.isNotEmpty()) {
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
                                                    data = it.data[idx],
                                                    onClick = { movieToSelectAnswer(it.data[idx].id) })
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else ->  {
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

        selectedView?.let { view ->
            view.setBackgroundResource(R.drawable.shape_fill_rect5_gray700)
            view.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }

        lastSelectedView = selectedView
    }

    private fun movieToSelectAnswer(questionId: Long) {
        val intent = Intent(activity, SelectAnswerActivity::class.java)
        intent.putExtra("questionId", questionId)
        startActivity(intent)
    }

    override fun onClick(view: View) {
        viewModel.updateSelectedThemeId(view.id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}