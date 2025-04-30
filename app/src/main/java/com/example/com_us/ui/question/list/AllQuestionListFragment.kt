package com.example.com_us.ui.question.list

import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.com_us.R
import com.example.com_us.base.fragment.BaseFragment
import com.example.com_us.databinding.FragmentQuestionBinding
import com.example.com_us.ui.base.UiState
import com.example.com_us.ui.compose.QuestionListItem
import com.example.com_us.util.ColorMatch
import com.example.com_us.util.ThemeType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select

// 바텀 네비게이션바에서 햄버거 버튼 클릭 시 이동하는 질문 리스트 화면
@AndroidEntryPoint
class AllQuestionListFragment : BaseFragment<FragmentQuestionBinding,AllQuestionListViewModel>(
    FragmentQuestionBinding::inflate
){

    private val navController by lazy {findNavController()}

    override val viewModel: AllQuestionListViewModel by viewModels()
    private  var selectedView:String=""
    private lateinit var themeKor:String


    override fun onBindLayout() {
        super.onBindLayout()

        // 스피너 이벤트 처리
        binding.spinner.onItemSelectedListener = object  : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
               val selectedItem = parent?.getItemAtPosition(position).toString()
                binding.spinner.setSelection(position)
                when(position){
                    0 -> {
                        viewModel.loadQuestionListByCate(selectedView,"")

                    }
                    1 -> {
                        viewModel.loadQuestionListByCate(selectedView,"MOST_CHAT")

                    }
                    2 -> {
                        viewModel.loadQuestionListByCate(selectedView,"LEAST_CHAT")

                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                binding.spinner.setSelection(0)
            }

        }

        //스피너 정의
        val items = arrayOf("전체", "대화 많이 한 순","대화 적은 순")
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            items
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter

        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.root.updatePadding(bottom = insets.bottom)
            // Return the windowInsets so insets can be applied to other views
            WindowInsetsCompat.CONSUMED
        }


        setThemeClickListener()
        setComposeList()
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
                                        LazyColumn(
                                        modifier = Modifier.fillMaxSize()
                                            .padding(bottom = 8.dp) // 컴포즈 내에서 추가 여백 제공
                                        ) {
                                            items(it.data.size) { idx ->
                                                QuestionListItem(
                                                    viewModel ,
                                                    data = it.data[idx],
                                                    onClick = {
                                                        if (it.data[idx].answerType =="MULTIPLE_CHOICE") {
                                                            moveToSelectAnswer(
                                                               answerType =  it.data[idx].answerType,
                                                                      questionId =   it.data[idx].id.toInt(),
                                                                type = it.data[idx].category
                                                            )
                                                        }
                                                        if(it.data[idx].answerType =="SENTENCE") {
                                                            moveToConversationAnswer(
                                                                answerType =  it.data[idx].answerType,
                                                                questionId =   it.data[idx].id.toInt(),
                                                                type = it.data[idx].category
                                                            )
                                                        }
                                                        if(it.data[idx].answerType =="BOTH"){
                                                            // Both
                                                            moveToSelectAnswer(
                                                                answerType =  it.data[idx].answerType,
                                                                questionId =   it.data[idx].id.toInt(),
                                                                type = it.data[idx].category
                                                            )

                                                    }
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
        binding.includeThemeAll.textviewTheme.setOnClickListener{
            onClickTheme(it as TextView)
            selectedView = "ALL"
            viewModel.loadQuestionListByCate("","")

        }
        binding.includeThemeDaily.textviewTheme.setOnClickListener{
            onClickTheme(it as TextView)
            selectedView = "DAILY"
            viewModel.loadQuestionListByCate(selectedView,"")
        }
        binding.includeThemeFamily.textviewTheme.setOnClickListener{
            onClickTheme(it as TextView)
            selectedView = "FAMILY"
            viewModel.loadQuestionListByCate(selectedView,"")
        }
        binding.includeThemeFriend.textviewTheme.setOnClickListener{
            onClickTheme(it as TextView)
            selectedView = "FRIEND"
            viewModel.loadQuestionListByCate(selectedView,"")
        }

        binding.includeThemeInterest.textviewTheme.setOnClickListener {
            onClickTheme(it as TextView)
            selectedView = "HOBBY"
            viewModel.loadQuestionListByCate(selectedView,"")
        }
        binding.includeThemeSchool.textviewTheme.setOnClickListener{
            onClickTheme(it as TextView)
            selectedView = "SCHOOL"
            viewModel.loadQuestionListByCate(selectedView,"")
        }

    }

    private fun onClickTheme(view: TextView){
        initTheme()
        view.let {
            view.setBackgroundResource(R.drawable.shape_fill_rect5_gray700)
            view.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }
    }

    private fun initTheme(){
        binding.includeThemeAll.textviewTheme.let{
            it.setBackgroundResource(R.drawable.shape_stroke_rect5_gray200)  // 기본 배경 리소스로 변경
            it.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_500))  // 기본 텍스트 색상으로 변경
        }
        binding.includeThemeDaily.textviewTheme.let{
            it.setBackgroundResource(R.drawable.shape_stroke_rect5_gray200)  // 기본 배경 리소스로 변경
            it.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_500))  // 기본 텍스트 색상으로 변경
        }
        binding.includeThemeFamily.textviewTheme.let{
            it.setBackgroundResource(R.drawable.shape_stroke_rect5_gray200)  // 기본 배경 리소스로 변경
            it.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_500))  // 기본 텍스트 색상으로 변경
        }
        binding.includeThemeFriend.textviewTheme.let{
            it.setBackgroundResource(R.drawable.shape_stroke_rect5_gray200)  // 기본 배경 리소스로 변경
            it.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_500))  // 기본 텍스트 색상으로 변경
        }

        binding.includeThemeInterest.textviewTheme.let {
            it.setBackgroundResource(R.drawable.shape_stroke_rect5_gray200)  // 기본 배경 리소스로 변경
            it.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_500))  // 기본 텍스트 색상으로 변경
        }
        binding.includeThemeSchool.textviewTheme.let{
            it.setBackgroundResource(R.drawable.shape_stroke_rect5_gray200)  // 기본 배경 리소스로 변경
            it.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_500))  // 기본 텍스트 색상으로 변경
        }

    }

    private fun setThemeSelected() {
        themeKor = when(selectedView){
            "DAILY" -> "일상"
            "FAMILY" -> "가족"
            "FRIEND" -> "친구"
            "HOBBY" -> "관심사"
            "SCHOOL" -> "학교"
            else -> "전체"
        }
        binding.textviewQuestionTitle.text = String.format(resources.getString(R.string.question_title), themeKor)
    }

    private fun moveToSelectAnswer(questionId: Int,type : String,        answerType : String,
    ) {
       val action =  AllQuestionListFragmentDirections.actionNavigationQuestionsToSelectAnswerFragment(
           questionId = questionId, type=type, answerType = answerType
       )
        navController.navigate(action)
    }

    private fun moveToConversationAnswer(
        answerType : String,
        questionId: Int,
        type : String) {

        val action =  AllQuestionListFragmentDirections.actionNavigationQuestionsToConversationQuestionFragment(
            questionId = questionId, type=type, answerType = answerType
        )
        navController.navigate(action)

    }


}