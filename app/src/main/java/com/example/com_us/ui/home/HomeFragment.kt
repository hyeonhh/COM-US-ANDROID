package com.example.com_us.ui.home

import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.com_us.R
import com.example.com_us.base.fragment.BaseFragment
import com.example.com_us.data.model.home.Block
import com.example.com_us.data.model.home.QuestionCounts
import com.example.com_us.data.model.home.RandomQuestion
import com.example.com_us.databinding.FragmentHomeBinding
import com.example.com_us.ui.base.UiState
import com.example.com_us.ui.compose.AnswerTypeTag
import com.example.com_us.ui.compose.QuestionTypeTag
import com.example.com_us.ui.login.LoginActivity
import com.example.com_us.util.ColorMatch
import com.example.com_us.util.ThemeType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment :
    BaseFragment<FragmentHomeBinding, HomeViewModel>(FragmentHomeBinding::inflate),
    View.OnClickListener {
    override val viewModel: HomeViewModel by viewModels()
    private lateinit var blockList: List<List<View>>
    private var isReload: MutableLiveData<Boolean> = MutableLiveData(false)
    private var randomQuestion =  RandomQuestion()


    private val homeViewModel: HomeViewModel by viewModels()
    private val navController by lazy { findNavController() }


    override fun onBindLayout() {
        super.onBindLayout()

        //랜덤 질문 화면으로 넘어가기
        binding.randomQuestionBox.layout.setOnClickListener {
            when(randomQuestion.answerType){
                "BOTH" -> {
                    val action = HomeFragmentDirections.actionNavigationHomeToSelectAnswerFragment(
                        questionId = randomQuestion.questionId,
                        type = randomQuestion.category,
                        answerType = randomQuestion.answerType,
                        isRandom = true
                    )
                    navController.navigate(action)
                }
                "MULTIPLE_CHOICE" -> {
                   val action = HomeFragmentDirections.actionNavigationHomeToSelectAnswerFragment(
                       questionId = randomQuestion.questionId,
                       type = randomQuestion.category,
                       answerType = randomQuestion.answerType,
                       isRandom = true
                   )
                    navController.navigate(action)

                }
                "SENTENCE" -> {
                    val action = HomeFragmentDirections.actionNavigationHomeToConversationQuestionFragment(
                        questionId = randomQuestion.questionId,
                        type = randomQuestion.category,
                        answerType = randomQuestion.answerType,
                        isRandom = true

                    )
                    navController.navigate(action)
                }
            }
        }


        // 월별 기록 페이지로 넘어가기
        binding.viewUserConversationInfoBox.viewWeekCalendar.btnCalendar.setOnClickListener {
            navController.navigate(R.id.recordFragment)
        }


        lifecycleScope.launch {
           viewModel.loginEvent.observe(this@HomeFragment, {
               val intent = Intent(requireContext(),LoginActivity::class.java)
               startActivity(intent)
           })
        }
        blockList =
            listOf(
                listOf(
                    binding.viewConversation.block1,
                    binding.viewConversation.block2,
                    binding.viewConversation.block3,
                    binding.viewConversation.block4,
                ),
                listOf(
                    binding.viewConversation.block5,
                    binding.viewConversation.block6,
                    binding.viewConversation.block7 ,
                    binding.viewConversation.block8,
                ),
                listOf(
                    binding.viewConversation.block9,
                    binding.viewConversation.block10,
                    binding.viewConversation.block11,
                    binding.viewConversation.block12,

                ),
                listOf(
                    binding.viewConversation.block13,
                    binding.viewConversation.block14,
                    binding.viewConversation.block15,
                    binding.viewConversation.block16,
                )
            )

        setThemeClickListener()
        setSwipeRefresh()
        setHomeData()
    }

    private fun setSwipeRefresh() {
        binding.swiperefreshHome.setOnRefreshListener {
            homeViewModel.loadHomeData()
        }

        isReload.observe(viewLifecycleOwner) {
            if (it) {
                isReload.value = false
                binding.swiperefreshHome.isRefreshing = false
            }
        }
    }

    private fun setThemeClickListener() {
        with(binding) {
            viewConversation.includeHomeDaily.constraint.setOnClickListener(this@HomeFragment)
            viewConversation.includeHomeSchool.constraint.setOnClickListener(this@HomeFragment)
            viewConversation.includeHomeFriend.constraint.setOnClickListener(this@HomeFragment)
            viewConversation.includeHomeFamily.constraint.setOnClickListener(this@HomeFragment)
            viewConversation.includeHomeHobby.constraint.setOnClickListener(this@HomeFragment)
            viewConversation.includeHomeRandom.constraint.setOnClickListener(this@HomeFragment)
        }
    }

    // 일상인지 학교인지 등등
    private fun setQuestionTypeCompose(category : String) {
        binding.randomQuestionBox.category.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                Row {
                    //todo :데이터 연결
                    val colorType = ColorMatch.fromKor(category)
                    if (colorType != null) {
                        QuestionTypeTag(colorType.colorType, category)
                    }
                }
            }
        }
    }

    // 대화형인지 선택형인지
    private fun setAnswerTypeCompose(answerType : String) {
        when (answerType) {
            "BOTH" -> {
                Timber.d("answerType :$answerType")
                binding.randomQuestionBox.answerType2.visibility = View.VISIBLE

                binding.randomQuestionBox.answerType1.apply{
                    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                    setContent {
                        Row {
                            //todo :데이터 연결
                            val colorType = ColorMatch.fromKor("선택형")
                            if (colorType != null) {
                                Timber.d("$colorType")
                                AnswerTypeTag(colorType.colorType, "선택형")
                            }

                        }
                    }
                }
                binding.randomQuestionBox.answerType2.apply{
                    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                    setContent {
                        Row {
                            //todo :데이터 연결
                            val colorType = ColorMatch.fromKor("대화형")
                            if (colorType != null) {
                                Timber.d("$colorType")
                                AnswerTypeTag(colorType.colorType, "대화형")
                            }
                        }
                    }
                }
            }
            "MULTIPLE_CHOICE" -> {
                Timber.d("answerType :$answerType")
                binding.randomQuestionBox.answerType2.visibility = View.GONE

                binding.randomQuestionBox.answerType1.apply{
                    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                    setContent {
                        Row {
                            //todo :데이터 연결
                            val colorType = ColorMatch.fromKor("선택형")
                            if (colorType != null) {
                                Timber.d("$colorType")
                                AnswerTypeTag(colorType.colorType, "선택형")
                            }
                        }
                    }
                }
            }
            "SENTENCE" ->  {
                Timber.d("answerType :$answerType")
                binding.randomQuestionBox.answerType2.visibility = View.GONE
                binding.randomQuestionBox.answerType1.apply {
                    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                    setContent {
                        Row {
                            //todo :데이터 연결
                            val colorType = ColorMatch.fromKor("대화형")
                            if (colorType != null) {
                                Timber.d("$colorType")
                                AnswerTypeTag(colorType.colorType, "대화형")
                            }

                        }
                    }
                }
                }
            }
    }

    private fun setWeekBlockImage(category : String):Drawable {
        val drawable = when(category) {
            "DAILY" ->  resources.getDrawable(R.drawable.ic_face_orange)
            "SCHOOL" ->  resources.getDrawable(R.drawable.ic_face_blue_v2)
            "HOBBY" ->   resources.getDrawable(R.drawable.ic_face_pink)
            "FAMILY" -> resources.getDrawable(R.drawable.ic_face_purple)
            "FRIEND" -> resources.getDrawable(R.drawable.ic_face_green)
            else -> resources.getDrawable(R.drawable.ic_blank_week)
        }
        return drawable
    }
    private fun setHomeData() {
        val emojiText = String(Character.toChars(resources.getInteger(R.integer.waving_hand_sign)))
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.homeUiState.collect {
                    when (it) {
                        is UiState.Success -> {
                            // 단계 표시
                      //    binding.viewConversation.textviewHomeStep.text =  if (it.data.blocks.isEmpty()) "1" else it.data.blocks.first().level.toString()

                          // 주간  캘린더 표시
                            binding.viewUserConversationInfoBox.viewWeekCalendar.txtWeekTitle.text = it.data.userInfo.week

                            it.data.userInfo.weeklyAnswer.forEach {
                                val category = setWeekBlockImage(it.category)
                                when(it.answerDay){
                                    "월" -> {
                                        binding.viewUserConversationInfoBox.viewWeekCalendar.monday.setImageDrawable(category)
                                    }
                                    "화" -> {
                                        binding.viewUserConversationInfoBox.viewWeekCalendar.tuesday.setImageDrawable(category)
                                    }
                                    "수" -> {
                                        binding.viewUserConversationInfoBox.viewWeekCalendar.wednesday.setImageDrawable(category)
                                    }
                                    "목" -> {
                                        binding.viewUserConversationInfoBox.viewWeekCalendar.thursday.setImageDrawable(category)
                                    }
                                    "금" -> {
                                        binding.viewUserConversationInfoBox.viewWeekCalendar.friday.setImageDrawable(category)

                                    }
                                    "토" -> {
                                        binding.viewUserConversationInfoBox.viewWeekCalendar.saturday.setImageDrawable(category)

                                    }
                                    "일" -> {
                                        binding.viewUserConversationInfoBox.viewWeekCalendar.sunday.setImageDrawable(category)

                                    }
                                }
                            }

                            binding.randomQuestionBox.txtQuestion.text = it.data.randomQuestion.questionContent
                            setAnswerTypeCompose(it.data.randomQuestion.answerType)
                            setQuestionTypeCompose(it.data.randomQuestion.category)
                            randomQuestion = it.data.randomQuestion

                           binding.viewUserConversationInfoBox.infoCountBox.setAnswerCount(it.data.userInfo.answerCount)
                           binding.viewUserConversationInfoBox.likeCountBox.setAnswerCount(it.data.userInfo.likeCount)
                            setBlock(it.data.blockBoard)

                            binding.swiperefreshHome.isRefreshing = false
                            binding.progressBar.visibility = View.GONE
                            binding.constraintHome.visibility = View.VISIBLE

                            binding.viewUserConversationInfoBox.textviewHomeGreeting.text =
                                String.format(
                                    resources.getString(R.string.home_title_greeting_user_hi),
                                    it.data.userInfo.name,
                                    emojiText,
                                )

                            Glide
                                .with(this@HomeFragment)
                                .load(it.data.userInfo.imageUrl)
                                .apply(RequestOptions().transform(RoundedCorners(300)))
                                .into(binding.viewUserConversationInfoBox.imageviewHomeUsericon)


                            for (i in it.data.questionCounts) {
                                 when(i.category){
                                     "DAILY"->{
                                         binding.viewConversation.includeHomeDaily.textviewThemeFraction.text = i.count
                                         binding.viewConversation.includeHomeDaily.textviewThemePercent.text =
                                             i.percentage
                                         binding.viewConversation.includeHomeDaily.progressbarTheme.max = i.questionTotalCount
                                         binding.viewConversation.includeHomeDaily.progressbarTheme.progress = i.questionAnsweredCount

                                     }
                                     "SCHOOL" -> {
                                         binding.viewConversation.includeHomeSchool.textviewThemeFraction.text = i.count
                                         binding.viewConversation.includeHomeSchool.textviewThemePercent.text =
                                             i.percentage
                                         binding.viewConversation.includeHomeSchool.progressbarTheme.max = i.questionTotalCount
                                         binding.viewConversation.includeHomeSchool.progressbarTheme.progress = i.questionAnsweredCount
                                     }
                                     "FRIEND" -> {
                                         binding.viewConversation.includeHomeFriend.textviewThemeFraction.text = i.count
                                         binding.viewConversation.includeHomeFriend.textviewThemePercent.text =
                                             i.percentage
                                         binding.viewConversation.includeHomeFriend.progressbarTheme.max = i.questionTotalCount
                                         binding.viewConversation.includeHomeFriend.progressbarTheme.progress = i.questionAnsweredCount
                                     }
                                     "FAMILY" -> {
                                         binding.viewConversation.includeHomeFamily.textviewThemeFraction.text = i.count
                                         binding.viewConversation.includeHomeFamily.textviewThemePercent.text =
                                             i.percentage
                                         binding.viewConversation.includeHomeFamily.progressbarTheme.max = i.questionTotalCount
                                         binding.viewConversation.includeHomeFamily.progressbarTheme.progress = i.questionAnsweredCount
                                     }
                                     "HOBBY" ->{
                                         binding.viewConversation.includeHomeHobby.textviewThemeFraction.text = i.count
                                         binding.viewConversation.includeHomeHobby.textviewThemePercent.text =
                                             i.percentage
                                         binding.viewConversation.includeHomeHobby.progressbarTheme.max = i.questionTotalCount
                                         binding.viewConversation.includeHomeHobby.progressbarTheme.progress = i.questionAnsweredCount
                                     }
                                 }

                            }
                            isReload.value = true
                        }
                        is UiState.Error -> {
                            Toast.makeText(context, "잠시 후에 다시 시도해주세요!", Toast.LENGTH_SHORT).show()
                        }
                        is UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.include_home_daily -> {
                moveToQuestionList(ThemeType.DAILY.toString())
            }
            R.id.include_home_school -> {
                moveToQuestionList(ThemeType.SCHOOL.toString())
            }
            R.id.include_home_friend -> {
                moveToQuestionList(ThemeType.FRIEND.toString())
            }
            R.id.include_home_family -> {
                moveToQuestionList(ThemeType.FAMILY.toString())
            }
            R.id.include_home_hobby -> {
                moveToQuestionList(ThemeType.HOBBY.toString())
            }
            R.id.include_home_random -> {
                    when(randomQuestion.answerType){
                        "BOTH" -> {
                            val action = HomeFragmentDirections.actionNavigationHomeToSelectAnswerFragment(
                                questionId = randomQuestion.questionId,
                                type = randomQuestion.category,
                                answerType = randomQuestion.answerType,
                                isRandom = true
                            )
                            navController.navigate(action)
                        }
                        "MULTIPLE_CHOICE" -> {
                            val action = HomeFragmentDirections.actionNavigationHomeToSelectAnswerFragment(
                                questionId = randomQuestion.questionId,
                                type = randomQuestion.category,
                                answerType = randomQuestion.answerType,
                                isRandom = true
                            )
                            navController.navigate(action)

                        }
                        "SENTENCE" -> {
                            val action = HomeFragmentDirections.actionNavigationHomeToConversationQuestionFragment(
                                questionId = randomQuestion.questionId,
                                type = randomQuestion.category,
                                answerType = randomQuestion.answerType,
                                isRandom = true

                            )
                            navController.navigate(action)
                        }
                }
            }
        }
    }



    private fun setBlock(blockData: Block) {
        if (blockData.blocks.isEmpty() ){
            setNoBlockBackground(true)
            return
        }
        setNoBlockBackground(false)
        for (data in blockData.blocks) {
            val color = when(data.category) {
                "DAILY" -> resources.getColor(R.color.orange_700)
                "SCHOOL" -> resources.getColor(R.color.blue_700)
                "HOBBY" -> resources.getColor(R.color.salmon_700)
                "FAMILY" -> resources.getColor(R.color.purple_700)
                "FRIEND" -> resources.getColor(R.color.green_700)
                else -> resources.getColor(R.color.orange_700)
            }
            // todo : 색 변경 방식 변경 필요
            data.blockPlace.forEach {
                blockList[it.row][it.col].setBackgroundColor(color)
            }
        }
    }

    private fun setNoBlockBackground(setBg: Boolean) {
        val visibility = if (setBg) View.VISIBLE else View.INVISIBLE
        binding.viewConversation.imageviewHomeNoblock.visibility = visibility
        binding.viewConversation.textviewHomeNoblock.visibility = visibility
    }

    private fun moveToQuestionList(
        theme: String,
    ) {
        val action  = HomeFragmentDirections.actionNavigationHomeToThemeQuestionListFragment(theme)
       navController.navigate(action)
    }

}
