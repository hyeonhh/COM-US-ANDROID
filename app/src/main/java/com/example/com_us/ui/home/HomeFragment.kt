package com.example.com_us.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.com_us.R
import com.example.com_us.data.response.question.Category
import com.example.com_us.databinding.FragmentHomeBinding
import com.example.com_us.ui.ThemeType
import com.example.com_us.ui.home.theme.ThemeQuestionListActivity
import com.example.com_us.ui.question.QuestionListItem
import com.example.com_us.ui.question.QuestionViewModel
import com.example.com_us.ui.question.QuestionViewModelFactory

class HomeFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels { HomeViewModelFactory(requireContext()) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        homeViewModel.loadHomeData()

        setThemeClickListener()
        setHomeData()

        return root
    }

    private fun setThemeClickListener() {
        binding.includeHomeDaily.constraint.setOnClickListener(this)
        binding.includeHomeSchool.constraint.setOnClickListener(this)
        binding.includeHomeFriend.constraint.setOnClickListener(this)
        binding.includeHomeFamily.constraint.setOnClickListener(this)
        binding.includeHomeHobby.constraint.setOnClickListener(this)
        binding.includeHomeRandom.constraint.setOnClickListener(this)
    }

    private fun setHomeData() {
        val emojiText = String(Character.toChars(resources.getInteger(R.integer.waving_hand_sign)))
        homeViewModel.homeData.observe(viewLifecycleOwner) {
            val chatMinute = it.user.todayChatTime.substring(3, 5)
            binding.textviewHomeGreeting.text = String.format(resources.getString(R.string.home_title_greeting_user_hi), it.user.name, emojiText)
            binding.textviewHomeMinute.text = String.format(resources.getString(R.string.home_sub_today_conversation_minute), chatMinute)
            setThemeProgress(it.category)
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.include_home_daily -> {
                moveToQuestionList(ThemeType.DAILY.toString(), ThemeType.DAILY.kor)
            }
            R.id.include_home_school -> {
                moveToQuestionList(ThemeType.SCHOOL.toString(), ThemeType.SCHOOL.kor)
            }
            R.id.include_home_friend -> {
                moveToQuestionList(ThemeType.FRIEND.toString(), ThemeType.FRIEND.kor)
            }
            R.id.include_home_family -> {
                moveToQuestionList(ThemeType.FAMILY.toString(), ThemeType.FAMILY.kor)
            }
            R.id.include_home_hobby -> {
                moveToQuestionList(ThemeType.HOBBY.toString(), ThemeType.HOBBY.kor)
            }
            R.id.include_home_random -> {
                moveToQuestionList(ThemeType.RANDOM.toString(), ThemeType.RANDOM.kor)
            }
        }
    }

    private fun setThemeProgress(categoryData: Category) {
        binding.includeHomeDaily.textviewThemeFraction.text = String.format(resources.getString(R.string.home_theme_fraction), categoryData.dailyCount, categoryData.dailyTotalCount)
        binding.includeHomeDaily.textviewThemePercent.text = String.format(resources.getString(R.string.home_theme_percent), categoryData.dailyPercent)
        binding.includeHomeDaily.progressbarTheme.progress = categoryData.dailyPercent.toInt()

        binding.includeHomeSchool.textviewThemeFraction.text = String.format(resources.getString(R.string.home_theme_fraction), categoryData.schoolCount, categoryData.schoolTotalCount)
        binding.includeHomeSchool.textviewThemePercent.text = String.format(resources.getString(R.string.home_theme_percent), categoryData.schoolPercent)
        binding.includeHomeSchool.progressbarTheme.progress = categoryData.schoolPercent.toInt()

        binding.includeHomeFriend.textviewThemeFraction.text = String.format(resources.getString(R.string.home_theme_fraction), categoryData.friendCount, categoryData.friendTotalCount)
        binding.includeHomeFriend.textviewThemePercent.text = String.format(resources.getString(R.string.home_theme_percent), categoryData.friendPercent)
        binding.includeHomeFriend.progressbarTheme.progress = categoryData.friendPercent.toInt()

        binding.includeHomeFamily.textviewThemeFraction.text = String.format(resources.getString(R.string.home_theme_fraction), categoryData.familyCount, categoryData.familyTotalCount)
        binding.includeHomeFamily.textviewThemePercent.text = String.format(resources.getString(R.string.home_theme_percent), categoryData.familyPercent)
        binding.includeHomeFamily.progressbarTheme.progress = categoryData.familyPercent.toInt()

        binding.includeHomeHobby.textviewThemeFraction.text = String.format(resources.getString(R.string.home_theme_fraction), categoryData.hobbyCount, categoryData.hobbyTotalCount)
        binding.includeHomeHobby.textviewThemePercent.text = String.format(resources.getString(R.string.home_theme_percent), categoryData.hobbyPercent)
        binding.includeHomeHobby.progressbarTheme.progress = categoryData.hobbyPercent.toInt()
    }
    private fun moveToQuestionList(theme: String, themeKor: String) {
        val intent = Intent(context, ThemeQuestionListActivity::class.java)
        intent.putExtra("theme", theme)
        intent.putExtra("themeKor", themeKor)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
