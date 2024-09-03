package com.example.com_us.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.com_us.R
import com.example.com_us.data.response.home.Block
import com.example.com_us.data.response.home.Category
import com.example.com_us.databinding.FragmentHomeBinding
import com.example.com_us.util.ColorMatch
import com.example.com_us.util.ThemeType

class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var blockList: List<List<View>>

    private var _binding: FragmentHomeBinding? = null
    private var isReload: MutableLiveData<Boolean> = MutableLiveData(false)
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels { HomeViewModelFactory(requireContext()) }

    private val scrollChangedListener = ViewTreeObserver.OnScrollChangedListener {
        _binding?.let {
            it.swiperefreshHome.isEnabled = (it.scrollviewHome.scrollY == 0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        blockList = listOf(
            listOf(binding.viewHomeBlock00, binding.viewHomeBlock01, binding.viewHomeBlock02, binding.viewHomeBlock03),
            listOf(binding.viewHomeBlock10, binding.viewHomeBlock11, binding.viewHomeBlock12, binding.viewHomeBlock13),
            listOf(binding.viewHomeBlock20, binding.viewHomeBlock21, binding.viewHomeBlock22, binding.viewHomeBlock23),
            listOf(binding.viewHomeBlock30, binding.viewHomeBlock31, binding.viewHomeBlock32, binding.viewHomeBlock33),
        )

        homeViewModel.loadHomeData()

        setSwipeRefresh()
        setThemeClickListener()
        setHomeData()

        return root
    }

    private fun setSwipeRefresh() {
        binding.swiperefreshHome.setOnRefreshListener {
            homeViewModel.loadHomeData()
        }

        isReload.observe(viewLifecycleOwner) {
            if(it){
                isReload.value = false
                binding.swiperefreshHome.isRefreshing = false
            }
        }

        binding.scrollviewHome.viewTreeObserver.addOnScrollChangedListener(scrollChangedListener)
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
            val chatMinute = it.user.todayChatTime.substring(3, 5).toInt()
            binding.textviewHomeGreeting.text = String.format(resources.getString(R.string.home_title_greeting_user_hi), it.user.name, emojiText)
            binding.textviewHomeMinute.text = String.format(resources.getString(R.string.home_sub_today_conversation_minute), chatMinute)
            Glide.with(this)
                .load(it.user.imageUrl)
                .apply(RequestOptions().transform(RoundedCorners(300)))
                .into(binding.imageviewHomeUsericon)

            setThemeProgress(it.category)
            setBlock(it.block)
            isReload.value = true
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
        binding.includeHomeDaily.textviewThemeFraction.text = String.format(resources.getString(R.string.home_theme_fraction), categoryData.DailyCount, categoryData.DailyTotalCount)
        binding.includeHomeDaily.textviewThemePercent.text = String.format(resources.getString(R.string.home_theme_percent), categoryData.DailyPercent)
        binding.includeHomeDaily.progressbarTheme.progress = categoryData.DailyPercent.toInt()

        binding.includeHomeSchool.textviewThemeFraction.text = String.format(resources.getString(R.string.home_theme_fraction), categoryData.SchoolCount, categoryData.SchoolTotalCount)
        binding.includeHomeSchool.textviewThemePercent.text = String.format(resources.getString(R.string.home_theme_percent), categoryData.SchoolPercent)
        binding.includeHomeSchool.progressbarTheme.progress = categoryData.SchoolPercent.toInt()

        binding.includeHomeFriend.textviewThemeFraction.text = String.format(resources.getString(R.string.home_theme_fraction), categoryData.FriendCount, categoryData.FriendTotalCount)
        binding.includeHomeFriend.textviewThemePercent.text = String.format(resources.getString(R.string.home_theme_percent), categoryData.FriendPercent)
        binding.includeHomeFriend.progressbarTheme.progress = categoryData.FriendPercent.toInt()

        binding.includeHomeFamily.textviewThemeFraction.text = String.format(resources.getString(R.string.home_theme_fraction), categoryData.FamilyCount, categoryData.FamilyTotalCount)
        binding.includeHomeFamily.textviewThemePercent.text = String.format(resources.getString(R.string.home_theme_percent), categoryData.FamilyPercent)
        binding.includeHomeFamily.progressbarTheme.progress = categoryData.FamilyPercent.toInt()

        binding.includeHomeHobby.textviewThemeFraction.text = String.format(resources.getString(R.string.home_theme_fraction), categoryData.HobbyCount, categoryData.HobbyTotalCount)
        binding.includeHomeHobby.textviewThemePercent.text = String.format(resources.getString(R.string.home_theme_percent), categoryData.HobbyPercent)
        binding.includeHomeHobby.progressbarTheme.progress = categoryData.HobbyPercent.toInt()
    }

    private fun setBlock(blockData: List<Block>){
        if(blockData.isNotEmpty()) setNoBlockBackground(false) else setNoBlockBackground(true)
        var color: Int?
        for(data in blockData) {
            color = ColorMatch.findColorFromKor(data.category)
            if(color != null){
                blockList[data.blockRow][data.blockColumn].setBackgroundResource(color)
            }
        }
    }
    private fun setNoBlockBackground(setBg: Boolean) {
        val visibility = if(setBg) View.VISIBLE else View.INVISIBLE
        binding.imageviewHomeNoblock.visibility = visibility
        binding.textviewHomeNoblock.visibility = visibility
    }
    private fun moveToQuestionList(theme: String, themeKor: String) {
        val intent = Intent(context, HomeThemeQuestionListActivity::class.java)
        intent.putExtra("theme", theme)
        intent.putExtra("themeKor", themeKor)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        binding.scrollviewHome.viewTreeObserver.addOnScrollChangedListener(scrollChangedListener)
    }

    override fun onPause() {
        super.onPause()
        binding.scrollviewHome.viewTreeObserver?.removeOnScrollChangedListener(scrollChangedListener)
    }

    override fun onDestroyView() {
        binding.scrollviewHome.viewTreeObserver?.removeOnScrollChangedListener(scrollChangedListener)
        super.onDestroyView()
        _binding = null
    }
}
