package com.example.com_us.ui.home

import android.content.Intent
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.com_us.R
import com.example.com_us.base.fragment.BaseFragment
import com.example.com_us.data.model.home.Block
import com.example.com_us.data.model.home.Category
import com.example.com_us.databinding.FragmentHomeBinding
import com.example.com_us.ui.base.UiState
import com.example.com_us.ui.question.theme.ThemeQuestionListActivity
import com.example.com_us.util.ColorMatch
import com.example.com_us.util.ThemeType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment :
    BaseFragment<FragmentHomeBinding, HomeViewModel>(FragmentHomeBinding::inflate),
    View.OnClickListener {
    override val viewModel: HomeViewModel by viewModels()
    private lateinit var blockList: List<List<View>>
    private var isReload: MutableLiveData<Boolean> = MutableLiveData(false)

    private val homeViewModel: HomeViewModel by viewModels()

//    private val scrollChangedListener =
//        ViewTreeObserver.OnScrollChangedListener {
//            binding.let {
//                it.swiperefreshHome.isEnabled = (it.scrollviewHome.scrollY == 0)
//            }
//        }

    override fun onBindLayout() {
        super.onBindLayout()
        blockList =
            listOf(
                listOf(
                    binding.viewConversation.viewHomeBlock00,
                    binding.viewConversation.viewHomeBlock01,
                    binding.viewConversation.viewHomeBlock02,
                    binding.viewConversation.viewHomeBlock03,
                ),
                listOf(
                    binding.viewConversation.viewHomeBlock10,
                    binding.viewConversation.viewHomeBlock11,
                    binding.viewConversation.viewHomeBlock12,
                    binding.viewConversation.viewHomeBlock13,
                ),
                listOf(
                    binding.viewConversation.viewHomeBlock20,
                    binding.viewConversation.viewHomeBlock21,
                    binding.viewConversation.viewHomeBlock22,
                    binding.viewConversation.viewHomeBlock23,
                ),
                listOf(
                    binding.viewConversation.viewHomeBlock30,
                    binding.viewConversation.viewHomeBlock31,
                    binding.viewConversation.viewHomeBlock32,
                    binding.viewConversation.viewHomeBlock33,
                ),
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
//        binding.scrollviewHome.viewTreeObserver.addOnScrollChangedListener(scrollChangedListener)
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

    private fun setHomeData() {
        val emojiText = String(Character.toChars(resources.getInteger(R.integer.waving_hand_sign)))
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.homeUiState.collect {
                    when (it) {
                        is UiState.Success -> {
                            binding.swiperefreshHome.isRefreshing = false
                            binding.progressBar.visibility = View.GONE
                            binding.constraintHome.visibility = View.VISIBLE

                            binding.viewUserConversationInfoBox.textviewHomeGreeting.text =
                                String.format(
                                    resources.getString(R.string.home_title_greeting_user_hi),
                                    it.data.user.name,
                                    emojiText,
                                )

                            Glide
                                .with(this@HomeFragment)
                                .load(it.data.user.imageUrl)
                                .apply(RequestOptions().transform(RoundedCorners(300)))
                                .into(binding.viewUserConversationInfoBox.imageviewHomeUsericon)

                            setThemeProgress(it.data.category)
                            setBlock(it.data.block)
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
        binding.viewConversation.includeHomeDaily.textviewThemeFraction.text =
            String.format(resources.getString(R.string.home_theme_fraction), categoryData.DailyCount, categoryData.DailyTotalCount)
        binding.viewConversation.includeHomeDaily.textviewThemePercent.text =
            String.format(resources.getString(R.string.home_theme_percent), categoryData.DailyPercent)
        binding.viewConversation.includeHomeDaily.progressbarTheme.progress = categoryData.DailyPercent.toInt()

        binding.viewConversation.includeHomeSchool.textviewThemeFraction.text =
            String.format(resources.getString(R.string.home_theme_fraction), categoryData.SchoolCount, categoryData.SchoolTotalCount)
        binding.viewConversation.includeHomeSchool.textviewThemePercent.text =
            String.format(resources.getString(R.string.home_theme_percent), categoryData.SchoolPercent)
        binding.viewConversation.includeHomeSchool.progressbarTheme.progress = categoryData.SchoolPercent.toInt()

        binding.viewConversation.includeHomeFriend.textviewThemeFraction.text =
            String.format(resources.getString(R.string.home_theme_fraction), categoryData.FriendCount, categoryData.FriendTotalCount)
        binding.viewConversation.includeHomeFriend.textviewThemePercent.text =
            String.format(resources.getString(R.string.home_theme_percent), categoryData.FriendPercent)
        binding.viewConversation.includeHomeFriend.progressbarTheme.progress = categoryData.FriendPercent.toInt()

        binding.viewConversation.includeHomeFamily.textviewThemeFraction.text =
            String.format(resources.getString(R.string.home_theme_fraction), categoryData.FamilyCount, categoryData.FamilyTotalCount)
        binding.viewConversation.includeHomeFamily.textviewThemePercent.text =
            String.format(resources.getString(R.string.home_theme_percent), categoryData.FamilyPercent)
        binding.viewConversation.includeHomeFamily.progressbarTheme.progress = categoryData.FamilyPercent.toInt()

        binding.viewConversation.includeHomeHobby.textviewThemeFraction.text =
            String.format(resources.getString(R.string.home_theme_fraction), categoryData.HobbyCount, categoryData.HobbyTotalCount)
        binding.viewConversation.includeHomeHobby.textviewThemePercent.text =
            String.format(resources.getString(R.string.home_theme_percent), categoryData.HobbyPercent)
        binding.viewConversation.includeHomeHobby.progressbarTheme.progress = categoryData.HobbyPercent.toInt()
    }

    private fun setBlock(blockData: List<Block>) {
        if (blockData.isNotEmpty()) setNoBlockBackground(false) else setNoBlockBackground(true)
        var color: Int?
        for (data in blockData) {
            color = ColorMatch.findColorFromKor(data.category)
            if (color != null) {
                // todo : 색 변경 방식 변경 필요
                blockList[data.blockRow][data.blockColumn].setBackgroundResource(color)
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
        themeKor: String,
    ) {
        val intent = Intent(context, ThemeQuestionListActivity::class.java)
        intent.putExtra("theme", theme)
        intent.putExtra("themeKor", themeKor)
        startActivity(intent)
    }

//    override fun onResume() {
//        super.onResume()
//        binding.scrollviewHome.viewTreeObserver.addOnScrollChangedListener(scrollChangedListener)
//    }
//
//    override fun onPause() {
//        super.onPause()
//        binding.scrollviewHome.viewTreeObserver?.removeOnScrollChangedListener(scrollChangedListener)
//    }
//
//    override fun onDestroyView() {
//        binding.scrollviewHome.viewTreeObserver?.removeOnScrollChangedListener(scrollChangedListener)
//        super.onDestroyView()
//    }
}
