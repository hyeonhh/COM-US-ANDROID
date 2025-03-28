package com.example.com_us.ui.profile

import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout.LayoutParams
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.com_us.R
import com.example.com_us.base.fragment.BaseFragment
import com.example.com_us.data.repository.ProfileRepository
import com.example.com_us.databinding.FragmentProfileBinding
import com.example.com_us.ui.base.UiState
import com.example.com_us.util.ServerResponseHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding,ProfileViewModel>(
    FragmentProfileBinding::inflate
){

    override val viewModel : ProfileViewModel by viewModels()



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindLayout() {
        super.onBindLayout()
        setProfile()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setProfile() {
        lifecycleScope.launch {
            viewModel.profileUiState.collect {
                when (it) {
                    is UiState.Success -> {
                        binding.constraintProfile.visibility = View.VISIBLE
                        val themeGraphRatioList: List<Float> = listOf(
                            it.data.answerStatistic.dailyQuestionRatio.toFloat(),
                            it.data.answerStatistic.schoolQuestionRatio.toFloat(),
                            it.data.answerStatistic.friendQuestionRatio.toFloat(),
                            it.data.answerStatistic.familyQuestionRatio.toFloat(),
                            it.data.answerStatistic.hobbyQuestionRatio.toFloat(),
                        )

                        setProfile(it.data.userInfo.name, it.data.userInfo.imageUrl)
                        setStatic(it.data.userInfo.totalChatTime, it.data.userInfo.totalChatCount)
                        changeTypeGraph(
                            it.data.answerStatistic.multipleChoiceRatio.toFloat(),
                            it.data.answerStatistic.sentenceRatio.toFloat()
                        )
                        changeThemeGraph(themeGraphRatioList)
                    }

                    is UiState.Error -> {
                        Toast.makeText(context,"잠시 후에 다시 시도해주세요!",Toast.LENGTH_SHORT).show()
                    }

                    is UiState.Loading -> {}
                }

            }
        }
    }

    private fun setProfile(username: String, profileImgUrl: String) {
        Log.d("profile","setProfile")
        binding.textviewProfileNickname.text = String.format(resources.getString(R.string.profile_username), username)
        Glide.with(this)
            .load(profileImgUrl)
            .apply(RequestOptions().transform(RoundedCorners(300)))
            .into(binding.imageviewProfileUsericon)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setStatic(totalChatTime: String, totalChatCount: Int) {
        Log.d("profile","setStatic")
        val totalChatTime = stringToLocalTime(totalChatTime)
        binding.textviewTimeTaken.text = String.format(resources.getString(R.string.profile_time_taken_minute), totalChatTime.toSecondOfDay() / 60)
        binding.textviewConvCount.text = String.format(resources.getString(R.string.profile_conv_count_count), totalChatCount)
    }

    private fun changeTypeGraph(choiceRatio: Float, interactionRatio: Float) {
        Log.d("profile","changeTypeGraph")
        changeGraphShape(R.drawable.shape_type_graph_fill_stroke_rect34_blue, choiceRatio, GraphPosition.LEFT)
        changeGraphShape(R.drawable.shape_type_graph_fill_stroke_rect34_pink, interactionRatio, GraphPosition.RIGHT)

        changeGraphLayoutWeight(binding.viewProfileGraphChoice, choiceRatio)
        changeGraphLayoutWeight(binding.viewProfileGraphInteraction, interactionRatio)

        binding.includeProfileGraphTypeChoice.textviewGraphPercent.text = String.format(resources.getString(R.string.percent), choiceRatio.toInt())
        binding.includeProfileGraphTypeInteraction.textviewGraphPercent.text = String.format(resources.getString(R.string.percent), interactionRatio.toInt())
    }

    private fun changeThemeGraph(themeGraphRatioList: List<Float>) {
        Log.d("profile","changeThemeGraph")
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
                if(leftThemeIdx == i && rightThemeIdx == i) changeGraphShape(themeGraphBg[i], 100f, GraphPosition.LEFT)
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

        print(binding)
        binding.includeProfileGraphThemeDaily.textviewGraphPercent.text = String.format(resources.getString(R.string.percent), themeGraphRatioList[0].toInt())
        binding.includeProfileGraphThemeSchool.textviewGraphPercent.text = String.format(resources.getString(R.string.percent), themeGraphRatioList[1].toInt())
        binding.includeProfileGraphThemeFriend.textviewGraphPercent.text = String.format(resources.getString(R.string.percent), themeGraphRatioList[2].toInt())
        binding.includeProfileGraphThemeFamily.textviewGraphPercent.text = String.format(resources.getString(R.string.percent), themeGraphRatioList[3].toInt())
        binding.includeProfileGraphThemeInterest.textviewGraphPercent.text = String.format(resources.getString(R.string.percent), themeGraphRatioList[4].toInt())
    }

    private fun changeGraphLayoutWeight(view: View, ratio: Float) {
        Log.d("profile","changeGraphLayoutWeight")
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
        Log.d("profile","changeGraphShape")
        val drawable = context?.let { ContextCompat.getDrawable(it, graphDrawableId) } as? GradientDrawable
        if(ratio >= 100.0) {
            drawable?.cornerRadius = 40f
        } else {
            drawable?.apply {
                var radius = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
                // 반경을 각각의 모서리에 적용: 순서대로 [top-left, top-right, bottom-right, bottom-left]
                if(position == GraphPosition.LEFT) radius = floatArrayOf(40f, 40f, 0f, 0f, 0f, 0f, 40f, 40f)
                else if(position == GraphPosition.MIDDLE) radius = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
                else if(position == GraphPosition.RIGHT) radius = floatArrayOf(0f, 0f, 40f, 40f, 40f, 40f, 0f, 0f)

                this.cornerRadii = radius
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun stringToLocalTime(timeString: String): LocalTime {
        Log.d("profile","stringToLocalTime")
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")

        return LocalTime.parse(timeString, formatter)
    }

    enum class GraphPosition {
        RIGHT, MIDDLE, LEFT
    }

}