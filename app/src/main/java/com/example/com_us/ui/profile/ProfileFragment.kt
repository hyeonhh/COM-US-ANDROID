package com.example.com_us.ui.profile

import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout.LayoutParams
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.com_us.R
import com.example.com_us.databinding.FragmentProfileBinding
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val profileViewModel: ProfileViewModel by viewModels { ProfileViewModelFactory(requireContext()) }
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        profileViewModel.loadProfileData()

        setProfile()

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setProfile() {
        profileViewModel.profileData.observe(viewLifecycleOwner) {
            val themeGraphRatioList: List<Float> = listOf(
                it.answerStatistic.dailyQuestionRatio.toFloat(),
                it.answerStatistic.schoolQuestionRatio.toFloat(),
                it.answerStatistic.friendQuestionRatio.toFloat(),
                it.answerStatistic.familyQuestionRatio.toFloat(),
                it.answerStatistic.hobbyQuestionRatio.toFloat(),
            )
            setProfile(it.userInfo.name, it.userInfo.imageUrl)
            setStatic(it.userInfo.totalChatTime, it.userInfo.totalChatCount)
            changeTypeGraph(it.answerStatistic.multipleChoiceRatio.toFloat(), it.answerStatistic.sentenceRatio.toFloat())
            changeThemeGraph(themeGraphRatioList)
        }
    }

    private fun setProfile(username: String, profileImgUrl: String) {
        binding.textviewProfileNickname.text = String.format(resources.getString(R.string.profile_username), username)
        Glide.with(this)
            .load(profileImgUrl)
            .apply(RequestOptions().transform(RoundedCorners(300)))
            .into(binding.imageviewProfileUsericon)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setStatic(totalChatTime: String, totalChatCount: Int) {
        val totalChatTime = stringToLocalTime(totalChatTime)
        binding.textviewTimeTaken.text = String.format(resources.getString(R.string.profile_time_taken_minute), totalChatTime.toSecondOfDay() / 60)
        binding.textviewConvCount.text = String.format(resources.getString(R.string.profile_conv_count_count), totalChatCount)
    }

    private fun changeTypeGraph(choiceRatio: Float, interactionRatio: Float) {
        changeGraphShape(R.drawable.shape_type_graph_fill_stroke_rect34_blue, choiceRatio, GraphPosition.LEFT)
        changeGraphShape(R.drawable.shape_type_graph_fill_stroke_rect34_pink, interactionRatio, GraphPosition.RIGHT)

        changeGraphLayoutWeight(binding.viewProfileGraphChoice, choiceRatio)
        changeGraphLayoutWeight(binding.viewProfileGraphInteraction, interactionRatio)

        binding.includeProfileGraphTypeChoice.textviewGraphPercent.text = String.format(resources.getString(R.string.percent), choiceRatio.toInt())
        binding.includeProfileGraphTypeInteraction.textviewGraphPercent.text = String.format(resources.getString(R.string.percent), interactionRatio.toInt())
    }

    private fun changeThemeGraph(themeGraphRatioList: List<Float>) {
        val themeGraphBg: List<Int> = listOf(
            R.drawable.shape_theme_graph_fill_rect_orange700,
            R.drawable.shape_theme_graph_fill_rect_blue700,
            R.drawable.shape_theme_graph_fill_rect_green700,
            R.drawable.shape_theme_graph_fill_rect_purple700,
            R.drawable.shape_theme_graph_fill_rect_salmon700,
        )

        var leftThemeIdx = -1
        var rightThemeIdx = -1
        for((i, tRatio) in themeGraphRatioList.withIndex()) {
            if(tRatio > 0) {
                rightThemeIdx = i
                if(leftThemeIdx == -1)  leftThemeIdx = i
            }
        }

        for((i, tRatio) in themeGraphRatioList.withIndex()) {
            if(rightThemeIdx != -1) {
                if(leftThemeIdx == i && rightThemeIdx == i) changeGraphShape(themeGraphBg[i], tRatio, GraphPosition.LEFT)
                else if(leftThemeIdx == i) changeGraphShape(themeGraphBg[i], tRatio, GraphPosition.LEFT)
                else if(rightThemeIdx == i) changeGraphShape(themeGraphBg[i], tRatio, GraphPosition.RIGHT)
                else changeGraphShape(themeGraphBg[i], tRatio, GraphPosition.MIDDLE)
            } else {
            }
        }


        changeGraphLayoutWeight(binding.viewProfileThemeGraphDaily, themeGraphRatioList[0])
        changeGraphLayoutWeight(binding.viewProfileThemeGraphSchool, themeGraphRatioList[1])
        changeGraphLayoutWeight(binding.viewProfileThemeGraphFriend, themeGraphRatioList[2])
        changeGraphLayoutWeight(binding.viewProfileThemeGraphFamily, themeGraphRatioList[3])
        changeGraphLayoutWeight(binding.viewProfileThemeGraphInterest, themeGraphRatioList[4])

        binding.includeProfileGraphThemeDaily.textviewGraphPercent.text = String.format(resources.getString(R.string.percent), themeGraphRatioList[0].toInt())
        binding.includeProfileGraphThemeSchool.textviewGraphPercent.text = String.format(resources.getString(R.string.percent), themeGraphRatioList[1].toInt())
        binding.includeProfileGraphThemeFriend.textviewGraphPercent.text = String.format(resources.getString(R.string.percent), themeGraphRatioList[2].toInt())
        binding.includeProfileGraphThemeFamily.textviewGraphPercent.text = String.format(resources.getString(R.string.percent), themeGraphRatioList[3].toInt())
        binding.includeProfileGraphThemeInterest.textviewGraphPercent.text = String.format(resources.getString(R.string.percent), themeGraphRatioList[4].toInt())
    }

    private fun changeGraphLayoutWeight(view: View, ratio: Float) {
        val marginInDp = resources.getDimensionPixelSize(R.dimen.profile_graph_margin)
        view.layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.MATCH_PARENT,
            100-ratio
        ).apply {
            setMargins(marginInDp, 0, marginInDp, 0)
        }
    }

    private fun changeGraphShape(graphDrawableId: Int, ratio: Float, position: GraphPosition) {
        val drawable = context?.let { ContextCompat.getDrawable(it, graphDrawableId) } as? GradientDrawable
        if(ratio >= 100.0) {
            drawable?.cornerRadius = 34f
        } else {
            drawable?.apply {
                var radius = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
                // 반경을 각각의 모서리에 적용: 순서대로 [top-left, top-right, bottom-right, bottom-left]
                if(position == GraphPosition.LEFT) radius = floatArrayOf(30f, 30f, 0f, 0f, 0f, 0f, 30f, 30f)
                else if(position == GraphPosition.MIDDLE) radius = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
                else if(position == GraphPosition.RIGHT) radius = floatArrayOf(0f, 0f, 30f, 30f, 30f, 30f, 0f, 0f)

                this.cornerRadii = radius
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun stringToLocalTime(timeString: String): LocalTime {
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")

        return LocalTime.parse(timeString, formatter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    enum class GraphPosition {
        RIGHT, MIDDLE, LEFT
    }
}