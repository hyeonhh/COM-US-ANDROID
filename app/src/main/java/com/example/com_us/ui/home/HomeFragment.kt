package com.example.com_us.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.com_us.R
import com.example.com_us.databinding.FragmentHomeBinding
import com.example.com_us.ui.ThemeType
import com.example.com_us.ui.home.theme.ThemeQuestionListActivity

class HomeFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/

        binding.includeHomeDaily.constraint.setOnClickListener(this)
        binding.includeHomeSchool.constraint.setOnClickListener(this)
        binding.includeHomeFriend.constraint.setOnClickListener(this)
        binding.includeHomeFamily.constraint.setOnClickListener(this)
        binding.includeHomeHobby.constraint.setOnClickListener(this)
        binding.includeHomeRandom.constraint.setOnClickListener(this)

        return root
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
